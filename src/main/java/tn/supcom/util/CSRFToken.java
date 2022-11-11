package tn.supcom.util;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Named
@Singleton
public class CSRFToken {
    private static final String HMAC_SHA_ALGORITHM = "HmacSHA384";

    private final Mac mac = Mac.getInstance(HMAC_SHA_ALGORITHM);

    public CSRFToken() throws NoSuchAlgorithmException, InvalidKeyException {
        Config config = ConfigProvider.getConfig();
        SecretKeySpec signingKey = new SecretKeySpec(config.getValue("csrfTokenSecret",String.class).getBytes(), HMAC_SHA_ALGORITHM);
        mac.init(signingKey);
    }

    public String sign(String clientIdOrSessionId){
        String dateTimeStamp = LocalDateTime.now().toString();
        String toHash = clientIdOrSessionId+dateTimeStamp;
        String csrfToken = Base64.getEncoder().encodeToString(mac.doFinal(toHash.getBytes()));
        csrfToken += ","+dateTimeStamp;
        return csrfToken;
    }

    public boolean verify(String clientIdOrSessionId,String csrfToken){
        String[] toVerify = csrfToken.split(",");
        String toHash = clientIdOrSessionId+toVerify[1];
        String expected = Base64.getEncoder().encodeToString(mac.doFinal(toHash.getBytes()));
        return expected.equals(toVerify[0]);
    }

}
