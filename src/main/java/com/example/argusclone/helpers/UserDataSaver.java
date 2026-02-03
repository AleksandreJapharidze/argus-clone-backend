package com.example.argusclone.helpers;

import com.example.argusclone.dtos.user.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDataSaver {
    private static final Logger log = LoggerFactory.getLogger(UserDataSaver.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String DATA_DIRECTORY = "real_user_data";
    private static final String USER_DATA_FILE = "users.json";

    private static File getSaveFile() {
        File dataDirectory = new File(DATA_DIRECTORY);
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }
        return new File(dataDirectory, USER_DATA_FILE);
    }

    public static void saveUser(User user) {
        File saveFile = getSaveFile();

        try {
            List<User> users;
            if (saveFile.exists()) {
                users = mapper.readValue(saveFile, new TypeReference<>() {});
            } else {
                users = new ArrayList<>();
            }

            users.add(user);
            mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, users);
            log.info("Saved user: {}", user);
        } catch (IOException e) {
            log.error("Failed to save user: {}", user);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public static void deleteUser(String username) {
        File saveFile = getSaveFile();

        try {
            if (!saveFile.exists() || saveFile.length() == 0) {
                log.warn("JSON file is empty or missing");
                return;
            }

            List<User> users = mapper.readValue(saveFile, new TypeReference<List<User>>() {});

            boolean removed = users.removeIf(user -> username.equals(user.username()));

            if (removed) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, users);
                log.info("Deleted user: {}", username);
            } else {
                log.warn("User {} not found", username);
            }

        } catch (IOException e) {
            log.error("Failed to delete user: {}", username, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
