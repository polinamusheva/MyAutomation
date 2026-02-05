package com.example.my_automation.enums;

public enum SourceType {
    STANDARD,
    CONSULT,
    IMAGE;

    private SourceType() {
    }

    public static SourceType fromString(String text) {
        for(SourceType st : values()) {
            if (st.name().equalsIgnoreCase(text)) {
                return st;
            }
        }

        return null;
    }
}
