package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import io.github.tassara7.trainingsystem.service.WorkoutStatsCalculator;
import io.github.tassara7.trainingsystem.view.*;
import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinManager;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class YearWorkoutController implements ActualDate, SkinnableScreen, LanguageChangeListener, SkinChangeListener {

    @FXML
    private Label nextYearLabel, previousYearLabel;
    @FXML
    private GridPane yearGridPane;
    @FXML
    private GridPane statsGridPane;
    @FXML
    private Label yearLabel;
    @FXML
    private PieChart pieChart;
    @FXML
    private Pane rootPane;
    @FXML
    private ImageView exit, config;


    private LocalDate actualDate = LocalDate.now();


    public void initialize() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        pieChart.setAnimated(false);
        I18nManager.registerListener(this);

        SkinManager.registerListener(this);

        setupNavigationActions();
        updateYearView();


    }

    private void updateStatsDisplay(List<Workout> workouts) {
        statsGridPane.getChildren().clear();

        if (workouts.isEmpty()) {
            return;
        }

        WorkoutStatsCalculator stats = new WorkoutStatsCalculator(workouts);
        Map<BodyParts, Long> counts = stats.getCountsByBodyPart();

        BodyParts[] partsInOrder = {
                BodyParts.CHEST, BodyParts.BACK, BodyParts.SHOULDERS, BodyParts.BICEPS,
                BodyParts.TRICEPS, BodyParts.QUADRICEPS, BodyParts.HAMSTRINGS, BodyParts.CALVES
        };

        for (int i = 0; i < partsInOrder.length; i++) {
            BodyParts part = partsInOrder[i];

            int rowIndex = i % 4; // Linha de 0 a 3
            int colIndex = (i / 4) * 2; // Coluna 0 para os 4 primeiros, Coluna 2 para os 4 últimos

            String count = String.valueOf(counts.getOrDefault(part, 0L));
            String name = I18nManager.get(part) + ":";

            Label nameLabel = new Label(name);
            Label countLabel = new Label(count);

            // Adiciona os Labels ao GridPane na posição calculada
            statsGridPane.add(nameLabel, colIndex, rowIndex);
            statsGridPane.add(countLabel, colIndex + 1, rowIndex);
        }
    }

    private void updateYearView() {
        // 1. Limpa os painéis para remover os dados do ano anterior
        yearGridPane.getChildren().clear();
        statsGridPane.getChildren().clear();

        // 2. Pega o ano atual e atualiza a label principal
        int year = actualDate.getYear();
        yearLabel.setText(String.valueOf(year));

        // 3. Filtra a lista de treinos para pegar apenas os do ano selecionado
        List<Workout> yearWorkouts = WorkoutFilter.filterByYear(AppData.getInstance().getCurrentUser().getWorkouts(), year);

        // 4. Atualiza os componentes visuais com os novos dados
        PieChartView.updatePieChart(pieChart, yearWorkouts); // Atualiza o gráfico de pizza
        updateStatsDisplay(yearWorkouts); // Atualiza a tabela de estatísticas

        // 5. Popula o grid com os calendários dos 12 meses do novo ano
        int row = 0;
        int col = 0;
        for (int month = 1; month <= 12; month++) {
            List<Workout> monthWorkouts = WorkoutFilter.filterByMonth(yearWorkouts, month);

            // A fábrica cria o "card" de cada mês
            MonthCardFactory cardFactory = new MonthCardFactory(year, month,
                    monthWorkouts, yearWorkouts, this.pieChart, this::updateStatsDisplay);

            VBox monthContainer = cardFactory.getMonthCard();
            yearGridPane.add(monthContainer, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }
    public void onSkinChanged() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        SkinManager.applySkin(rootPane.getScene(), getScreenType());
        updateYearView();
    }



    private void setupNavigationActions() {
        nextYearLabel.setOnMouseClicked(e -> {
            actualDate = actualDate.plusYears(1);
            updateYearView();
        });

        previousYearLabel.setOnMouseClicked(e -> {
            actualDate = actualDate.minusYears(1);
            updateYearView();
        });

        exit.setOnMouseClicked(e -> ((Stage) exit.getScene().getWindow()).close());
        config.setOnMouseClicked(e -> navigateTo(ScreenStorage.CONFIGSCREEN, true));
    }

    @Override
    public void setDate(LocalDate date) {
        actualDate = date;
        updateYearView();
        yearLabel.setText(String.valueOf(date.getYear()));
        onLanguageChanged();
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.YEAR;
    }

    private void navigateTo(ScreenStorage screen, boolean newWindow) {
        try {
            if (newWindow) {
                ScreenManager.getInstance().openScreenInNewWindow(screen);
            } else {
                ScreenManager.getInstance().setScreen(screen);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onLanguageChanged() {
        // A forma mais simples de atualizar TUDO (título, meses e stats) é
        // chamar o método que redesenha a tela inteira.
        updateYearView();
    }

}