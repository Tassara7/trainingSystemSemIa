module io.github.tassara7.trainingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens io.github.tassara7.trainingsystem to javafx.fxml; // Pacote principal
    opens io.github.tassara7.trainingsystem.controller to javafx.fxml; // Novo: pacote do controlador
    exports io.github.tassara7.trainingsystem;
}