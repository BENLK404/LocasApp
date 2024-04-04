package database;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AbonneData implements Initializable {
    private Integer idAbonne;
    private String nomAbonne;
    private String prenomAbonne;
    private String adresseAbonne;
    private Date dateAbm;
    private Date dateEntrer;

    public AbonneData(Integer idAbonne, String nomAbonne, String prenomAbonne, String adresseAbonne, Date dateAbm, Date dateEntrer) {
        this.idAbonne = idAbonne;
        this.nomAbonne = nomAbonne;
        this.prenomAbonne = prenomAbonne;
        this.adresseAbonne = adresseAbonne;
        this.dateAbm = dateAbm;
        this.dateEntrer = dateEntrer;
    }

    public Integer getIdAbonne() {
        return idAbonne;
    }

    public void setIdAbonne(Integer idAbonne) {
        this.idAbonne = idAbonne;
    }

    public String getNomAbonne() {
        return nomAbonne;
    }

    public void setNomAbonne(String nomAbonne) {
        this.nomAbonne = nomAbonne;
    }

    public String getPrenomAbonne() {
        return prenomAbonne;
    }

    public void setPrenomAbonne(String prenomAbonne) {
        this.prenomAbonne = prenomAbonne;
    }

    public String getAdresseAbonne() {
        return adresseAbonne;
    }

    public void setAdresseAbonne(String adresseAbonne) {
        this.adresseAbonne = adresseAbonne;
    }

    public Date getDateAbm() {
        return dateAbm;
    }

    public void setDateAbm(Date dateAbm) {
        this.dateAbm = dateAbm;
    }

    public Date getDateEntrer() {
        return dateEntrer;
    }

    public void setDateEntrer(Date dateEntrer) {
        this.dateEntrer = dateEntrer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
