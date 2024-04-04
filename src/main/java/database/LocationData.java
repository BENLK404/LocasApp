package database;

import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LocationData implements Initializable {

    private Integer idLocationAbonne;
    private Integer idLocationCassette;
    private String nomAbonne;
    private Date dateLocation;


    private Date dateRetour;


    public LocationData(Integer idLocationAbonne, Integer idLocationCassette, String nomAbonne, Date dateLocation,Date dateRetour) {
        this.idLocationAbonne = idLocationAbonne;
        this.idLocationCassette = idLocationCassette;
        this.nomAbonne = nomAbonne;
        this.dateLocation = dateLocation;
        this.dateRetour = dateRetour;
    }
    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }


    public Integer getIdLocationAbonne() {
        return idLocationAbonne;
    }

    public void setIdLocationAbonne(Integer idLocationAbonne) {
        this.idLocationAbonne = idLocationAbonne;
    }

    public Integer getIdLocationCassette() {
        return idLocationCassette;
    }

    public void setIdLocationCassette(Integer idLocationCassette) {
        this.idLocationCassette = idLocationCassette;
    }

    public String getNomAbonne() {
        return nomAbonne;
    }

    public void setNomAbonne(String nomAbonne) {
        this.nomAbonne = nomAbonne;
    }

    public Date getDateLocation() {
        return dateLocation;
    }

    public void setDateLocation(Date dateLocation) {
        this.dateLocation = dateLocation;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
