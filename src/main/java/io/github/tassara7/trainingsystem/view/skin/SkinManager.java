package io.github.tassara7.trainingsystem.view.skin;

import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.view.SkinChangeListener;
import io.github.tassara7.trainingsystem.view.Skins;
import javafx.scene.Scene;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SkinManager {
    

    private static class SceneEntry {
        Scene scene;
        ScreenType type;

        SceneEntry(Scene scene, ScreenType type) {
            this.scene = scene;
            this.type = type;
        }
    }

    public static final List<SceneEntry> scenes = new ArrayList<>();
    private static final List<SkinChangeListener> listeners = new ArrayList<>();

    public static void applySkin(Scene scene, ScreenType screenType) {
        Skins skin = AppData.getInstance().getCurrentUser().getSkin();
        scene.getStylesheets().clear();

        String stylesheetPath = skin.getCssFor(screenType);

        URL originalUrl = Skins.class.getResource(stylesheetPath);
        if (originalUrl == null) {
            throw new IllegalArgumentException("CSS não encontrado: " + stylesheetPath);
        }

        try {
            InputStream in = originalUrl.openStream();
            File tempFile = File.createTempFile("skin-", ".css");
            tempFile.deleteOnExit();
            try (OutputStream out = new FileOutputStream(tempFile)) {
                in.transferTo(out);
            }

            String tempCssUrl = tempFile.toURI().toURL().toExternalForm();
            scene.getStylesheets().add(tempCssUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (scenes.stream().noneMatch(entry -> entry.scene == scene)) {
            scenes.add(new SceneEntry(scene, screenType));
        }
    }

    public static void updateSkinForAll(Skins newSkin) {
        for (SceneEntry entry : scenes) {
            applySkin(entry.scene, entry.type);
        }
        notifyListeners();
    }

    public static void registerListener(SkinChangeListener listener) {
        listeners.add(listener);
    }

    public static void notifyListeners() {
        for (SkinChangeListener listener : listeners) {
            listener.onSkinChanged();
        }
    }
}
