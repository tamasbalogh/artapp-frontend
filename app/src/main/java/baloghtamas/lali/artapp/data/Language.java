package baloghtamas.lali.artapp.data;

import java.util.HashMap;
import java.util.Map;

public enum Language {
        ENGLISH("en"),
        GERMANY("de"),
        FRANCE("fr"),
        FINNISH("fi");

        private final String code;
        private static final Map<String,Language> valuesByCode;

    static {
        valuesByCode = new HashMap<>();
        for(Language language : Language.values()) {
            valuesByCode.put(language.code, language);
        }
    }

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Language lookupByCode(String code) {
        return valuesByCode.get(code);
    }
}