package tn.supcom.util;

import java.io.Serializable;
import java.security.Principal;

public interface Identity extends Principal, Serializable {
    Long getPermissionLevel();
}
