package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.persistence.AppData;

public class WallpaperUtil {
    public static String buildWallpaperStyle() {
        Skins skin = AppData.getInstance().getCurrentUser().getSkin();
        String imageUrl = WallpaperUtil.class.getResource(skin.getWallpaperPath()).toExternalForm();

        return "-fx-background-image: url('" + imageUrl + "');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center center;" +
                "-fx-background-repeat: no-repeat;";
    }
}
