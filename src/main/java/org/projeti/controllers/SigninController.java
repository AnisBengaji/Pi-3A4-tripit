package org.projeti.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SigninController {
    @FXML private ImageView backgroundImage;
    @FXML private ImageView googleIcon;
    @FXML private ImageView facebookIcon;

    public void initialize() {
        backgroundImage.setImage(new Image(getClass().getResourceAsStream("/images/background.png")));
        googleIcon.setImage(new Image(getClass().getResourceAsStream("/images/google.png")));
        facebookIcon.setImage(new Image(getClass().getResourceAsStream("/images/facebook.png")));
    }
}
