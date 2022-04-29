package Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


/**
 * EtiquetteCombo
 * @author ymouacha
 */
public class LabelComboString extends HBox{

    ComboBox<String> myCombobox;
    public LabelComboString(String text){
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

