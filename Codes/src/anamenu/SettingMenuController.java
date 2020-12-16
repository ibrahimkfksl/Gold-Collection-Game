package anamenu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SettingMenuController implements Initializable {

    @FXML
    private TextField sahaBoyutuX;

    @FXML
    private TextField sahaBoyutuY;

    @FXML
    private TextField altinOrani;

    @FXML
    private TextField gizliAltinOrani;

    @FXML
    private TextField baslangicAltinMiktari;

    @FXML
    private TextField adimSayisi;

    @FXML
    private TextField aHamleMaaliyet;

    @FXML
    private TextField aHedefBelirleme;

    @FXML
    private TextField bHamleMaaliyet;

    @FXML
    private TextField bHedefBelirleme;

    @FXML
    private TextField cHamleMaaliyet;

    @FXML
    private TextField cHedefBelirleme;

    @FXML
    private TextField dHamleMaaliyet;

    @FXML
    private TextField dHedefBelirleme;

    @FXML
    private Label uyari;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void Okay(ActionEvent event) throws IOException {
        uyari.setVisible(false);
        uyari.setText("Her şey Tamam!");
        if (sayiDonder(sahaBoyutuX) <= 0 || sayiDonder(sahaBoyutuY) <= 0) {
            uyari.setText("SAHA BOYUTU HATASI!");
            uyari.setVisible(true);
            
        } else if (sayiDonder(altinOrani) <= 0 || sayiDonder(gizliAltinOrani) <= 0) {
            uyari.setText("Altin Orani Hatasi!");
            uyari.setVisible(true);
            
        } else if (sayiDonder(baslangicAltinMiktari) <= 0) {
            uyari.setText("Baslangic Altin Miktari Hatasi!");
            uyari.setVisible(true);
            
        } else if (sayiDonder(adimSayisi) <= 0) {
            uyari.setText("Adim Sayisi Hatasi!");
            uyari.setVisible(true);
        } else if (sayiDonder(aHamleMaaliyet) <= 0 || sayiDonder(aHedefBelirleme) <= 0 || sayiDonder(bHamleMaaliyet) <= 0 || sayiDonder(bHedefBelirleme) <= 0 || sayiDonder(cHamleMaaliyet) <= 0 || sayiDonder(cHedefBelirleme) <= 0 || sayiDonder(dHamleMaaliyet) <= 0 || sayiDonder(dHedefBelirleme) <= 0) {
            uyari.setText("Oyuncu Deger Hatasi!");
            uyari.setVisible(true);
        } else {
            uyari.setVisible(false);
            degerDuzenle();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    public int sayiDonder(TextField text) {
        int sayi = Integer.parseInt(text.getText());
        return sayi;
    }
    
    public void degerDuzenle(){
        Degerler.setAdimSayisi(sayiDonder(adimSayisi));
        Degerler.setAltinOrani(sayiDonder(altinOrani));
        Degerler.setBaslangicAltinMiktari(sayiDonder(baslangicAltinMiktari));
        Degerler.setGizliAltinOrani(sayiDonder(gizliAltinOrani));
        Degerler.setSahaBoyutuX(sayiDonder(sahaBoyutuX));
        Degerler.setSahaBoyutuY(sayiDonder(sahaBoyutuY));
        
        Degerler.setaHamleMaaliyet(sayiDonder(aHamleMaaliyet));
        Degerler.setaHedefBelirleme(sayiDonder(aHedefBelirleme));
        
        Degerler.setbHamleMaaliyet(sayiDonder(bHamleMaaliyet));
        Degerler.setbHedefBelirleme(sayiDonder(bHedefBelirleme));
        
        Degerler.setcHamleMaaliyet(sayiDonder(cHamleMaaliyet));
        Degerler.setcHedefBelirleme(sayiDonder(cHedefBelirleme));
        
        Degerler.setdHamleMaaliyet(sayiDonder(dHamleMaaliyet));
        Degerler.setdHedefBelirleme(sayiDonder(dHedefBelirleme));
    }
    
}
