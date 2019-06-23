package baloghtamas.lali.artapp.data;

public interface IPreferencesHelper {
    void setLanguage(Language language);
    Language getLanguage();
    boolean getAlreadyOnBoardStatus();
    void setAlreadyOnBoardStatusToTrue();
    String getCurrentGameType();
    void setCurrentGameType(String gameType);
    int getSelectedLesson();
    void setSelectedLesson(int lesson);
    int getSelectedLevel();
    void setSelectedLevel(int level);

}
