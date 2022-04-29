package Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


/**
 * EtiquetteComboInteger
 */
public class EtiquetteComboInteger extends HBox{

    ComboBox<Integer> myCombobox;
    public EtiquetteComboInteger(String text){
        super(2);
        Label etiquette = new Label(text);
        this.myCombobox = new ComboBox<>();

        Ressort ressort = new Ressort();

        this.getChildren().addAll(etiquette,ressort,this.myCombobox);
    }

    public ComboBox<Integer> getCombobox(){
        return myCombobox;
    }
}
