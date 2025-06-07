package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import io.github.tassara7.trainingsystem.view.*;

import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinManager;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MonthWorkoutController implements UserAware, ActualDate, DaySelectionListener, SkinChangeListener, SkinnableScreen {


    private User currentUser;
    private LocalDate currentDate;
    private ParallelTransition currentTransition;

    @FXML
    public Pane rootPane;
    @FXML
    private ImageView year;
    @FXML
    private ImageView config;
    @FXML
    private ImageView exit;
    @FXML
    private Label yearLabel;
    @FXML
    private Label monthLabel;
    @FXML
    private GridPane calendar;
    @FXML
    private Label todayLabel;
    @FXML
    private Label nextYearLabel;
    @FXML
    private Label previousYearLabel;
    @FXML
    private Label nextMonthLabel;
    @FXML
    private Label previousMonthLabel;
    @FXML
    private VBox infoPane;
    @FXML
    private PieChart pieChart;

    public void initialize() {

        SkinManager.registerListener(this);
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());

        if (currentDate == null) {
            currentDate = LocalDate.now();
        }
        yearLabel.setText(String.valueOf(currentDate.getYear()));
        monthLabel.setText(String.valueOf(currentDate.getMonth()));

        infoPane.setVisible(false);
        calendar.setOnMouseExited(event -> infoPane.setVisible(false));
        LocalDate today = LocalDate.now();
        String todayString = today.format(DateTimeFormatter.ofPattern("dd/MM"));
        todayLabel.setText(todayString);

        Image exitIcon = new Image(getClass().getResource("/io/github/tassara7/trainingsystem/images/exit2.png").toExternalForm());
        Image configIcon = new Image(getClass().getResource("/io/github/tassara7/trainingsystem/images/config-icon.png").toExternalForm());
        exit.setImage(exitIcon);
        exit.setOnMouseClicked(e -> {
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });

        config.setImage(configIcon);
        config.setOnMouseClicked(e -> {
            try {
                ScreenManager.getInstance().openScreenInNewWindow(ScreenStorage.CONFIGSCREEN);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        year.setOnMouseClicked(e -> {
            try {
                ScreenManager.getInstance().setScreen(ScreenStorage.YEARWORKOUTSCREEN, currentDate);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        setChangeDateLabels();

    }

    @Override
    public void onSkinChanged() {
        updateCalendar();
        updatePieChart();




        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        SkinManager.applySkin(rootPane.getScene(), getScreenType());
        URL url = getClass().getResource("/io/github/tassara7/trainingsystem/skins/dark/MonthWorkoutScreen.css");
        System.out.println("URL do CSS DARK: " + url);

    }


    private void updateCalendarWithAnimation() {
        // Se já houver uma animação em andamento, cancele e remova nós extras
        if (currentTransition != null) {
            currentTransition.stop();
            // Remove todos os nós extras, mantendo somente o calendário atualmente visível
            while (calendar.getChildren().size() > 1) {
                calendar.getChildren().remove(0);
            }
            currentTransition = null;
        }

        // Filtra os dias de treino do mês atual
        Set<LocalDate> workoutDays = currentUser.getWorkouts().stream()
                .map(Workout::getDate)
                .filter(date -> date.getYear() == currentDate.getYear()
                        && date.getMonthValue() == currentDate.getMonthValue())
                .collect(Collectors.toSet());

        // Atualiza os labels de ano e mês
        yearLabel.setText(String.valueOf(currentDate.getYear()));
        monthLabel.setText(String.valueOf(currentDate.getMonth()));

        // Cria a nova view do calendário para o mês atual
        CalendarView newCalendarView = new CalendarView(currentDate.getYear(), currentDate.getMonthValue(), workoutDays, 30, 20, 10);
        newCalendarView.setDaySelectionListener(this);

        // Obtém a largura do container do calendário (se não estiver definida, usamos um fallback)
        double width = calendar.getWidth();
        if (width <= 0) {
            width = 600; // valor padrão caso a largura ainda não esteja definida
        }

        // Posiciona o novo calendário fora da tela à direita
        newCalendarView.setTranslateX(width);

        // Adiciona o novo calendário à GridPane sem remover o antigo
        calendar.add(newCalendarView, 0, 0);

        // Se já existir um calendário antigo na GridPane, anima sua saída
        if (calendar.getChildren().size() > 1) {
            // O nó antigo é o primeiro filho do container
            Node oldCalendarView = calendar.getChildren().get(0);

            // Cria uma transição para deslizar o calendário antigo para a esquerda
            TranslateTransition ttOld = new TranslateTransition(Duration.millis(250), oldCalendarView);
            ttOld.setToX(-width);

            // Configura o binding para ajustar a opacidade do calendário antigo
            // Ele começará a desaparecer após mover 30% da largura e ficará totalmente invisível com 70%
            double disappearThreshold = -width * 0.3;
            double finalWidth = width;
            oldCalendarView.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
                double x = oldCalendarView.getTranslateX();
                if (x >= disappearThreshold) {
                    return 1.0;
                } else if (x <= -finalWidth * 0.7) {
                    return 0.0;
                } else {
                    // Interpolação linear para um desaparecimento mais rápido
                    return (x + finalWidth * 0.7) / (disappearThreshold + finalWidth * 0.7);
                }
            }, oldCalendarView.translateXProperty()));

            // Cria uma transição para deslizar o novo calendário para a posição original (0)
            TranslateTransition ttNew = new TranslateTransition(Duration.millis(250), newCalendarView);
            ttNew.setToX(0);

            // Junta as duas transições para executá-las em paralelo
            ParallelTransition pt = new ParallelTransition(ttOld, ttNew);
            pt.setOnFinished(e -> {
                // Remove o calendário antigo após a animação
                calendar.getChildren().remove(oldCalendarView);
                currentTransition = null;
            });
            currentTransition = pt;
            pt.play();
        } else {
            // Caso não haja calendário antigo, apenas anima o novo calendário entrando
            TranslateTransition ttNew = new TranslateTransition(Duration.millis(500), newCalendarView);
            ttNew.setToX(0);
            ttNew.play();
        }
    }

    private void updateCalendar() {

        if (currentDate == null) {
            currentDate = LocalDate.now();
        }

        Set<LocalDate> workoutDays = currentUser.getWorkouts().stream()
                .map(Workout::getDate)
                .filter(date -> date.getYear() == currentDate.getYear()
                        && date.getMonthValue() == currentDate.getMonthValue())
                .collect(Collectors.toSet());

        yearLabel.setText(String.valueOf(currentDate.getYear()));
        monthLabel.setText(String.valueOf(currentDate.getMonth()));

        // Cria ou atualiza a view do calendário.
        // Supondo que CalendarView seja um Node (um componente visual) que pode ser adicionado ao GridPane 'calendar'
        CalendarView calendarView = new CalendarView(currentDate.getYear(), currentDate.getMonthValue(), workoutDays, 30, 20, 10);

        calendarView.setDaySelectionListener(this);
        // Se o seu GridPane 'calendar' já tiver outros componentes, você pode querer limpar antes de adicionar o novo
        calendar.getChildren().clear();
        // Adiciona o CalendarView na posição desejada do GridPane (exemplo na coluna 0, linha 0)
        calendar.add(calendarView, 0, 0);


    }

    private void updatePieChart() {
        PieChartView.updatePieChart(pieChart,
                WorkoutFilter.filterByMonthAndYear(
                        currentUser.getWorkouts(),
                        currentDate.getMonthValue(),
                        currentDate.getYear()));
    }


    @Override
    public void onDayClicked(LocalDate date) throws IOException {
        Optional<Workout> workoutOpt = currentUser.getWorkouts().stream()
                .filter(workout -> workout.getDate().equals(date))
                .findFirst();

        Workout clickedWorkout;
        System.out.println("Clicado");
        if (workoutOpt.isPresent()) {
            clickedWorkout = workoutOpt.get();
            ScreenManager.getInstance().setScreen(ScreenStorage.NEWWORKOUTSCREEN, clickedWorkout);
        } else {
            ScreenManager.getInstance().setScreen(ScreenStorage.NEWWORKOUTSCREEN, date);
        }
    }

    @Override
    public void onDaySelected(LocalDate date) {
        Optional<Workout> workoutOpt = currentUser.getWorkouts().stream()
                .filter(workout -> workout.getDate().equals(date))
                .findFirst();

        if (workoutOpt.isPresent()) {
            Workout selectedworkout = workoutOpt.get();
            openWorkoutDetails(selectedworkout);
        }

    }

    @Override
    public void onDayDeselected(LocalDate date) {
        // Oculta o painel de detalhes quando o mouse sai do dia
        infoPane.setVisible(false);
    }

    // Método chamado pelo Listener
    private void openWorkoutDetails(Workout workout) {
        WorkoutInfo workoutInfo = new WorkoutInfo(workout);
        workoutInfo.showInfo();

        infoPane.getChildren().clear();
        infoPane.getChildren().add(workoutInfo);
        infoPane.setVisible(true);
    }


    @Override
    public void setDate(LocalDate date) {
        currentDate = date;
        updateCalendar(); // Atualiza o calendário com a data injetada
        PieChartView.updatePieChart(pieChart,
                WorkoutFilter.filterByMonthAndYear(
                        currentUser.getWorkouts(),
                        date.getMonthValue(),
                        date.getYear()));

    }


    @Override
    public void setUser(User user) {
        currentUser = user;
    }

    public void setChangeDateLabels() {
        nextYearLabel.setOnMouseClicked(event -> {
            currentDate = currentDate.plusYears(1);
            updateCalendarWithAnimation();
            updatePieChart();

        });
        previousYearLabel.setOnMouseClicked(event -> {
            currentDate = currentDate.minusYears(1);
            updateCalendarWithAnimation();
            updatePieChart();
        });

        nextMonthLabel.setOnMouseClicked(event -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendarWithAnimation();
            updatePieChart();
        });
        previousMonthLabel.setOnMouseClicked(event -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendarWithAnimation();
            updatePieChart();
        });
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.MONTH;
    }
}
