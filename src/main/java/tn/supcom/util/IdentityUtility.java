package tn.supcom.util;

public class IdentityUtility {
    private static final ThreadLocal<String> usernameTL = new ThreadLocal<>();

    public static void iAm(String username){
        usernameTL.set(username);
    }
    public static String whoAmI() {
        return usernameTL.get();
    }
}
