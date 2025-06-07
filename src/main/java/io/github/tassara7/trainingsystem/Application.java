package io.github.tassara7.trainingsystem;

import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.persistence.DataManager;
import io.github.tassara7.trainingsystem.persistence.LoadWorkoutTask;
import io.github.tassara7.trainingsystem.view.ScreenManager;
import io.github.tassara7.trainingsystem.view.ScreenStorage;
import io.github.tassara7.trainingsystem.view.Skins;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

//        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        LocalDate today = LocalDate.now();
        // Configura o ScreenManager com o stage da aplicação
        ScreenManager screenManager = ScreenManager.getInstance();
        screenManager.setStage(stage);

        // Instancia a Task de carregamento dos treinos (Workouts)
        LoadWorkoutTask loadTask = new LoadWorkoutTask();

        // Define o que fazer quando o carregamento for bem-sucedido
        loadTask.setOnSucceeded(event -> {
            User user = loadTask.getValue();
            if(user.getSkin() == null){
            user.setSkin(Skins.DEFAULT);
            }
            AppData.getInstance().setCurrentUser(user);
            // Escolhe a tela de acordo com a lista de treinos
            ScreenStorage screen = user.getName().isEmpty() ? ScreenStorage.FIRSTSCREEN : ScreenStorage.MONTHWORKOUTSCREEN;
            try {
                screenManager.setScreen(screen, today);
            } catch (IOException e) {
                // Em vez de lançar uma RuntimeException, aqui você pode exibir um alerta ou registrar o erro
                e.printStackTrace();
            }
        });

        // Define o que fazer se ocorrer algum erro durante o carregamento
        loadTask.setOnFailed(event -> {
            Throwable error = loadTask.getException();
            // Registre o erro ou notifique o usuário de forma apropriada
            error.printStackTrace();
        });

        // Inicia a Task em uma nova thread para não bloquear a UI
        new Thread(loadTask).start();
    }

    @Override
    public void stop() throws Exception {
        User currentUser = AppData.getInstance().getCurrentUser();
        System.out.println("Salvo com sucesso!");
        try {
            DataManager.save(currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}