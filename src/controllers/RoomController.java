package controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static controllers.UserController.*;

    /*
    This is a JavaFX Controller class that is used to handle the behavior of the chat room interface.
    The class implements the Initializable interface, which means it will have an initialize method
    that will be called when the GUI is initialized.
    The class contains various instance variables that are annotated with @FXML. These variables
    represent the different elements of the chat room interface, such as labels, buttons, and text fields.
     */

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
    public TextField fileChoosePath;
    @FXML
    public TextField proImage;
    @FXML
    public Circle showProPic;

    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    /*
    connectSocket method, which is used to connect to a server. It creates a new Socket
    instance that connects to localhost on port 8889. Once the socket is connected, it creates
    a BufferedReader and a PrintWriter to read from and write to the socket, respectively.
    The method also starts a new thread.
    In case of an error during the socket connection, an IOException is thrown and caught in
    the catch block. The error is printed to the console using e.printStackTrace().
     */

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8889);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    This code is the run() method of the RoomController class which is being executed as a
    new thread when the method connectSocket() is called. This method receives messages from
    the server and appends them to the msgRoom TextArea, updating the chat room in real-time.
    It uses a BufferedReader named reader to read the incoming messages from the server.
    The messages are split by the space character and the first token is taken as a command
    (indicating the origin of the message) and the rest of the tokens are combined to form the
    full message.
    The messages are checked to see if they are from the current user (indicated by the username
    in the command), if they are, they are ignored. If the message is "bye", it breaks the loop,
    closes the reader, writer and socket and terminates the thread.
    If there is any exception, it is caught and printed to the console using e.printStackTrace().
     */

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fulmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println(fulmsg);
                if (cmd.equalsIgnoreCase(UserController.username + ":")) {
                    continue;
                } else if (fulmsg.toString().equalsIgnoreCase("bye")) {
                    break;
                }
                msgRoom.appendText(msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    The handleProfileBtn method is an event handler for the profileBtn button.
    When the profile button is clicked, the method checks if the toggleProfile
    variable is false. If toggleProfile is false, the method animates the profile
    Pane to the front and hides the chat Pane by sending it to the back. The method
    sets the toggleProfile variable to true and the toggleChat variable to false.
    The text of the profile button is changed to "Back". The setProfile method is called
    to set the profile information of the current user.
    If toggleProfile is true, the method animates the chat Pane to the front and sets the
    toggleProfile and toggleChat variables to false. The text of the profile button is changed
    back to "Profile".
     */

    public void handleProfileBtn(ActionEvent event) {
        if (event.getSource().equals(profileBtn) && !toggleProfile) {
            new FadeIn(profile).play();
            profile.toFront();
            chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            profileBtn.setText("Back");
            setProfile();
        } else if (event.getSource().equals(profileBtn) && toggleProfile) {
            new FadeIn(chat).play();
            chat.toFront();
            toggleProfile = false;
            toggleChat = false;
            profileBtn.setText("Profile");
        }
    }

    /*
    The setProfile method sets the profile information of the current user by looping through
    a list of User objects and checking the name property of each user with the username from
    the UserController class. If the name matches, the profile information of the user is
    displayed on the labels.
     */

    public void setProfile() {
        for (User user : users) {
            if (UserController.username.equalsIgnoreCase(user.name)) {
                fullName.setText(user.fullName);
                fullName.setOpavity(1);
                email.setText(user.email);
                email.setOpacity(1);
                phoneNo.setText(user.phoneNo);
                gender.setText(user.gender);
            }
        }
    }

    /*
    The handleSendEvent method is triggered when the mouse is clicked on the send button.
    It calls the send method which retrieves the message from the msgField text field and
    sends it to the server by writing it to the PrintWriter object, writer. The NodeOrientation
    property of the msgRoom text area is set to NodeOrientation.LEFT_TO_RIGHT. Then the message
    is appended to the msgRoom text area.
     */

    public void handleSendEvent(MouseEvent event) {
        send();
        for (User user : users) {
            System.out.println(user.name);
        }
    }

    /*
    The send method checks if the message is equal to either "BYE" or "logout". If it is, the
    system exits with a status code of 0.
     */

    public void send() {
        String msg = msgField.getText();
        writer.println(UserController.username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("");
        if (msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    //Changing profile picture

    public boolean saveControl = false;

    /*
    The "chooseImageButton" method is used to handle the choose image button event. It opens a
    file chooser dialog and sets the selected file path to the "filePath" instance variable.
     */

    public void chooseImageButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        fileChoosePath.setText(filePath.getPath());
        saveControl = true;
    }

    /*
    The "sendMessageByKey" method is used to handle the key event when the enter key is pressed.
    It calls the "send" method to send the message to the server.
     */

    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    /*
    This code is for saving an image in a JavaFX application. It has a function named saveImage
    that reads the image file chosen by the user using the FileChooser and sets the image on an
    ImageView component and a Circle component.
    The image file path is stored in the filePath instance variable, which is set when the user
    chooses an image using the chooseImageButton method. The saveControl variable is used to ensure
    that the image is saved only when the user has selected an image. If the user has not selected
    an image, the code does not do anything.
    The code uses the ImageIO class from the Java Image I/O API to read the image file and the
    SwingFXUtils class from the JavaFX API to convert the image from a BufferedImage to a
    javafx.scene.image.Image.
     */

    public void saveImage() {
        if (saveControl) {
            try {
                BufferedImage bufferedImage = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                proImage.setImage(image);
                showProPic.setFill(new ImagePattern(image));
                saveControl = false;
                fileChoosePath.setText("");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /*
    the initialize method sets the profile picture and the username for the chat client.
    The showProPic is a Circle object, which is filled with an image pattern that depends
    on the gender of the user (either a male or female icon). The username is set using the
    clientName Label and the UserController.username property.
    Finally, it calls the connectSocket method, which is probably responsible for establishing
    a connection to the chat server.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProPic.setStroke(Color.valueOf("#90a4ae"));
        Image image;
        if (UserController.gender.equalsIgnoreCase("Male")) {
            image = new Image("icons/user.png", false);
        }else {
            image = new Image("icons/female.png", false);
            proImage.setImage(image);
            }
        showProPic.setFill(new ImagePattern(image));
        clientName.setText(UserController.username);
        connectSocket();
    }

}

