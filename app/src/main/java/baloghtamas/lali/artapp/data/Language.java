package baloghtamas.lali.artapp.data;

public enum Language {
        ENGLISH(0),
        GERMAN(1),
        FRENCH(2),
        FINNISH(3);

        private final int code;

        Language(int language) {
            this. code = language;
        }

    public int getCode() {
        return code;
    }
}
