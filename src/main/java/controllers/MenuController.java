package controllers;

import database.Connectivity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Button search_btn;
    @FXML
    private TextField auteur_cassette;

    @FXML
    private TextField date_achat_cassette;

    @FXML
    private TextField dure_cassette;

    @FXML
    private TextField libelle_categorie;

    @FXML
    private TextField num_cassette;

    @FXML
    private TextField prix_cassette;

    @FXML
    private TextField titre_cassette;


    @FXML
    private TextField adress_abonne;

    @FXML
    private Button ajouter_abonne_btn;

    @FXML
    private TextField date_abonnement;



    @FXML
    private TextField nom_abonne;

    @FXML
    private TextField num_abonne;

    @FXML
    private AnchorPane menu_bar;


    @FXML
    private Button reset_btn;
    @FXML
    private TextField date_location;

    @FXML
    private TextField id_abonne;

    @FXML
    private TextField id_cassette;

    @FXML
    /*void AjouterLocation() {
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "INSERT INTO louer (IDCASSETTE ,IDABONNEE, DATELOCATION) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1, id_cassette.getText());
                preparedStatement.setString(2, id_abonne.getText());
                LocalDate currentDate = LocalDate.now();
                preparedStatement.setDate(3, Date.valueOf(currentDate));
                preparedStatement.executeUpdate();


                RestLocation();
                showInfo("Réussi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void RestLocation() {
        id_cassette.clear();
        id_abonne.clear();
        date_location.clear();

    }

    @FXML
    void AjouterAbonne() {
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "INSERT INTO ABONNEE (IDABONNEE, NOMABONNEE, ADRESSEABONNEE, DATEABONNEMENT, DATEENTREE) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1, num_abonne.getText());
                preparedStatement.setString(2, nom_abonne.getText());
                preparedStatement.setString(3, adress_abonne.getText());
                preparedStatement.setString(4, date_abonnement.getText());
                LocalDate currentDate = LocalDate.now();
                preparedStatement.setDate(5, Date.valueOf(currentDate));
                preparedStatement.executeUpdate();
                ResetAbonne();
                showInfo("Réussi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AjouterCassette() {
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "INSERT INTO CASSETTE (IDCASSETTE , IDTITRE, DATEACHAT, PRIX, DUREE,CATEGORIE,AUTEUR) VALUES (?, ?, ?, ?, ?,?,?)";
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1, num_cassette.getText());
                preparedStatement.setString(2, titre_cassette.getText());
                preparedStatement.setString(3, date_achat_cassette.getText());
                preparedStatement.setString(4, prix_cassette.getText());
                preparedStatement.setString(5, dure_cassette.getText());
                preparedStatement.setString(6, libelle_categorie.getText());
                preparedStatement.setString(7, auteur_cassette.getText());
                preparedStatement.executeUpdate();
                ResetCassette();
                showInfo("Réussi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void ResetCassette() {
        num_cassette.clear();
        titre_cassette.clear();
        date_achat_cassette.clear();
        prix_cassette.clear();
        dure_cassette.clear();
        libelle_categorie.clear();
        auteur_cassette.clear();

    }


    @FXML
    void ResetAbonne() {
        num_abonne.clear();
        adress_abonne.clear();
        date_abonnement.clear();
        nom_abonne.clear();


    }

    @FXML
    private AnchorPane menuaffiche;

    @FXML
    void OpenAjouterAbonne() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/interfaces/MenuAbonne.fxml"));
            menuaffiche.getChildren().removeAll();
            menuaffiche.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }



    }

    @FXML
    void OpenAjouterCassette() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/interfaces/MenuCassette.fxml"));
            menuaffiche.getChildren().removeAll();
            menuaffiche.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void OpenAjouterLocation() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/interfaces/MenuLocation.fxml"));
            menuaffiche.getChildren().removeAll();
            menuaffiche.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void OpenModifierAbonne() {

    }

    @FXML
    void OpenModifierCassette() {

    }

    @FXML
    void OpenModifierLocation() {

    }

    @FXML
    void OpenSuprimerAbonne() {

    }

    @FXML
    void OpenSuprimerLocation() {

    }

    @FXML
    void OpenSuprimerCassette() {

    }
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message d'information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


     */










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void search() {
    }
}
