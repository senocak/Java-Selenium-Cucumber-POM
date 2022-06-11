package com.senocak.fvs.utility;

import com.senocak.fvs.config.ConfigFileReader;
import com.senocak.fvs.config.User;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class Constants {
    private static final ConfigFileReader configFileReader = ConfigFileReader.getInstance();

    public static final long EXPECTED_CONDITION_TIMEOUT = 30L;
    public static final long EXPECTED_CONDITION_POLLING_INTERVAL = 1L;
    public static final long DOCUMENT_READY_TIMEOUT = 60L;
    public static final long USER_WAIT_IN_MS = 1500L;
    public static final long WEBDRIVER_TIME_OUT_IN_SECONDS = 3;

    /**
     * Extract user from string
     * @param str string
     * @return user
     */
    public static User extractUserFromString(String str) {
        List<User> users = configFileReader.getUsers();
        if (str.startsWith("@user.")) {
            String extracted = str.substring(str.indexOf("[") + 1);
            extracted = extracted.substring(0, extracted.indexOf("]"));
            try {
                User user1 = users.get(Integer.parseInt(extracted) - 1);
                log.debug("Extracted user: {}", user1);
                return user1;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                log.error("User with index {} is not defined in config file", extracted);
                throw new RuntimeException("User with index " + extracted + " is not defined in config file");
            }
        }
        return null;
    }
}
