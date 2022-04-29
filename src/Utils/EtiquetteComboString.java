package Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


/**
 * EtiquetteCombo
 */
public class EtiquetteComboString extends HBox{

    ComboBox<String> myCombobox;
    public EtiquetteComboString(String text){
        super(2);
        Label etiquette = new Label(text);
        this.myCombobox = new ComboBox<>();

        Ressort ressort = new Ressort();

        this.getChildren().addAll(etiquette,ressort,this.myCombobox);
    }

    public ComboBox<String> getCombobox(){
        return myCombobox;
    }
}

