package controllers;

import database.Connectivity;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Forgot_passwordController implements Initializable {

    @FXML
    private Button btn_Signin;

    @FXML
    private Button btn_connexion;

    @FXML
    private AnchorPane forgot_window;

    @FXML
    private TextField forgot_answer;

    @FXML
    private AnchorPane forgot_form;

    @FXML
    private AnchorPane forgot_garde;

    @FXML
    private PasswordField forgot_password;

    @FXML
    private PasswordField forgot_password_confirm;

    @FXML
    private ComboBox<String> forgot_question;

    @FXML
    private Button forgot_valide_btn;

    @FXML
    private AnchorPane si_formConnexion;

    @FXML
    private AnchorPane si_formGarde;

    @FXML
    private AnchorPane si_formGarde1;

    @FXML
    private PasswordField si_motDePasse;

    @FXML
    private Hyperlink si_motDePasseOublie;

    @FXML
    private TextField si_nomUtilisateur;
    Connection cnx;
    public PreparedStatement st;
    public ResultSet result;

    @FXML
    void ForgotValide() {
        String sql = "UPDATE admin SET MOTDEPASSE =? WHERE couleur =?";



    }

    @FXML
    void SignIn() {
        forgot_window.getScene().getWindow().hide();
        Stage stage = new Stage();
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/interfaces/SignIn.fxml"));
            Scene scene = new Scene(fxml);
            stage.setScene(scene);
            Image icon = new Image(getClass().getResourceAsStream("/icons/iconLocas.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Connexion");
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    @FXML
    void ShowCouleur() {
        String sql = "SELECT couleur from admin";
        List<String> couleurs = new ArrayList<>();
        try {
            st = cnx.prepareStatement(sql);
            result = st.executeQuery();
            while (result.next()){
                couleurs.add(result.getString("couleur"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        forgot_question.setItems(FXCollections.observableArrayList(couleurs));



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cnx = Connectivity.connectDb();
        ShowCouleur();
    }
}
