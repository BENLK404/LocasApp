package dao;

import database.CassetteData;
import database.Connectivity;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class CassetteDAO implements Initializable {

    @FXML
    private TextField auteur_cassette;

    @FXML
    private TableColumn<CassetteData,String> cassette_col_auteur_cassette;

    @FXML
    private TableColumn<CassetteData,String> cassette_col_categorie_cassette;

    @FXML
    private TableColumn<CassetteData,String> cassette_col_duerr_cassette;

    @FXML
    private TableColumn<CassetteData,String> cassette_col_idcassette_cassette;

    @FXML
    private TableColumn<CassetteData,String> cassette_col_prix_cassette;

    @FXML
    private TableColumn<CassetteData,String> cassette_col_titre_cassette;

    @FXML
    private TextField dure_cassette;

    @FXML
    private TextField id_cassette;

    @FXML
    private TextField titre;

    @FXML
    private TextField libelle_categorie;

    @FXML
    private AnchorPane menuaffiche;

    @FXML
    private AnchorPane menuaffiche1;

    @FXML
    private TextField prix_cassette;

    @FXML
    private TextField search_cassette;

    @FXML
    private Button search_cassette_btn;

    @FXML
    private TableView<CassetteData> table_cassette;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    void AjouterCassette() {
        if (auteur_cassette.getText().isEmpty() || titre.getText().isEmpty() ||
                dure_cassette.getText().isEmpty() || libelle_categorie.getText().isEmpty()
                || prix_cassette.getText().isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try (Connection connect = Connectivity.connectDb()) {
            int nextId = getNextCassetteId(connect);
            String insertQuery = "INSERT INTO cassette (Titre , DateAchat, Prix, Duree,Categorie,Auteur,NumCassette) VALUES (?, ?, ?, ?, ?,?,?)";
            try (PreparedStatement preparedStatement = connect.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, titre.getText());
                preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
                preparedStatement.setString(3, prix_cassette.getText());
                preparedStatement.setString(4, dure_cassette.getText());
                preparedStatement.setString(5, libelle_categorie.getText());
                preparedStatement.setString(6, auteur_cassette.getText());
                preparedStatement.setInt(7, nextId);

                preparedStatement.executeUpdate();
                addCassetteShowListData();
                ResetCassette();
                showInfo("Ajout Réussi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur lors de l'ajout de la cassette.");
        }
    }



    private int getNextCassetteId(Connection connect) throws SQLException {
        String maxIdQuery = "SELECT MAX(NumCassette) FROM cassette";
        try (PreparedStatement maxIdStatement = connect.prepareStatement(maxIdQuery);
             ResultSet resultSet = maxIdStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            } else {
                // Si la table est vide, commencez à partir de 1
                return 1;
            }
        }
    }


    @FXML
    void ModifierCassette() {
        if (id_cassette.getText().isEmpty()) {
            showError("Veuillez sélectionner une cassette à modifier.");
            return;
        }

        try (Connection connect = Connectivity.connectDb()) {
            String updateQuery = "UPDATE cassette SET Titre=?, DateAchat=?, Prix=?, Duree=?, Categorie=?, Auteur=? WHERE NumCassette=?";
            try (PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, titre.getText());
                preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
                preparedStatement.setString(3, prix_cassette.getText());
                preparedStatement.setString(4, dure_cassette.getText());
                preparedStatement.setString(5, libelle_categorie.getText());
                preparedStatement.setString(6, auteur_cassette.getText());
                preparedStatement.setString(7, id_cassette.getText());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    addCassetteShowListData();
                    ResetCassette();
                    showInfo("Modification Réussie");
                } else {
                    showError("Aucune cassette correspondante trouvée.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur lors de la modification de la cassette.");
        }
    }

    private ObservableList<CassetteData> addCassetteList;

    private void addCassetteShowListData() {
        addCassetteList = addCassetteData();
        cassette_col_idcassette_cassette.setCellValueFactory(new
                PropertyValueFactory<>("idCassette"));
        cassette_col_titre_cassette.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        cassette_col_auteur_cassette.setCellValueFactory(new
                PropertyValueFactory<>("auteur"));
        cassette_col_categorie_cassette.setCellValueFactory(new
                PropertyValueFactory<>("categorie"));
        cassette_col_duerr_cassette.setCellValueFactory(new
                PropertyValueFactory<>("duree"));
        cassette_col_prix_cassette.setCellValueFactory(new
                PropertyValueFactory<>("prix"));
        table_cassette.setItems(addCassetteList);
        table_cassette.refresh();


    }



    @FXML
    void ResetCassette() {
        id_cassette.clear();
        titre.clear();
        auteur_cassette.clear();
        prix_cassette.clear();
        libelle_categorie.clear();
        dure_cassette.clear();


    }

    @FXML
    void SuprimerCassette() {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette cassette?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String cassetteId = id_cassette.getText();
                if (!cassetteId.isEmpty() && cassetteId.matches("\\d+")) { // Check if the ID is a non-emptynumeric string
                    if (cassetteExists(cassetteId)) {
                        try (Connection connect = Connectivity.connectDb()) {
                            String deleteQuery = "DELETE FROM cassette WHERE NumCassette=?";
                            try (PreparedStatement preparedStatement =
                                         connect.prepareStatement(deleteQuery)) {
                                preparedStatement.setString(1, cassetteId);
                                int rowsAffected = preparedStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    showInfo("Suppression Réussie");
                                    addCassetteShowListData(); // Refresh the table after deletion
                                    ResetCassette();
                                } else {
                                    showError("Aucun abonné avec cet ID n'existe.");
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showError("Aucun abonné avec cet ID n'existe.");
                    }
                } else {
                    showError("Veuillez sélectionner un abonné à supprimer.");
                }
            }

    }

    @FXML
    void search() {
        String searchTerm = search_cassette.getText();
        if (!searchTerm.isEmpty()) {
            try (Connection connect = Connectivity.connectDb()) {
                String sql = "SELECT * FROM cassette WHERE NumCassette  LIKE ? OR Titre  LIKE ? OR DateAchat LIKE ? OR Prix LIKE ? OR Duree LIKE ? OR Categorie LIKE ?OR Auteur LIKE ?";
                try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                    preparedStatement.setString(1, "%" + searchTerm + "%");
                    preparedStatement.setString(2, "%" + searchTerm + "%");
                    preparedStatement.setString(3, "%" + searchTerm + "%");
                    preparedStatement.setString(4, "%" + searchTerm + "%");
                    preparedStatement.setString(5, "%" + searchTerm + "%");
                    preparedStatement.setString(6, "%" + searchTerm + "%");
                    preparedStatement.setString(7, "%" + searchTerm + "%");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ObservableList<CassetteData> searchResults =
                            FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        CassetteData cassetteD = new CassetteData(
                                resultSet.getInt("NumCassette"),
                                resultSet.getString("Titre"),
                                resultSet.getDate("DateAchat"),
                                resultSet.getLong("Prix"),
                                resultSet.getLong("Duree"),
                                resultSet.getString("Categorie"),
                                resultSet.getString("Auteur"));
                        searchResults.add(cassetteD);
                    }
                    table_cassette.setItems(searchResults);
                    table_cassette.refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showError("Veuillez entrer un terme de recherche.");
        }


    }
    public ObservableList<CassetteData> addCassetteData() {
        ObservableList<CassetteData> listData = FXCollections.observableArrayList();
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "SELECT * FROM cassette ORDER BY NumCassette DESC";

            try (PreparedStatement prepare = connect.prepareStatement(sql);
                 ResultSet result = prepare.executeQuery()) {

                CassetteData cassetteD;
                while (result.next()) {
                    cassetteD = new CassetteData(
                            result.getInt("NumCassette"),
                            result.getString("Titre"),
                            result.getDate("DateAchat"),
                            result.getLong("Prix"),
                            result.getLong("Duree"),
                            result.getString("Categorie"),
                            result.getString("Auteur")
                    );
                    listData.add(cassetteD);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void CassetteSelect(){
        CassetteData cassetteD = table_cassette.getSelectionModel().getSelectedItem();
        if (cassetteD != null) {
            id_cassette.setText(String.valueOf(cassetteD.getIdCassette()));
            titre.setText(String.valueOf(cassetteD.getTitre()));
            auteur_cassette.setText(cassetteD.getAuteur());
            prix_cassette.setText(String.valueOf(cassetteD.getPrix()));
            libelle_categorie.setText(cassetteD.getCategorie());
            dure_cassette.setText(String.valueOf(cassetteD.getDuree()));
        } else {
            ResetCassette(); // Clear fields if no selection is made
        }
    }


    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message d'information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message d'erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    private boolean cassetteExists(String cassetteId) {
        try (Connection connect = Connectivity.connectDb()) {
            String checkIfExistsQuery = "SELECT * FROM cassette WHERE NumCassette=?";
            try (PreparedStatement checkIfExistsStmt =
                         connect.prepareStatement(checkIfExistsQuery)) {
                checkIfExistsStmt.setString(1, cassetteId);
                ResultSet resultSet = checkIfExistsStmt.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCassetteShowListData();
        CassetteSelect();
        addCassetteData();
    }
}
