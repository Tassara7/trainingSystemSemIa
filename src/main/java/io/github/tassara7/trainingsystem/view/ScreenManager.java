package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.controller.ActualDate;
import io.github.tassara7.trainingsystem.controller.UserAware;
import io.github.tassara7.trainingsystem.controller.WorkoutCreator;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.view.skin.SkinManager;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class ScreenManager {
    private static ScreenManager instance;
    private Stage stage;


    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setScreen(ScreenStorage screen) throws IOException {
        URL fxmlUrl = getClass().getResource(screen.fxmlPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML não encontrado: " + screen.fxmlPath());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();

        //Injeção Automática do User
        Object controller = fxmlLoader.getController();
        if (controller instanceof UserAware) {
            ((UserAware) controller).setUser(AppData.getInstance().getCurrentUser());
        }


        newScene(screen, root, controller);
    }

    public void setScreen(ScreenStorage screen, LocalDate date) throws IOException {
        URL fxmlUrl = getClass().getResource(screen.fxmlPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML não encontrado: " + screen.fxmlPath());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();



        // Injeção Automática do User
        Object controller = fxmlLoader.getController();
        if (controller instanceof UserAware) {
            ((UserAware) controller).setUser(AppData.getInstance().getCurrentUser());
        }

        // Injeção Automática da data
        if (controller instanceof ActualDate) {
            ((ActualDate) controller).setDate(date);
        }

        // Criação da cena
        newScene(screen, root, controller);
    }

    public void setScreen(ScreenStorage screen, Workout workout) throws IOException {
        URL fxmlUrl = getClass().getResource(screen.fxmlPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML não encontrado: " + screen.fxmlPath());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();

        //Injeção Automática do User
        Object controller = fxmlLoader.getController();
        if (controller instanceof UserAware) {
            ((UserAware) controller).setUser(AppData.getInstance().getCurrentUser());
        }

        if (controller instanceof WorkoutCreator) {
            ((WorkoutCreator) controller).setWorkout(workout);
        }

        newScene(screen, root, controller);
    }

    public void openScreenInNewWindow(ScreenStorage screen) throws IOException {
        URL fxmlUrl = getClass().getResource(screen.fxmlPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML não encontrado: " + screen.fxmlPath());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();

        // Injeção automática do User
        Object controller = fxmlLoader.getController();
        if (controller instanceof UserAware) {
            ((UserAware) controller).setUser(AppData.getInstance().getCurrentUser());
        }

        // Cria novo Stage (nova janela)
        Stage newStage = new Stage();
        newStage.setTitle(screen.title()); // se tiver método title()
        newStage.setScene(new Scene(root));
        newStage.show();
    }


    private void newScene(ScreenStorage screen, Parent root, Object controller) {
        Scene scene = new Scene(root, screen.width(), screen.height());

        if (controller instanceof SkinnableScreen skinnable) {
            SkinManager.applySkin(scene, skinnable.getScreenType());
        } else {
            throw new IllegalStateException("Controller não implementa SkinnableScreen: " + controller.getClass().getSimpleName());
        }

        stage.setTitle(screen.title());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
