package anamenu;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

import backend.*;

import javax.swing.*;

public class OyunEkrani extends Application {

    public int KARE_BOYUT = 20;
    public int sahaBoyutX = Degerler.getSahaBoyutuX();
    public int sahaBoyutY = Degerler.getSahaBoyutuY();
    public static Rectangle[][] board;
    private Group root;
    private Scene scene;
    static VBox box = new VBox();


    public static Label labelA = new Label();
    public static Label labelB = new Label();
    public static Label labelC = new Label();
    public static Label labelD = new Label();
    public static Label altin = new Label();


    public void scoreBoard(){
        labelA.setStyle("-fx-text-weight:bold; -fx-border-style:solid; -fx-border-color:white; -fx-border-width:3; -fx-border-insets: 5px; -fx-padding: 2px;");
        labelB.setStyle("-fx-text-weight:bold; -fx-border-style:solid; -fx-border-color:white; -fx-border-width:3; -fx-border-insets: 5px; -fx-padding: 2px;");
        labelC.setStyle("-fx-text-weight:bold; -fx-border-style:solid; -fx-border-color:white; -fx-border-width:3; -fx-border-insets: 5px; -fx-padding: 2px;");
        labelD.setStyle("-fx-text-weight:bold; -fx-border-style:solid; -fx-border-color:white; -fx-border-width:3; -fx-border-insets: 5px; -fx-padding: 2px;");
        altin.setStyle("-fx-text-weight:bold; -fx-border-style:solid; -fx-border-color:white; -fx-border-width:3; -fx-border-insets: 5px; -fx-padding: 2px;");

        labelA.setText("A Oyuncusu\nRenk:KIRMIZI\n\n");
        labelB.setText("B Oyuncusu\nRenk:GRII\n\n");
        labelC.setText("C Oyuncusu\nRenk:MAVI\n\n");
        labelD.setText("D Oyuncusu\nRenk:SIYAH\n\n");
        altin.setText("Altin Renk:Sari\nGizli Altin Renk:Yesil\n");

        box.getChildren().add(labelA);
        box.getChildren().add(labelB);
        box.getChildren().add(labelC);
        box.getChildren().add(labelD);
        box.getChildren().add(altin);
    }


    public void gameBoard() {
        board = new Rectangle[sahaBoyutY][sahaBoyutX];
        for(int i=0;i<sahaBoyutY;i++){
            for(int j =0;j<sahaBoyutX;j++){
                board[i][j] = new Rectangle(KARE_BOYUT-1,KARE_BOYUT-1);
                board[i][j].setFill(Color.PINK);
                board[i][j].setX(j*(KARE_BOYUT));
                board[i][j].setY(i*(KARE_BOYUT));
                root.getChildren().add(board[i][j]);
            }
        }
    }


    public void oyunu_baslat(){
        OyunMasasi oyun=new OyunMasasi(sahaBoyutX, sahaBoyutY, Degerler.getAltinOrani(), Degerler.getGizliAltinOrani());
        OyuncuA a=new OyuncuA(Degerler.getBaslangicAltinMiktari(), oyun,Degerler.getaHamleMaaliyet(),Degerler.getaHedefBelirleme(),Degerler.getAdimSayisi());
        OyuncuB b=new OyuncuB(Degerler.getBaslangicAltinMiktari(), oyun,Degerler.getbHamleMaaliyet(),Degerler.getbHedefBelirleme(),Degerler.getAdimSayisi());
        OyuncuC c=new OyuncuC(Degerler.getBaslangicAltinMiktari(), oyun,Degerler.getcHamleMaaliyet(),Degerler.getcHedefBelirleme(),Degerler.getAdimSayisi());
        OyuncuD d=new OyuncuD(Degerler.getBaslangicAltinMiktari(), oyun,Degerler.getdHamleMaaliyet(),Degerler.getdHedefBelirleme(),Degerler.getAdimSayisi(),a,b,c);
			oyun.oyunculariAta(a, b, c, d);
			altin_deger(oyun.getMasa());
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				int sira=0;
				Boolean kontrol=true;
			    @Override
			    public void run() {
			        if(kontrol) {
			        	kontrol=oyun.oynat(sira%4);
				    	sira++;
			        }
			        else {
			        	this.cancel();
			        }
			    }
			};
			timer.scheduleAtFixedRate(task, 0, 100);
    }


    public void altin_deger(int altin_degerleri[][]){
        for(int i=0;i<sahaBoyutY;i++){
            for(int j =0;j<sahaBoyutX;j++){
                Text text = new Text();
                if(altin_degerleri[i][j] < 0){
                    text.setText(String.valueOf(altin_degerleri[i][j]*(-1)));
                }
                else if(altin_degerleri[i][j]>0){
                    text.setText(String.valueOf(altin_degerleri[i][j]));
                }
                else{
                    text.setText("");
                }
                text.setFill(Color.BLACK);
                text.setX(j*(KARE_BOYUT));
                text.setY(i*(KARE_BOYUT)+KARE_BOYUT-3);
                root.getChildren().add(text);
            }
        }
    }


    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        if(sahaBoyutY * KARE_BOYUT<310){
            scene = new Scene(root, sahaBoyutX * KARE_BOYUT + 129, 310, Color.AQUA);
        }
        else{
            scene = new Scene(root, sahaBoyutX * KARE_BOYUT + 129, sahaBoyutY * KARE_BOYUT, Color.AQUA);
        }
        box.setLayoutX(sahaBoyutX * KARE_BOYUT);

        scoreBoard();
        root.getChildren().add(box);

        primaryStage.setTitle("Oyun");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameBoard();
        oyunu_baslat();

    }
}