// Crie este novo arquivo em: src/main/java/io/github/tassara7/trainingsystem/view/WorkoutPopupManager.java
package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.Workout;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.stage.Popup;
import javafx.stage.Window;

/**
 * Uma classe especialista em criar, posicionar e gerenciar
 * o Popup que mostra os detalhes de um treino.
 */
public class WorkoutPopupManager {

    private final Popup popup;
    private final Node ownerNode; // O nó de referência para o posicionamento (nosso calendário)

    /**
     * O construtor prepara o Popup e guarda a referência do nó que o controlará.
     * @param ownerNode O componente da tela que servirá de referência para a posição do Popup.
     */
    public WorkoutPopupManager(Node ownerNode) {
        this.ownerNode = ownerNode;
        this.popup = new Popup();
        this.popup.setAutoHide(true);
    }

    /**
     * Exibe o Popup com os detalhes de um treino específico em uma posição fixa.
     * @param workout O treino a ser mostrado.
     */
    public void show(Workout workout) {
        // 1. Cria o conteúdo visual (a VBox com as informações)
        WorkoutInfo workoutInfo = new WorkoutInfo(workout);
        workoutInfo.showInfo();
        this.popup.getContent().setAll(workoutInfo);

        // 2. Pega a janela principal
        Window ownerWindow = ownerNode.getScene().getWindow();

        // 3. Calcula a posição fixa baseada no nó de referência (o calendário)
        Bounds ownerBounds = ownerNode.localToScreen(ownerNode.getBoundsInLocal());
        double x = ownerBounds.getMaxX() - 30;
        double y = ownerBounds.getMinY() + 15;

        // 4. Mostra o popup
        this.popup.show(ownerWindow, x, y);
    }

    /**
     * Esconde o Popup se ele estiver visível.
     */
    public void hide() {
        if (this.popup.isShowing()) {
            this.popup.hide();
        }
    }
}