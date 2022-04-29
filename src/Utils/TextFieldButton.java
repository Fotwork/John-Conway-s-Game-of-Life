package Utils;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * TextFieldButton
 */
public class TextFieldButton extends HBox {

    TextField myTextField;
    Button myButton;

    public TextFieldButton(String text){
        super(2);
        this.myTextField = new TextField();
        this.myButton = new Button(text);

        this.getChildren().addAll(this.myTextField, this.myButton);
    }

    public Button getButton(){
        return myButton;
    }

    public TextField getTextField(){
        return myTextField;
    }
}
