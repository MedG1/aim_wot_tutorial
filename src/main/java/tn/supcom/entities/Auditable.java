package tn.supcom.entities;


public interface Auditable {
    AuditFields getAuditFields();
    void setAuditFields(AuditFields auditFields);
}
