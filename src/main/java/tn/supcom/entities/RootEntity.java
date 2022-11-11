package tn.supcom.entities;
import java.io.Serializable;

public interface RootEntity<ID extends Serializable> extends Serializable{
    ID getId();

    void setId(ID id);

    Long getVersion();

    void setVersion(Long version);
}
