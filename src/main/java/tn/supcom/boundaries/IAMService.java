package tn.supcom.boundaries;

import jakarta.ejb.EJBException;
import tn.supcom.util.Identity;
import tn.supcom.controllers.UserManager;
import tn.supcom.util.CSRFToken;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import tn.supcom.util.OAuth2PKCE;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Path("/")
@RequestScoped
public class IAMService {
    public final static String XSS_COOKIE_NAME = "xssCookie";

    @EJB
    private OAuth2PKCE oAuth2PKCE;

    @EJB
    private UserManager identityController;

    @Inject
    private CSRFToken csrfToken;

    @Context
    private UriInfo uriInfo;

    @POST
    @Path("/authorize")
    @Produces(MediaType.APPLICATION_JSON)
    public Response preSignIn(@HeaderParam("Pre-Authorization") String authorization){
        String decoded = new String(Base64.getDecoder().decode(authorization.substring("Bearer ".length())));
        String[] credentials = decoded.split(":");
        NewCookie cookie = new NewCookie(XSS_COOKIE_NAME,
                oAuth2PKCE.generateXSSToken(credentials[0],uriInfo.getBaseUri().getPath()),
                uriInfo.getBaseUri().getPath(),
                uriInfo.getBaseUri().getHost(),"Secure Http Only Cookie",86400,true,true);
        return Response .status(Response.Status.FOUND)
                        .cookie(cookie)
                        .entity("{\"signInId\":\""+oAuth2PKCE.addChallenge(credentials[1],credentials[0])+
                            "\",\"csrfToken\":\""+csrfToken.sign(credentials[0])+"\"}").build();
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(@FormParam("username")String username,
                           @FormParam("password")String password,
                           @FormParam("signInId") String signInId){
        try {
            username = java.net.URLDecoder.decode(username, StandardCharsets.UTF_8.name());
            password = java.net.URLDecoder.decode(password, StandardCharsets.UTF_8.name());
            signInId = java.net.URLDecoder.decode(signInId, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\":\""+e.getMessage()+"\"}").build();
        }
        if(username == null || password == null || signInId == null ||
                username.length()<4 || username.length()>8 || password.length()!=64){
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{\"message\":\"Invalid Credentials!\"}").build();
        }
        try {
            Identity identity = identityController.authenticate(username,password);
            return Response.ok()
                    .entity("{\"authCode\":\""+oAuth2PKCE.generateAuthorizationCode(signInId,identity)+"\"}")
                    .build();
        } catch (EJBException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\":\""+e.getMessage()+"\"}").build();
        }
    }

    @GET
    @Path("/oauth/token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSignIn(@HeaderParam("Post-Authorization") String authorization){
        String decoded = new String(Base64.getDecoder().decode(authorization.substring("Bearer ".length())));
        String[] credentials = decoded.split(":");
        String token;
        try {
            token = oAuth2PKCE.checkCode(credentials[0],credentials[1]);
        } catch (Exception e) {
            return Response.serverError().entity("{\"message\":\""+e.getMessage()+"\"}").build();
        }
        return token==null?Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Unauthorized Access!\"}").build():
                Response.ok().entity("{\"accessToken\":\""+token+"\",\"refreshToken\":\""+oAuth2PKCE.generateRefreshTokenFor(token)+"\"}").build();
    }

    @GET
    @Path("/oauth/token/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshSignIn(@HeaderParam("Refresh-Authorization")String refreshToken,@HeaderParam(HttpHeaders.AUTHORIZATION) String accessToken){
        String refreshPayload = accessToken.substring(0,accessToken.lastIndexOf("."));
        if(oAuth2PKCE.check(refreshPayload,refreshToken)){
            JsonParser parser = Json.createParser(new StringReader(refreshPayload.substring(refreshPayload.indexOf(".")+1)));
            Identity identity = identityController.findByUsername(parser.getObject().getString("sub"));
            String token;
            try {
                token = oAuth2PKCE.generateTokenFor(identity);
            } catch (Exception e) {
                return Response.serverError().entity("{\"message\":\""+e.getMessage()+"\"}").build();
            }
            return token == null ?Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Unauthorized Access!\"}").build():
                    Response.ok().entity("{\"accessToken\":\""+token+"\",\"refreshToken\":\""+oAuth2PKCE.generateRefreshTokenFor(token)+"\"}").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Unauthorized Access!\"}").build();
    }
}
