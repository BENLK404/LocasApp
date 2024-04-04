package dao;

import database.AbonneData;
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

public class AbonneDAO implements Initializable {
    @FXML
    private TableView<AbonneData> table_abonne;
    @FXML
    private TableColumn<AbonneData, String> abonne_col_adresse_abonne;
    @FXML
    private TableColumn<AbonneData, String> abonne_col_date_abm_abonne;
    @FXML
    private TableColumn<AbonneData, String> abonne_col_date_entrer;
    @FXML
    private TableColumn<AbonneData, String> abonne_col_id_abonne;
    @FXML
    private TableColumn<AbonneData, String> abonne_col_nom_abonne;
    @FXML
    private TableColumn<AbonneData, String> abonne_col_prenom_abonne;
    @FXML
    private TextField adresse_abonne;
    @FXML
    private TextField date_abm;
    @FXML
    private TextField date_entre;
    @FXML
    private TextField id_abonne;
    @FXML
    private AnchorPane menuaffiche;
    @FXML
    private AnchorPane affiche_table_abonne;
    @FXML
    private AnchorPane menuaffiche1;
    @FXML
    private TextField nom_abonne;
    @FXML
    private TextField prenom_abonne;
    @FXML
    private TextField search;

    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    void AjouterAbonne() {
        if (nom_abonne.getText().isEmpty() ||
                adresse_abonne.getText().isEmpty() || date_abm.getText().isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }
        try (Connection connect = Connectivity.connectDb()) {
            int nextId = getNextAbonneId(connect);

            String checkIfExistsQuery = "SELECT * FROM abonne WHERE NomAbonne=? AND PrenomAbonne=?";
            try (PreparedStatement checkIfExistsStmt = connect.prepareStatement(checkIfExistsQuery)) {
                checkIfExistsStmt.setString(1, nom_abonne.getText());
                checkIfExistsStmt.setString(2, prenom_abonne.getText());
                ResultSet resultSet = checkIfExistsStmt.executeQuery();
                if (resultSet.next()) {
                    showError("Un abonné avec ces noms existe déjà.");
                } else {
                    String insertQuery = "INSERT INTO abonne (NomAbonne, AdresseAbonne, DateAbonnement, DateEntree, PrenomAbonne,NumAbonne) VALUES (?, ?, ?, ?, ?,?)";
                    try (PreparedStatement preparedStatement = connect.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, nom_abonne.getText());
                        preparedStatement.setString(2, adresse_abonne.getText());
                        preparedStatement.setString(3, date_abm.getText());
                        preparedStatement.setString(5, prenom_abonne.getText());
                        preparedStatement.setInt(6, nextId);
                        if (date_entre.getText().isEmpty()){
                            LocalDate currentDate = LocalDate.now();
                            preparedStatement.setDate(4, Date.valueOf(currentDate));
                            preparedStatement.executeUpdate();
                            addAbonneShowListData();
                            ResetAbonne();
                            showInfo("Ajout Réussi");
                        } else {
                            preparedStatement.setString(4, date_entre.getText());
                            preparedStatement.executeUpdate();
                            addAbonneShowListData();
                            ResetAbonne();
                            showInfo("Ajout Réussi");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int getNextAbonneId(Connection connect) throws SQLException {
        String maxIdQuery = "SELECT MAX(NumAbonne) FROM abonne";
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
    void ModifierAbonne() {
        if (nom_abonne.getText().isEmpty() || adresse_abonne.getText().isEmpty() || date_abm.getText().isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try (Connection connect = Connectivity.connectDb()) {
            String checkIfExistsQuery = "SELECT * FROM abonne WHERE NumAbonne=?";
            try (PreparedStatement checkIfExistsStmt = connect.prepareStatement(checkIfExistsQuery)) {
                checkIfExistsStmt.setString(1, id_abonne.getText());
                ResultSet resultSet = checkIfExistsStmt.executeQuery();

                if (resultSet.next()) {
                    String updateQuery = "UPDATE abonne SET NomAbonne=?, AdresseAbonne=?, DateAbonnement=?, DateEntree=?, PrenomAbonne=? WHERE NumAbonne=?";
                    try (PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, nom_abonne.getText());
                        preparedStatement.setString(2, adresse_abonne.getText());
                        preparedStatement.setString(3, date_abm.getText());
                        LocalDate currentDate = LocalDate.now();
                        preparedStatement.setDate(4, Date.valueOf(currentDate));
                        preparedStatement.setString(5, prenom_abonne.getText());
                        preparedStatement.setString(6, id_abonne.getText());
                        preparedStatement.executeUpdate();
                        addAbonneShowListData();
                        ResetAbonne();
                        showInfo("Modification Réussie");
                    }
                } else {
                    showError("Aucun abonné avec cet ID n'existe.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ResetAbonne() {
        id_abonne.clear();
        adresse_abonne.clear();
        date_abm.clear();
        nom_abonne.clear();
        prenom_abonne.clear();
        date_entre.clear();
    }

    @FXML
    void Search() {
        String searchTerm = search.getText();
        if (!searchTerm.isEmpty()) {
            try (Connection connect = Connectivity.connectDb()) {
                String sql = "SELECT * FROM abonne WHERE NumAbonne LIKE ? OR NomAbonne LIKE ? OR PrenomAbonne LIKE ? OR AdresseAbonne LIKE ? OR DateAbonnement LIKE ? OR DateEntree LIKE ?";
                try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                    preparedStatement.setString(1, "%" + searchTerm + "%");
                    preparedStatement.setString(2, "%" + searchTerm + "%");
                    preparedStatement.setString(3, "%" + searchTerm + "%");
                    preparedStatement.setString(4, "%" + searchTerm + "%");
                    preparedStatement.setString(5, "%" + searchTerm + "%");
                    preparedStatement.setString(6, "%" + searchTerm + "%");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ObservableList<AbonneData> searchResults =
                            FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        AbonneData abonneD = new AbonneData(
                                resultSet.getInt("NumAbonne"),
                                resultSet.getString("NomAbonne"),
                                resultSet.getString("PrenomAbonne"),
                                resultSet.getString("AdresseAbonne"),
                                resultSet.getDate("DateAbonnement"),
                                resultSet.getDate("DateEntree")
                        );
                        searchResults.add(abonneD);
                    }
                    table_abonne.setItems(searchResults);
                    table_abonne.refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showError("Veuillez entrer un terme de recherche.");
        }
    }

    @FXML
    void SuprimerAbonne() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet abonné?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String abonneId = id_abonne.getText();
            if (!abonneId.isEmpty() && abonneId.matches("\\d+")) {
                if (abonneExists(abonneId)) {
                    try (Connection connect = Connectivity.connectDb()) {
                        String deleteQuery = "DELETE FROM abonne WHERE NumAbonne=?";
                        try (PreparedStatement preparedStatement = connect.prepareStatement(deleteQuery)) {
                            preparedStatement.setString(1, abonneId);
                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                showInfo("Suppression Réussie");

                                ResetAbonne();
                                addAbonneShowListData();
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







    private boolean abonneExists(String abonneId) {
        try (Connection connect = Connectivity.connectDb()) {
            String checkIfExistsQuery = "SELECT * FROM abonne WHERE NumAbonne=?";
            try (PreparedStatement checkIfExistsStmt = connect.prepareStatement(checkIfExistsQuery)) {
                checkIfExistsStmt.setString(1, abonneId);
                ResultSet resultSet = checkIfExistsStmt.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message d'information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public ObservableList<AbonneData> addbonneData() {
        ObservableList<AbonneData> listData = FXCollections.observableArrayList();
        try (Connection connect = Connectivity.connectDb()) {
            String sql = "SELECT * FROM abonne order by NumAbonne desc ";
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            AbonneData abonneD;
            while (result.next()) {
                abonneD = new AbonneData(result.getInt("NumAbonne"),
                        result.getString("NomAbonne"),
                        result.getString("PrenomAbonne"),
                        result.getString("AdresseAbonne"),
                        result.getDate("DateAbonnement"),
                        result.getDate("DateEntree"));
                listData.add(abonneD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<AbonneData> addAbonneList;
    public void addAbonneShowListData() {
        addAbonneList = addbonneData();
        abonne_col_id_abonne.setCellValueFactory(new PropertyValueFactory<>("idAbonne"));
        abonne_col_nom_abonne.setCellValueFactory(new PropertyValueFactory<>("nomAbonne"));
        abonne_col_prenom_abonne.setCellValueFactory(new PropertyValueFactory<>("prenomAbonne"));
        abonne_col_adresse_abonne.setCellValueFactory(new PropertyValueFactory<>("adresseAbonne"));
        abonne_col_date_abm_abonne.setCellValueFactory(new PropertyValueFactory<>("dateAbm"));
        abonne_col_date_entrer.setCellValueFactory(new PropertyValueFactory<>("dateEntrer"));
        table_abonne.setItems(addAbonneList);
        table_abonne.refresh();
    }

    public void AbonneSelect() {
        AbonneData abonneD = table_abonne.getSelectionModel().getSelectedItem();
        if (abonneD != null) {
            id_abonne.setText(String.valueOf(abonneD.getIdAbonne()));
            nom_abonne.setText(abonneD.getNomAbonne());
            prenom_abonne.setText(abonneD.getPrenomAbonne());
            adresse_abonne.setText(abonneD.getAdresseAbonne());
            date_abm.setText(String.valueOf(abonneD.getDateAbm()));
            date_entre.setText(String.valueOf(abonneD.getDateEntrer()));
        } else {
            ResetAbonne(); // Clear fields if no selection is made
        }
    }




    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message d'erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAbonneShowListData();
        addbonneData();
        table_abonne.refresh();


    }
}

