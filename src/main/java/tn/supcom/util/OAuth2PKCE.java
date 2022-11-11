package tn.supcom.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import jakarta.ejb.Singleton;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import tn.supcom.controllers.Role;
import tn.supcom.controllers.WoTRoleUtility;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import tn.supcom.entities.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;


@SuppressWarnings("unused")
@Singleton
public class OAuth2PKCE {
    public static final String AuthenticationSchemePrefix = "Bearer ";

    private final Map<String,String> challenges = new HashMap<>();
    private final Map<String,String> codes = new HashMap<>();
    private final Map<String,Identity> identities = new HashMap<>();



    public String addChallenge(String codeChallenge,String clientId){
        String signInId = clientId+"#"+UUID.randomUUID().toString();
        challenges.put(codeChallenge,signInId);
        return signInId;
    }

    public String generateAuthorizationCode(String signInId, Identity identity){
        String code = UUID.randomUUID().toString();
        codes.put(signInId,code);
        identities.put(code,identity);
        return code;
    }

    public String checkCode(String code,String codeVerifier) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(codeVerifier.getBytes(StandardCharsets.UTF_8));
        String key = Base64.getEncoder().encodeToString(md.digest());
        if(challenges.containsKey(key)){
            if(codes.get(challenges.get(key)).equals(code)){
                codes.remove(challenges.get(key));
                challenges.remove(key);
                return generateTokenFor(identities.remove(code));
            }
        }
        codes.entrySet().stream().filter(e -> e.getValue().equals(code)).findFirst().ifPresent(
                e -> {
                   codes.remove(e.getKey());
                   challenges.entrySet().stream().filter(f -> f.getValue().equals(e.getKey())).findFirst()
                           .ifPresent(f->challenges.remove(f.getKey()));
                }
        );
        identities.remove(code);
        return null;
    }

    private static final Config config = ConfigProvider.getConfig();
    static {
        FileInputStream fis = null;
        char[] password = config.getValue("jwtSecret",String.class).toCharArray();
        String alias = config.getValue("jwtAlias",String.class);
        PrivateKey pk = null;
        PublicKey pub = null;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            String configDir = System.getProperty("jboss.server.config.dir");
            String keystorePath = configDir + File.separator + "jwt.keystore";
            fis = new FileInputStream(keystorePath);
            ks.load(fis, password);
            Key key = ks.getKey(alias, password);
            if (key instanceof PrivateKey) {
                pk = (PrivateKey) key;
                // Get certificate of public key
                java.security.cert.Certificate cert = ks.getCertificate(alias);
                pub = cert.getPublicKey();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {}
            }
        }
        privateKey = pk;
        publicKey = pub;
    }

    private static final PrivateKey privateKey;
    static final PublicKey publicKey;
    private static final int TOKEN_VALIDITY = config.getValue("jwtTokenValidity",Integer.class);
    public static final String CLAIM_ROLES = config.getValue("jwtClaimRoles",String.class);
    static final String ISSUER = config.getValue("jwtIssuer",String.class);
    static final String AUDIENCE = config.getValue("jwtAudience",String.class);
    private final JWSSigner signer = new RSASSASigner(privateKey);

    private Role[] getRoles(Long permissionLevel) {
        Set<Role> roles = new HashSet<>();
        for(Role role: Role.values()){
            if((permissionLevel & role.getValue()) != 0L){
                roles.add(role);
            }
        }
        return roles.toArray(new Role[0]);
    }

    public String generateTokenFor(Identity identity) throws Exception {
        JsonArrayBuilder rolesBuilder = Json.createArrayBuilder();
        for (Role role : getRoles(identity.getPermissionLevel())) {
            rolesBuilder.add(role.toString());
        }
       /* JsonArrayBuilder audienceBuilder = Json.createArrayBuilder();
        for(String aud:AUDIENCE) { audienceBuilder.add(aud);}*/
        long currentTime = System.currentTimeMillis() / 1000;
        JsonObjectBuilder claimsBuilder = Json.createObjectBuilder()
                .add("sub", identity.getName())
                .add("iss", ISSUER)
                .add("aud", AUDIENCE)
                .add(CLAIM_ROLES, rolesBuilder.build())
                .add("iat",currentTime)
                .add("nbf",currentTime + 1)
                .add("exp",currentTime + TOKEN_VALIDITY)
                .add("jti", "urn:phenix:token:"+UUID.randomUUID());

        JWSObject jwsObject = new JWSObject(new JWSHeader.Builder(JWSAlgorithm.RS512)
                .type(new JOSEObjectType("jwt")).build(),
                new Payload(claimsBuilder.build().toString()));

        jwsObject.sign(signer);

        return jwsObject.serialize();
    }

    private static Mac hmac;

    static {
        try {
            hmac = Mac.getInstance("HmacSHA256");
            SecureRandom secureRandom = new SecureRandom();
            SecretKeySpec secret_key = new SecretKeySpec(secureRandom.generateSeed(32), "HmacSHA256");
            hmac.init(secret_key);
        } catch (NoSuchAlgorithmException | InvalidKeyException  e) {
            e.printStackTrace();
        }
    }

    public String hmacSignature(String toSign){
        return Base64.getEncoder().encodeToString(hmac.doFinal(toSign.getBytes()));
    }

    public String generateRefreshTokenFor(String accessToken){
        String refreshPayload = accessToken.substring(0,accessToken.lastIndexOf("."));
        String some = new SecureRandom().ints(48,123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(64)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return some+"."+hmacSignature(refreshPayload+some);
    }

    public boolean check(String accessTokenHeaderAndPayload,String refresh){
        String[] split =refresh.split("\\.");
        return hmacSignature(accessTokenHeaderAndPayload + split[0]).equals(split[1]);
    }

    public String generateXSSToken(String credential,String baseURI) {
        long currentTime = System.currentTimeMillis() / 1000;
        JsonObjectBuilder claimsBuilder = Json.createObjectBuilder()
                .add("sub", credential)
                .add("iss", ISSUER)
                .add("aud", AUDIENCE)
                .add("uri", baseURI)
                .add("iat", currentTime)
                .add("nbf",currentTime + 1)
                .add("exp", currentTime+86407L)
                .add("jti", "urn:phenix:token:"+UUID.randomUUID());

        JWSObject jwsObject = new JWSObject(new JWSHeader.Builder(JWSAlgorithm.RS512)
                .type(new JOSEObjectType("jwt")).build(),
                new Payload(claimsBuilder.build().toString()));
        try {
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return jwsObject.serialize();
    }
}

