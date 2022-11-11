package tn.supcom.entities;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import tn.supcom.util.IdentityUtility;


import java.time.ZonedDateTime;

public class AuditListener {
    @PrePersist
    public void handleCreate(Object object){
        if(object instanceof Auditable that){
            //Auditable that = (Auditable) object; jdk<17
            AuditFields audit = that.getAuditFields();
            if(audit == null){
                audit = new AuditFields();
                that.setAuditFields(audit);
            }
            audit.setCreatedAt(ZonedDateTime.now().toLocalDateTime());
            audit.setCreatedBy(IdentityUtility.whoAmI());
        }
    }

    @PreUpdate
    public void handleUpdate(Object object){
        if(object instanceof Auditable that){ //jdk 17
            AuditFields audit = that.getAuditFields();
            audit.setUpdatedAt(ZonedDateTime.now().toLocalDateTime());
            audit.setUpdatedBy(IdentityUtility.whoAmI());
        }
    }
}
