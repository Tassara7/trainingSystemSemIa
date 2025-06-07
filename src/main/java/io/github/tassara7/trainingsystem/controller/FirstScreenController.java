package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.view.ScreenManager;
import io.github.tassara7.trainingsystem.view.ScreenStorage;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstScreenController implements UserAware {

    @FXML
    private Pane rootPane;
    @FXML
    private TextField nameTextField;
    private User currentUser;


    public void initialize() {
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                {
                    if (!nameTextField.getText().isEmpty())
                        try {
                            currentUser.setName(nameTextField.getText());
                            ScreenManager.getInstance().setScreen(ScreenStorage.NEWWORKOUTSCREEN);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                }
            }
        });

    }

    @Override
    public void setUser(User user) {
     currentUser = user;
    }
}
