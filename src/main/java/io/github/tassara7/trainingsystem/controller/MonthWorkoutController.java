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

public class MonthWorkoutController implements UserAware, ActualDate, DaySelectionListener, SkinChangeListener, SkinnableScreen {

    //<editor-fold desc="FXML Fields">
    @FXML public Pane rootPane;
    @FXML private ImageView year;
    @FXML private ImageView config;
    @FXML private ImageView exit;
    @FXML private Label yearLabel;
    @FXML private Label monthLabel;
    @FXML private GridPane calendar;
    @FXML private Label todayLabel;
    @FXML private Label nextYearLabel;
    @FXML private Label previousYearLabel;
    @FXML private Label nextMonthLabel;
    @FXML private Label previousMonthLabel;
    @FXML private VBox infoPane;
    @FXML private PieChart pieChart;
    //</editor-fold>

    // --- Estado do Controller ---
    private User currentUser;
    private LocalDate currentDate;

    // --- Gerentes/Helpers ---
    private CalendarManager calendarManager;
    private WorkoutHoverDetailManager detailManager;


    public void initialize() {
        // 1. Cria as dependências e os gerentes
        CalendarAnimation calendarAnimation = new CalendarAnimation(calendar);
        this.calendarManager = new CalendarManager(calendar, this, calendarAnimation);

        // 2. Registra listeners
        SkinManager.registerListener(this);

        // 3. Configura a UI que não depende de dados do usuário
        setupStaticUIComponents();
        setChangeDateLabels();
    }

    @Override
    public void setUser(User user) {
        this.currentUser = user;

        // Cria o gerente que depende dos dados do usuário
        this.detailManager = new WorkoutHoverDetailManager(infoPane, currentUser);

        // 4. Inicia a primeira renderização da tela
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        updateView();
    }

    /**
     * Configura componentes e eventos que não dependem do 'currentUser'.
     */
    private void setupStaticUIComponents() {
        exit.setImage(new Image(getClass().getResource("/io/github/tassara7/trainingsystem/images/exit2.png").toExternalForm()));
        config.setImage(new Image(getClass().getResource("/io/github/tassara7/trainingsystem/images/config-icon.png").toExternalForm()));

        exit.setOnMouseClicked(e -> ((Stage) exit.getScene().getWindow()).close());
        config.setOnMouseClicked(e -> navigateTo(ScreenStorage.CONFIGSCREEN, true));
        year.setOnMouseClicked(e -> navigateTo(ScreenStorage.YEARWORKOUTSCREEN, false));

        todayLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")));
    }

    /**
     * Configura os eventos de clique para navegar entre meses e anos.
     */
    private void setChangeDateLabels() {
        previousYearLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.minusYears(1)));
        nextYearLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.plusYears(1)));
        previousMonthLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.minusMonths(1)));
        nextMonthLabel.setOnMouseClicked(event -> updateDateAndAnimate(currentDate.plusMonths(1)));
    }

    /**
     * Orquestra a atualização completa da view sem animação.
     */
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

        updatePieChart(monthWorkouts);
        calendarManager.displayMonth(currentDate, monthWorkouts);
    }

    /**
     * Orquestra a atualização da view com animação do calendário.
     */
    private void updateDateAndAnimate(LocalDate newDate) {
        currentDate = newDate;

        List<Workout> monthWorkouts = WorkoutFilter.filterByMonthAndYear(
                currentUser.getWorkouts(),
                currentDate.getMonthValue(),
                currentDate.getYear()
        );

        yearLabel.setText(String.valueOf(currentDate.getYear()));
        monthLabel.setText(String.valueOf(currentDate.getMonth()));

        updatePieChart(monthWorkouts);
        calendarManager.animateToMonth(currentDate, monthWorkouts);
    }

    private void updatePieChart(List<Workout> monthWorkouts) {
        PieChartView.updatePieChart(pieChart, monthWorkouts);
    }

    // --- Implementação das Interfaces ---

    @Override
    public void onDayClicked(LocalDate date) {
        WorkoutFilter.filterByDate(currentUser.getWorkouts(), date)
                .ifPresentOrElse(
                        workout -> { // Se existe um treino, vai para a tela de edição
                            try {
                                ScreenManager.getInstance().setScreen(ScreenStorage.NEWWORKOUTSCREEN, workout);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        },
                        () -> { // Se não existe, vai para a tela de criação
                            try {
                                ScreenManager.getInstance().setScreen(ScreenStorage.NEWWORKOUTSCREEN, date);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
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
        if (currentUser != null) {
            updateView();
        }
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.MONTH;
    }

    /**
     * Método auxiliar para navegação.
     */
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
}