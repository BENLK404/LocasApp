package controllers;

import database.Connectivity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    @FXML
    private Button btn_connexion;

    @FXML
    private AnchorPane connexion_window;

    @FXML
    private AnchorPane si_formConnexion;

    @FXML
    private AnchorPane si_formGarde;

    @FXML
    private PasswordField si_motDePasse;

    @FXML
    private Hyperlink si_motDePasseOublie;

    @FXML
    private TextField si_nomUtilisateur;

    private Parent fxml;
    private Alert alert;


    @FXML
    void OpenHome() throws SQLException {
        String sql = "SELECT * FROM Admin WHERE NomAdmin = ? and MotDePasse = ?";

        try (Connection connect = Connectivity.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, si_nomUtilisateur.getText());
            prepare.setString(2, si_motDePasse.getText());

            try (ResultSet result = prepare.executeQuery()) {
                if (si_nomUtilisateur.getText().isEmpty() || si_motDePasse.getText().isEmpty()) {
                    showError("Remplissez toutes les colonnes");
                } else {
                    if (result.next()) {
                        showInfo("Connection réussie");
                        connexion_window.getScene().getWindow().hide();
                        Stage home = new Stage();
                        try {
                            fxml = FXMLLoader.load(getClass().getResource("/interfaces/Home3.fxml"));
                            Scene scene = new Scene(fxml);
                            home.setScene(scene);
                            Image icon = new Image(getClass().getResourceAsStream("/icons/iconLocas.png"));
                            home.getIcons().add(icon);
                            home.setTitle("Accueil");
                            home.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                    showError("Mot de passe ou nom d'utilisateur incorrect");
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @FXML
    void OpenForgotPass() {
        connexion_window.getScene().getWindow().hide();
        Stage stage = new Stage();
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/interfaces/Forgot_password.fxml"));
            Scene scene = new Scene(fxml);
            stage.setScene(scene);
            Image icon = new Image(getClass().getResourceAsStream("/icons/iconLocas.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void showError(String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message d'erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message d'information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


















    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
