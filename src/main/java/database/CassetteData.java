package database;

import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.UUID;

public class CassetteData implements Initializable {
    private Integer  idCassette;
    private String Titre;

    private Date dateAchat;
    private Long prix;
    private Long duree;
    private String categorie;
    private String auteur;


    public CassetteData(Integer idCassette, String Titre, Date dateAchat, Long prix, Long duree, String categorie, String auteur) {
        this.idCassette = idCassette;
        this.Titre = Titre;
        this.dateAchat = dateAchat;
        this.prix = prix;
        this.duree = duree;
        this.categorie = categorie;
        this.auteur = auteur;
    }

    public Integer getIdCassette() {
        return idCassette;
    }

    public void setIdCassette(Integer idCassette) {
        this.idCassette = idCassette;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String Titre) {
        this.Titre = Titre;
    }



    public Date getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Date dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Long getPrix() {
        return prix;
    }

    public void setPrix(Long prix) {
        this.prix = prix;
    }

    public Long getDuree() {
        return duree;
    }

    public void setDuree(Long duree) {
        this.duree = duree;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
