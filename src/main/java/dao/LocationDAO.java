package dao;

import database.Connectivity;
import database.LocationData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LocationDAO implements Initializable {
    @FXML
    private TextField date_retour;

    @FXML
    private TextField id_abonne;

    @FXML
    private TextField id_cassette;

    @FXML
    private TableColumn<?, ?> location_col_dateRetour_location;

    @FXML
    private TableColumn<LocationData, String> location_col_date_location;

    @FXML
    private TableColumn<LocationData, String> location_col_id_abonne;

    @FXML
    private TableColumn<LocationData, String> location_col_id_cassette;

    @FXML
    private TableColumn<LocationData, String> location_col_nom_abonne;

    @FXML
    private AnchorPane menuaffiche;

    @FXML
    private AnchorPane menuaffiche1;

    @FXML
    private Button search_btn;

    @FXML
    private TextField search_location;

    @FXML
    private TableView<LocationData> table_location;

    private ResultSet result;
    private PreparedStatement prepare;

    private ObservableList<LocationData> addLocationList;
    @FXML
    void AjouterLocation() {
        // Vérifier si les champs sont vides
        if (id_abonne.getText().isEmpty() || id_cassette.getText().isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try (Connection connect = Connectivity.connectDb()) {
            // Vérifier si l'IDABONNEE existe
            if (!checkIfExists("abonne", "NumAbonne", id_abonne.getText())) {
                showError("Le Numéro abonné spécifié n'existe pas dans la base de données.");
                return;
            }

            // Vérifier si l'IDCASSETTE existe
            if (!checkIfExists("cassette", "NumCassette", id_cassette.getText())) {
                showError("Le Numéro cassette spécifié n'existe pas dans la base de données.");
                return;
            }

            // Vérifier le nombre de locations actuelles de l'abonné
            int nombreLocationsAbonne = getNombreLocationsAbonne(id_abonne.getText());
            if (nombreLocationsAbonne >= 3) {
                showError("L'abonné a atteint la limite de 3 locations.");
                return;
            }

            // Vérifier si la location existe déjà
            if (locationExists(id_abonne.getText(), id_cassette.getText())) {
                // Si la location existe déjà, mettre à jour la date de location
                updateLocationDate(id_abonne.getText(), id_cassette.getText());
                showInfo("Date de location mise à jour avec succès.");
            } else {
                // Sinon, insérer une nouvelle ligne
                insertNewLocation(id_abonne.getText(), id_cassette.getText());
                showInfo("Location ajoutée avec succès.");
            }

            // Actualiser l'affichage des locations dans la table
            addLocationShowListData();

            // Réinitialiser les champs après l'ajout
            ResetLocation();

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur lors de l'ajout ou de la mise à jour de la location.");
        }
    }

    // Nouvelle méthode pour insérer une nouvelle location
    private void insertNewLocation(String idAbonne, String idCassette) throws SQLException {
        String insertQuery = "INSERT INTO location (NumAbonne, NumCassette, DateLocation) VALUES (?, ?, ?)";
        try (Connection connect = Connectivity.connectDb();
             PreparedStatement preparedStatement = connect.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, idAbonne);
            preparedStatement.setString(2, idCassette);
            LocalDate currentDate = LocalDate.now();
            preparedStatement.setDate(3, Date.valueOf(currentDate));
            preparedStatement.executeUpdate();
        }
    }

    // Nouvelle méthode pour mettre à jour la date de location existante
    private void updateLocationDate(String idAbonne, String idCassette) throws SQLException {
        String updateQuery = "UPDATE location SET DateLocation = ? WHERE NumAbonne = ? AND NumCassette = ?";
        try (Connection connect = Connectivity.connectDb();
             PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {
            LocalDate currentDate = LocalDate.now();
            preparedStatement.setDate(1, Date.valueOf(currentDate));
            preparedStatement.setString(2, idAbonne);
            preparedStatement.setString(3, idCassette);
            preparedStatement.executeUpdate();
        }
    }

    // Nouvelle méthode pour vérifier si la location existe déjà
    private boolean locationExists(String idAbonne, String idCassette) throws SQLException {
        try (Connection connect = Connectivity.connectDb()) {
            String query = "SELECT * FROM location WHERE NumAbonne = ? AND NumCassette = ?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                preparedStatement.setString(1, idAbonne);
                preparedStatement.setString(2, idCassette);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // true si la location existe déjà
                }
            }
        }
    }

    // Nouvelle méthode pour obtenir le nombre de locations actuelles de l'abonné
    private int getNombreLocationsAbonne(String idAbonne) throws SQLException {
        try (Connection connect = Connectivity.connectDb()) {
            String query = "SELECT COUNT(*) FROM location WHERE NumAbonne = ?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                preparedStatement.setString(1, idAbonne);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    private boolean checkIfExists(String tableName, String columnName, String value) throws SQLException {
        try (Connection connect = Connectivity.connectDb()) {
            String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
                preparedStatement.setString(1, value);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }
    }


    @FXML
    void ModifierLocation() throws SQLException {
        if (id_abonne.getText().isEmpty() || id_cassette.getText().isEmpty()) {
            showError("Veuillez sélectionner une location à modifier.");
            return;
        }

        if (!checkIfExists("cassette", "NumCassette", id_cassette.getText())) {
            showError("Le Numéro de cassette spécifié n'existe pas dans la base de données.");
            return;
        }

        try (Connection connect = Connectivity.connectDb()) {
            String updateQuery = "UPDATE location SET NumCassette=?,DateLocation=? , WHERE NumAbonne=?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, id_cassette.getText());
                LocalDate currentDate = LocalDate.now();
                preparedStatement.setDate(2, Date.valueOf(currentDate));
                preparedStatement.setString(3, id_abonne.getText());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    addLocationShowListData();
                    ResetLocation();
                    showInfo("Modification Réussie");
                } else {
                    showError("Aucune location correspondante trouvée.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur de modification de la location.");
            addLocationShowListData();
        }
    }

    @FXML
    void AjouterDateRetour() throws SQLException {
        if (id_abonne.getText().isEmpty() || id_cassette.getText().isEmpty()) {
            showError("Veuillez sélectionner une location à modifier.");
            return;
        }

        if (!checkIfExists("cassette", "NumCassette", id_cassette.getText())) {
            showError("Le Numéro de cassette spécifié n'existe pas dans la base de données.");
            return;
        }

        try (Connection connect = Connectivity.connectDb()) {
            String updateQuery = "UPDATE location SET DateRetour=? WHERE NumAbonne=? AND NumCassette =?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {
                preparedStatement.setString(3, id_cassette.getText());
                preparedStatement.setString(1, date_retour.getText());
                preparedStatement.setString(2, id_abonne.getText());
                if (date_retour.getText().isEmpty() || date_retour.getText().equals("null")){
                    LocalDate currentDate = LocalDate.now();
                    preparedStatement.setDate(1, Date.valueOf(currentDate));
                }

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    addLocationShowListData();
                    ResetLocation();
                    showInfo("Date de retour ajouté");
                } else {
                    showError("Aucune location correspondante trouvée.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur lors de l'ajout de la date retour.");
        }
    }


    @FXML
    void ResetLocation() {
        id_abonne.clear();
        id_cassette.clear();
        date_retour.clear();
    }

    @FXML
    void SuprimerLocation() {
        if (id_abonne.getText().isEmpty() || id_cassette.getText().isEmpty()) {
            showError("Veuillez sélectionner une location à supprimer.");
            return;
        }

        if (!checkIfExistsLocation(id_abonne.getText(), id_cassette.getText())) {
            showError("La location spécifiée n'existe pas dans la base de données.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette location?");

        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try (Connection connect = Connectivity.connectDb()) {
            String deleteQuery = "DELETE FROM location WHERE NumAbonne = ? AND NumCassette = ?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, id_abonne.getText());
                preparedStatement.setString(2, id_cassette.getText());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    addLocationShowListData();
                    ResetLocation();
                    showInfo("Suppression réussie.");
                } else {
                    showError("Aucune location correspondante trouvée.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur lors de la suppression de la location.");
        }

    }

    private boolean checkIfExistsLocation(String idAbonne, String idCassette) {
        try (Connection connect = Connectivity.connectDb()) {
            String checkQuery = "SELECT COUNT(*) FROM location WHERE NumAbonne = ? AND NumCassette = ?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(checkQuery)) {
                preparedStatement.setString(1, idAbonne);
                preparedStatement.setString(2, idCassette);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @FXML
    void search() {
        String searchTerm = search_location.getText();

        if (searchTerm.isEmpty()) {
            addLocationShowListData();
        } else {
            try (Connection connect = Connectivity.connectDb()) {
                String searchQuery = "SELECT * FROM location INNER JOIN cassette ON location.NumCassette = cassette.NumCassette INNER JOIN abonne ON location.NumAbonne = abonne.NumAbonne WHERE location.NumCassette LIKE ? OR location.NumAbonne LIKE ? OR cassette.Titre LIKE ? OR abonne.NomAbonne LIKE ?";

                try (PreparedStatement preparedStatement = connect.prepareStatement(searchQuery)) {
                    preparedStatement.setString(1, "%" + searchTerm + "%");
                    preparedStatement.setString(2, "%" + searchTerm + "%");
                    preparedStatement.setString(3, "%" + searchTerm + "%");
                    preparedStatement.setString(4, "%" + searchTerm + "%");

                    ResultSet resultSet = preparedStatement.executeQuery();

                    ObservableList<LocationData> searchResults = FXCollections.observableArrayList();

                    while (resultSet.next()) {
                        LocationData locationData = new LocationData(
                                resultSet.getInt("NumAbonne"),
                                resultSet.getInt("NumCassette"),
                                resultSet.getString("NomAbonne"),
                                resultSet.getDate("DateLocation"),
                                resultSet.getDate("DateRetour")
                        );
                        searchResults.add(locationData);
                    }

                    location_col_id_abonne.setCellValueFactory(new PropertyValueFactory<>("idLocationAbonne"));
                    location_col_id_cassette.setCellValueFactory(new PropertyValueFactory<>("idLocationCassette"));
                    location_col_date_location.setCellValueFactory(new PropertyValueFactory<>("dateLocation"));
                    location_col_nom_abonne.setCellValueFactory(new PropertyValueFactory<>("nomAbonne"));
                    location_col_dateRetour_location.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));
                    table_location.setItems(searchResults);
                    table_location.refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Erreur lors de la recherche de locations.");
            }
        }
    }


    public void addLocationShowListData() {

        addLocationList = addLocationData();
        location_col_id_abonne.setCellValueFactory(new PropertyValueFactory<>("idLocationAbonne"));
        location_col_id_cassette.setCellValueFactory(new PropertyValueFactory<>("idLocationCassette"));
        location_col_nom_abonne.setCellValueFactory(new PropertyValueFactory<>("nomAbonne"));
        location_col_date_location.setCellValueFactory(new PropertyValueFactory<>("dateLocation"));
        location_col_dateRetour_location.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));
        table_location.setItems(addLocationList);
        table_location.refresh();
    }


    public ObservableList<LocationData> addLocationData() {
        ObservableList<LocationData> listData = FXCollections.observableArrayList();
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "SELECT location.*, abonne.NomAbonne FROM location INNER JOIN abonne ON location.NumAbonne = abonne.NumAbonne";
                    ;

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            LocationData locationD;
            while (result.next()) {
                locationD = new LocationData(
                        result.getInt("NumAbonne"),
                        result.getInt("NumCassette"),
                        result.getString("NomAbonne"),
                        result.getDate("DateLocation"),
                        result.getDate("DateRetour")
                );
                listData.add(locationD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }





    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message d'erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message d'information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void LocationSelect(){
        LocationData locationD = table_location.getSelectionModel().getSelectedItem();
        if ( locationD != null) {
            id_abonne.setText(String.valueOf(locationD.getIdLocationAbonne()));
            id_cassette.setText(String.valueOf(locationD.getIdLocationCassette()));
            date_retour.setText(String.valueOf(locationD.getDateRetour()));
        }else {
            ResetLocation(); // Clear fields if no selection is// made
         }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addLocationData();
        addLocationShowListData();
        LocationSelect();
    }

}
