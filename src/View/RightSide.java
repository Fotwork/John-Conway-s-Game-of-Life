package View;

import Utils.LabelComboString;
import Utils.Quit;
import Utils.Ressort;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * RightSide
 * @author ymouacha
 */
public class RightSide extends VBox {
    Board zoneTampon;
    LabelComboString labJeuxPredefinis;
    Button load;

    public RightSide(){
        super(5);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_RIGHT);
        this.setStyle("-fx-background-color:gold;-fx-border-color: black;");

        //ressort 
        Ressort ressort1 = new Ressort();
        Ressort ressort2 = new Ressort();

        //Zone Tampon
        HBox myHbox = new HBox();
        this.zoneTampon = new Board(10,5);
        myHbox.getChildren().addAll(ressort1,zoneTampon);

        //Combobox
        this.labJeuxPredefinis = new LabelComboString("Jeux prédéfinis");
        this.labJeuxPredefinis.getCombobox().getItems().addAll("Motif 1", "Motif 2");

        //boutton charger
        this.load = new Button("Charger");

        //boutton pour quitter la plateform
        Quit quit = new Quit();

        this.getChildren().addAll(myHbox,this.labJeuxPredefinis,this.load,ressort2,quit);
    }

    public Button getLoad(){
        return this.load;
    }

    public LabelComboString getPredefLabelCombo(){
        return this.labJeuxPredefinis;
    }

    public Board getZoneTompon(){
        return this.zoneTampon;
    }
}