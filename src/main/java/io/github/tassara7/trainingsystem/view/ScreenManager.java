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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class ScreenManager {
    private static ScreenManager instance;
    private Stage stage;

    // Record auxiliar para retornar o Root e o Controller juntos
    private record LoadedScreen(Parent root, Object controller) {}

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * NOVO MÉTODO AUXILIAR: Centraliza a lógica de carregar FXML e injetar o usuário.
     */
    private LoadedScreen loadFxml(ScreenStorage screen) throws IOException {
        URL fxmlUrl = getClass().getResource(screen.fxmlPath());
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML não encontrado: " + screen.fxmlPath());
        }
        // Sempre carrega com o ResourceBundle para as traduções
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl, I18nManager.getBundle());
        Parent root = fxmlLoader.load();

        Object controller = fxmlLoader.getController();
        if (controller instanceof UserAware) {
            ((UserAware) controller).setUser(AppData.getInstance().getCurrentUser());
        }
        return new LoadedScreen(root, controller);
    }

    public void setScreen(ScreenStorage screen) throws IOException {
        LoadedScreen loaded = loadFxml(screen);
        newScene(screen, loaded.root(), loaded.controller());
    }

    public void setScreen(ScreenStorage screen, LocalDate date) throws IOException {
        LoadedScreen loaded = loadFxml(screen);
        if (loaded.controller() instanceof ActualDate) {
            ((ActualDate) loaded.controller()).setDate(date);
        }
        newScene(screen, loaded.root(), loaded.controller());
    }

    public void setScreen(ScreenStorage screen, Workout workout) throws IOException {
        LoadedScreen loaded = loadFxml(screen);
        if (loaded.controller() instanceof WorkoutCreator) {
            ((WorkoutCreator) loaded.controller()).setWorkout(workout);
        }
        newScene(screen, loaded.root(), loaded.controller());
    }

    public void openScreenInNewWindow(ScreenStorage screen) throws IOException {
        LoadedScreen loaded = loadFxml(screen);

        Stage newStage = new Stage();
        // CORREÇÃO: Usa o I18nManager para buscar a tradução da chave do título
        newStage.setTitle(I18nManager.getString(screen.titleKey()));

        Scene scene = new Scene(loaded.root());

        // CORREÇÃO: Aplica a skin na nova janela também
        if (loaded.controller() instanceof SkinnableScreen skinnable) {
            SkinManager.applySkin(scene, skinnable.getScreenType());
        }

        newStage.setScene(scene);
        newStage.show();
    }

    private void newScene(ScreenStorage screen, Parent root, Object controller) {
        Scene scene = new Scene(root, screen.width(), screen.height());

        if (controller instanceof SkinnableScreen skinnable) {
            SkinManager.applySkin(scene, skinnable.getScreenType());
        } else {
            throw new IllegalStateException("Controller não implementa SkinnableScreen: " + controller.getClass().getSimpleName());
        }

        // CORREÇÃO: Usa o I18nManager para buscar a tradução da chave do título
        stage.setTitle(I18nManager.getString(screen.titleKey()));

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}