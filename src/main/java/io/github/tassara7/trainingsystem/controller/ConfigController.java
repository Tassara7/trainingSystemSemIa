package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.view.SkinChangeListener;

import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinManager;
import io.github.tassara7.trainingsystem.view.Skins;
import io.github.tassara7.trainingsystem.view.WallpaperUtil;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;


public class ConfigController implements SkinChangeListener, SkinnableScreen {

    @FXML
    private Pane rootPane;
    @FXML
    private ComboBox<Skins> skinComboBox;

    public void initialize(){

        SkinManager.registerListener(this);
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());


        skinComboBox.getSelectionModel().select(AppData.getInstance().getCurrentUser().getSkin());
        skinComboBox.getItems().addAll(Skins.values());

        skinComboBox.valueProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null){
                AppData.getInstance().getCurrentUser().setSkin(newSkin);
                SkinManager.updateSkinForAll(newSkin);
            }
        });
    }

    @Override
    public void onSkinChanged() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.CONFIG;
    }
}
