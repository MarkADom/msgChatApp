package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.entities.User;
import model.services.ClientHandler;
import model.services.Server;
import javafx.fxml.FXML;

import javax.swing.text.html.ImageView;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

    /*
    This code is a JavaFXML implementation of a UserController class. The class contains
    methods and fields that handle the registration and login functionality of a user.
    The class has several instance variables with @FXML annotations, which are JavaFX GUI
    elements such as Pane, Button, TextField, RadioButton, and Label objects.
     */


public class UserController {

    @FXML
    public Pane pnSignIn;
    @FXML
    public Pane pnSignUp;
    @FXML
    public Button btnSignUp;
    @FXML
    public Button getStarted;
    @FXML
    public ImageView btnBack;
    @FXML
    public TextField regName;
    @FXML
    public TextField regPass;
    @FXML
    public TextField regEmail;
    @FXML
    public TextField regFirstName;
    @FXML
    public TextField regPhoneNo;
    @FXML
    public RadioButton male;
    @FXML
    public RadioButton female;
    @FXML
    public Label controlRegLabel;
    @FXML
    public Label success;
    @FXML
    public Label goBack;
    @FXML
    public TextField userName;
    @FXML
    public TextField passWord;
    @FXML
    public Label loginNotifier;
    @FXML
    public Label nameExists;
    @FXML
    public Label checkEmail;

    public static String username, password, gender;
    public static ArrayList<User> loggedInUser = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();

    /*
    The registration method checks if all required fields in the registration
    form are filled in and if the entered username and email are unique. If the
    fields are filled in correctly, a new user object is created and added to the
    users ArrayList. If not, error labels are made visible to notify the user of
    what went wrong.
     */

    public void registration() {
        if (!regName.getText().equalsIgnoreCase("")
                && !regPass.getText().equalsIgnoreCase("")
                && !regEmail.getText().equalsIgnoreCase("")
                && !regFirstName.getText().equalsIgnoreCase("")
                && !regPhoneNo.getText().equalsIgnoreCase("")
                && (male.isSelected() || female.isSelected())) {
            if (checkUser(regName.getText())) {
                if (checkEmail(regEmail.getText())) {
                    User newUser = new User();
                    newUser.name = regName.getText();
                    newUser.password = regPass.getText();
                    newUser.email = regEmail.getText();
                    newUser.fullName = regFirstName.getText();
                    newUser.phoneNo = regPhoneNo.getText();
                    if (male.isSelected()) {
                        newUser.gender = "Male";
                    } else {
                        newUser.gender = "Female";
                    }
                    users.add(newUser);
                    goBack.setOpacity(1);
                    success.setOpacity(1);
                    makeDefault();
                    if (controlRegLabel.getOpacity() == 1) {
                        controlRegLabel.setOpacity(0);
                    }
                    if (nameExists.getOpacity() == 1) {
                        nameExists.setOpacity(0);
                    }
                } else {
                    checkEmail.setOpacity(1);
                    setOpacity(nameExists, goBack, controlRegLabel, success);
                }
            } else {
                nameExists.setOpacity(1);
                setOpacity(success, goBack, controlRegLabel, checkEmail);
            }
        } else {
            controlRegLabel.setOpacity(1);
            setOpacity(success, goBack, nameExists, checkEmail);
        }
    }

    /*
    This method sets the opacity of four labels to 0, which means they will become completely
    transparent. If any of the four labels have an opacity of 1 (fully visible), this method
    will set their opacity to 0, making them invisible. The method takes four Label objects as
    parameters and sets their opacity to 0.
     */

    private void setOpacity(Label a, Label b, Label c, Label d) {
        if (a.getOpacity() == 1 || b.getOpacity() == 1 || c.getOpacity() == 1 || d.getOpacity() == 1) {
            a.setOpacity(0);
            b.setOpacity(0);
            c.setOpacity(0);
            d.setOpacity(0);
        }
    }

    /*
    The setOpacity method is a helper method that sets the opacity of multiple Label objects to 0.
     */
    private void setOpacity(Label controlRegLabel, Label checkEmail, Label nameExists) {
        controlRegLabel.setOpacity(0);
        checkEmail.setOpacity(0);
        nameExists.setOpacity(0);
    }

    /*
    The checkUser and checkEmail methods are helper methods that check if
    a given username or email already exists in the users ArrayList, respectively.
    The makeDefault method sets all registration form fields to their default values
    and makes any visible error labels invisible.
     */

    private boolean checkUser(String username) {
        for (User user : users) {
            if (user.name.equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEmail(String email) {
        for (User user : users) {
            if (user.email.equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    /*
    This method sets default values for several text fields (regName, regPass, regEmail,
    regFirstName, regPhoneNo), selects the male radio button, and sets the opacity of three
    labels (controlRegLabel, checkEmail, nameExists) to 0. The purpose of this method is to
    clear the user input and return the form to its original state.
     */

    private void makeDefault() {
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
        regFirstName.setText("");
        regPhoneNo.setText("");
        male.setSelected(true);
        setOpacity(controlRegLabel, checkEmail, nameExists);
    }

    /*
    The login method checks if the entered username and password
    match any of the user objects in the users ArrayList. If a match is found,
    the user object is added to the loggedInUser ArrayList, and the login is
    considered successful.
     */

    public void login() {
        username = userName.getText();
        password = passWord.getText();
        boolean login = false;
        for (User x : users) {
            if (x.name.equalsIgnoreCase(username) && x.password.equalsIgnoreCase(password)) {
                login = true;
                loggedInUser.add(x);
                System.out.println(x.name);
                gender = x.gender;
                break;
            }
        }
        if (login) {
            changeWindow();
        } else {
            loginNotifier.setOpacity(1);
        }
    }

    /*
    This Java code changes the current window of the application to another window defined by the FXML file "room.fxml".
    The first line gets the current stage from the scene associated with the userName text field.
    Then, a new root node is created by loading the FXML file "room.fxml" using the FXMLLoader.load method.
    The scene associated with the stage is then changed to the newly created scene, and its dimensions
    are set to 330 by 560. The title of the stage is set to the value of the username variable.
    The setOnCloseRequest method sets an event handler to close the system when the window is closed.
    The setResizable method sets the resizability of the window to false, and finally, the show method displays the window.
    The code has an exception handler to print the stack trace of any exceptions thrown by the FXMLLoader.load method.
     */

    public void changeWindow() {
        try {
            Stage stage = (Stage) userName.getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("room.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     The handleButtonAction method handles the action events for two buttons,
     btnSignUp and getStarted. If the btnSignUp button is clicked, the pnSignUp
     panel will be brought to the front and a fade-in animation will be played on it.
     If the getStarted button is clicked, the pnSignIn panel will be brought to the front
     and a fade-in animation will be played. The opacity of the loginNotifier object will
     be set to 0 and the text of the userName and passWord fields will be cleared.
     */

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(btnSignUp)) {
            new FadeIn(pnSignUp).play();
            pnSignUp.toFront();
        }
        if (event.getSource().equals(getStarted)) {
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        loginNotifier.setOpacity(0);
        userName.setText("");
        passWord.setText("");
    }

    /*
    The handleMouseEvent method handles the mouse events for a single button,
    btnBack. If the btnBack button is clicked, the pnSignIn panel will be brought
    to the front and a fade-in animation will be played on it. The text of the regName,
    regPass, and regEmail fields will be cleared
     */

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource() == btnBack) {
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
    }
}


