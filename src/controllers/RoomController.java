package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

import static controllers.UserController.loggedInUser;
import static controllers.UserController.users;

public class RoomController extends Thread implements Initializable {

    @FXML
    public Label clientName;
    @FXML
    public Button chatBtn;
    @FXML
    public Pane chat;
    @FXML
    public TextField msgField;
    @FXML
    public TextArea msgRoom;
    @FXML
    public Label online;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public Label phoneNo;
    @FXML
    public Label gender;
    @FXML
    public Pane profile;
    @FXML
    public Button profileBtn;
    @FXML
    public TextField proImage;
    @FXML
    public Circle showProPic;

    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;














    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
