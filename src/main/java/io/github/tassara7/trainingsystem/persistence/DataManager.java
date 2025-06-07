package io.github.tassara7.trainingsystem.persistence;

import io.github.tassara7.trainingsystem.model.User;
import java.io.*;

public class DataManager {
    private static final String DATA_FILE = "workout.dat";

    public static void save(User user) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(user);
        }
    }



    public static User load() throws IOException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new User("");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (User) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
