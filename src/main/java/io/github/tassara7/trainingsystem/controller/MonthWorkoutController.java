package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import io.github.tassara7.trainingsystem.view.*;
import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinManager;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// Não precisa de mudanças na declaração da classe
public class MonthWorkoutController implements UserAware, ActualDate, DaySelectionListener, SkinChangeListener, SkinnableScreen {

    // Seus campos @FXML e outras variáveis de instância permanecem os mesmos
    private User currentUser;
    private LocalDate currentDate;
    private CalendarAnimation calendarAnimation;
    private WorkoutHoverDetailManager detailManager;

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

    // **MUDANÇA PRINCIPAL AQUI**
    // initialize() agora só configura o que não depende de `currentUser`
    public void initialize() {
        this.calendarAnimation = new CalendarAnimation(calendar);
        SkinManager.registerListener(this);

        // Configura ações que independem do usuário estar carregado
        setupStaticUIComponents();
        setChangeDateLabels();
    }

    // **NOVO MÉTODO**
    // O método setUser agora é responsável por iniciar a visualização
    @Override
    public void setUser(User user) {
        this.currentUser = user;
        this.detailManager = new WorkoutHoverDetailManager(infoPane, currentUser);

        // Agora que temos o usuário, podemos popular a tela com segurança
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        updateView();
    }

    // Este método foi renomeado para refletir que só configura partes estáticas
    private void setupStaticUIComponents() {
        infoPane.setVisible(false);

        String todayString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
        todayLabel.setText(todayString);

        exit.setImage(new Image(getClass().getResource("/io/github/tassara7/trainingsystem/images/exit2.png").toExternalForm()));
        config.setImage(new Image(getClass().getResource("/io/github/tassara7/trainingsystem/images/config-icon.png").toExternalForm()));

        exit.setOnMouseClicked(e -> ((Stage) exit.getScene().getWindow()).close());
        config.setOnMouseClicked(e -> navigateTo(ScreenStorage.CONFIGSCREEN, true));
        year.setOnMouseClicked(e -> navigateTo(ScreenStorage.YEARWORKOUTSCREEN, false));
    }

    private void updateView() {
        if (currentDate == null) {
            currentDate = LocalDate.now();
        }

        List<Workout> monthWorkouts = WorkoutFilter.filterByMonthAndYear(
                currentUser.getWorkouts(),
                currentDate.getMonthValue(),
                currentDate.getYear()
        );


        yearLabel.setText(String.valueOf(currentDate.getYear()));
        monthLabel.setText(String.valueOf(currentDate.getMonth()));
        updateCalendar(monthWorkouts);
        updatePieChart(monthWorkouts);
    }

    private void updateCalendar(List<Workout> monthWorkouts) {
        Set<LocalDate> workoutDays = monthWorkouts.stream()
                .map(Workout::getDate)
                .collect(Collectors.toSet());

        CalendarView calendarView = new CalendarView(currentDate.getYear(), currentDate.getMonthValue(), workoutDays, 30, 20, 10);
        calendarView.setDaySelectionListener(this);
        calendar.getChildren().clear();
        calendar.add(calendarView, 0, 0);
    }

    // O resto dos seus métodos (onDayClicked, onSkinChanged, etc.) pode permanecer igual.
    // Apenas os métodos que reescrevi acima precisam ser atualizados.

    private void navigateTo(ScreenStorage screen, boolean newWindow) {
        try {
            if (newWindow) {
                ScreenManager.getInstance().openScreenInNewWindow(screen);
            } else {
                ScreenManager.getInstance().setScreen(screen, currentDate);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateCalendarWithAnimation() {
        // Filtra a lista UMA VEZ
        List<Workout> monthWorkouts = WorkoutFilter.filterByMonthAndYear(
                currentUser.getWorkouts(),
                currentDate.getMonthValue(),
                currentDate.getYear()
        );

        Set<LocalDate> workoutDays = monthWorkouts.stream()
                .map(Workout::getDate)
                .collect(Collectors.toSet());

        CalendarView newCalendarView = new CalendarView(currentDate.getYear(), currentDate.getMonthValue(), workoutDays, 30, 20, 10);
        newCalendarView.setDaySelectionListener(this);

        calendarAnimation.animateCalendarTransition(newCalendarView);

        yearLabel.setText(String.valueOf(currentDate.getYear()));
        monthLabel.setText(String.valueOf(currentDate.getMonth()));

        // Usa a lista já filtrada
        updatePieChart(monthWorkouts);
    }

    private void updatePieChart(List<Workout> monthWorkouts) {
        PieChartView.updatePieChart(pieChart, monthWorkouts);
    }

    @Override
    public void onDayClicked(LocalDate date) {
        try {
            WorkoutFilter.filterByDate(currentUser.getWorkouts(), date)
                    .ifPresentOrElse(
                            workout -> navigateToWorkoutScreen(workout),
                            () -> navigateToWorkoutScreen(date)
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToWorkoutScreen(Workout workout) {
        try {
            ScreenManager.getInstance().setScreen(ScreenStorage.NEWWORKOUTSCREEN, workout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToWorkoutScreen(LocalDate date) {
        try {
            ScreenManager.getInstance().setScreen(ScreenStorage.NEWWORKOUTSCREEN, date);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDaySelected(LocalDate date) {
       detailManager.handleDaySelected(date);
    }

    @Override
    public void onDayDeselected(LocalDate date) {
        detailManager.handleDayDeselected();
    }



    @Override
    public void onSkinChanged() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        SkinManager.applySkin(rootPane.getScene(), getScreenType());
        updateView();
    }

    @Override
    public void setDate(LocalDate date) {
        this.currentDate = date;
        // Se a view já estiver carregada, atualize-a
        if (currentUser != null) {
            updateView();
        }
    }

    private void setChangeDateLabels() {
        previousYearLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.minusYears(1)));
        nextYearLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.plusYears(1)));
        previousMonthLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.minusMonths(1)));
        nextMonthLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.plusMonths(1)));
    }

    private void updateDateAndAnimate(LocalDate newDate) {
        currentDate = newDate;
        updateCalendarWithAnimation();
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.MONTH;
    }
}