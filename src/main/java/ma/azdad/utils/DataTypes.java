package ma.azdad.utils;

import java.io.Serializable;

public enum DataTypes implements Serializable {
    PHOTO("photo"),
    EMAIL("email"),
    JOB("job"),
    NAME("name"),
    PHONE("phone"),
    LM("lm"),
    SLM("slm"),
    LOM("lom"),
    HR("hr"),
    LOB("lob");
    

    private String value;

    private DataTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
    	return this.value;
    }
}
