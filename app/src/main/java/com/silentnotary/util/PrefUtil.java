package com.silentnotary.util;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by albert on 9/29/17.
 */

public class PrefUtil {
    public static final String LOGIN_CREDENTIALS_FIELD_1 = "loginCredentials_1";
    public static final String LOGIN_CREDENTIALS_FIELD_2 = "loginCredentials_2";
    private static final String LATEST_UPLOADED_FILES = "latest_uploaded_files";
    private static final String SESSION_ID = "sessionId";
    public static final String NEED_TO_ENABLE_LOCKER = "need_to_enable_locker";
    private static final String USER_EMAIL = "user_email";

    public static String getUserEmail() {
        return Prefs.getString(USER_EMAIL, "");
    }

    public static void setUserEmail(String email) {
        Prefs.putString(USER_EMAIL, email);
    }


    public static List<File> getLatestUploadedFiles() {
        List<File> files = new ArrayList<>();
        try {
            Set<String> orderedStringSet = Prefs.getOrderedStringSet(LATEST_UPLOADED_FILES, new HashSet<>());

            for (String location : orderedStringSet) {
                File file = new File(location);
                if (file.exists() && file.length() > 0) {
                    files.add(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void saveLatestUploadedFiles(List<File> files) {
        Set<String> fileLocations = new HashSet<>();
        for (File file : files) {
            fileLocations.add(file.getAbsolutePath());
        }

        Prefs.putOrderedStringSet(LATEST_UPLOADED_FILES, fileLocations);
    }

    public static class LoginCredentials {
        public LoginCredentials(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        String field1;
        String field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    public static LoginCredentials getLoginCredentials() {

        try {
            return new LoginCredentials(Prefs.getString(LOGIN_CREDENTIALS_FIELD_1, ""),
                    Prefs.getString(LOGIN_CREDENTIALS_FIELD_2, ""));
        } catch (Exception e) {

        }
        return new LoginCredentials("", "");
    }

    public static void setLoginCredentials(String field1, String field2) {
        Prefs.putString(LOGIN_CREDENTIALS_FIELD_2, field1);
        Prefs.putString(LOGIN_CREDENTIALS_FIELD_1, field2);
    }


    public static String getSessionId() {
        return Prefs.getString(SESSION_ID, "");
    }

    public static void setSessionId(String sessionId) {
        Prefs.putString(SESSION_ID, sessionId);
    }

    public static void clearSessionId() {
        PrefUtil.setSessionId("");
    }


    public static boolean isNeedToEnableLocker() {
        return Prefs.getBoolean(NEED_TO_ENABLE_LOCKER, false);
    }

    public static void setIsNeedToEnableLocker(Boolean isNeedToEnableLocker) {
        Prefs.putBoolean(NEED_TO_ENABLE_LOCKER, isNeedToEnableLocker);
    }
}
