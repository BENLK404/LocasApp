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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private StackPane Home;

    @FXML
    private AnchorPane affichePane;

    @FXML
    private AnchorPane home_window;

    @FXML
    private Label nb_abonneeTotal;

    @FXML
    private Label nb_casetteTotal;

    @FXML
    private Label nb_locationTotal;

    @FXML
    private TextField recherche;

    @FXML
    private Button search_btn;

    private Parent fxml;
    @FXML
    private ListView<String> listViewLocation1;
    @FXML

    private ListView<String> listViewLocation2;

    @FXML
    private ListView<String> listViewCassette1;
    @FXML
    private ListView<String> listViewCassette2;

    @FXML
    void search() {

    }


    @FXML
    void Dashbord() {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/Home3.fxml"));
            home_window.getChildren().removeAll();
            home_window.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    void OpenLocation() {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/MenuLocation.fxml"));
            affichePane.getChildren().removeAll();
            affichePane.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }



    }

    @FXML
    void OpenAbonne() {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/MenuAbonne.fxml"));
            affichePane.getChildren().removeAll();
            affichePane.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    void OpenCassette() {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/MenuCasette.fxml"));
            affichePane.getChildren().removeAll();
            affichePane.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    @FXML
    void OpenContact() {
        try {
            fxml = FXMLLoader.load(getClass().getResource("/interfaces/MenuContact.fxml"));
            affichePane.getChildren().removeAll();
            affichePane.getChildren().setAll(fxml);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void SignOut() {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message de confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez vous sortir?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)){
            home_window.getScene().getWindow().hide();
            Stage stage = new Stage();
            try {
                fxml = FXMLLoader.load(getClass().getResource("/interfaces/SignIn.fxml"));
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


    }
    public void NbAbonne(){
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "SELECT COUNT(*) FROM abonne";

            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                result.getInt(1);
                nb_abonneeTotal.setText(String.valueOf(result.getInt(1))+"K");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void NbCassette() {
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "SELECT COUNT(*) FROM cassette";

            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                result.getInt(1);
                nb_casetteTotal.setText(String.valueOf(result.getInt(1))+"K");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void NbLocation(){
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "SELECT COUNT(*) FROM location";

            PreparedStatement prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                result.getInt(1);
                nb_locationTotal.setText(String.valueOf(result.getInt(1))+"K");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void afficherTroisDerniersLocation() {
        listViewLocation1.getItems().clear(); // Clear previous items
        try (Connection connection = Connectivity.connectDb()) {
            String query = "SELECT location.*, abonne.NomAbonne, cassette.Titre FROM location INNER JOIN abonne ON location.NumAbonne = abonne.NumAbonne inner join cassette on cassette.NumCassette = abonne.NumAbonne ORDER BY DateLocation DESC LIMIT 3";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String abonneInfo1 = resultSet.getString("NomAbonne") +
                            "             Cassette: " + resultSet.getString("Titre");
                    listViewLocation1.getItems().add(abonneInfo1);
                    String abonneInfo2 = resultSet.getString("DateLocation");
                    listViewLocation2.getItems().add(abonneInfo2);



                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    private void afficherTroisDerniersCassette() {
        listViewCassette1.getItems().clear();
        try (Connection connection = Connectivity.connectDb()) {
            String query = "SELECT * FROM cassette ORDER BY NumCassette DESC LIMIT 3";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String cassetteInfo1 = "N°Cassette: " + resultSet.getString("NumCassette") +
                            "        Titre: " + resultSet.getString("Titre");
                    listViewCassette1.getItems().add(cassetteInfo1);
                    String cassetteInfo2 =  resultSet.getString("DateAchat") ;
                    listViewCassette2.getItems().add(cassetteInfo2);



                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NbAbonne();
        NbCassette();
        NbLocation();
        afficherTroisDerniersLocation();
        afficherTroisDerniersCassette();

    }

}
