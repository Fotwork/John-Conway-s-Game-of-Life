package View;

import Utils.Ressort;
import Utils.TextFieldButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Edition
 */
public class Edition extends VBox{
    TextFieldButton sizeBoard;
    Button rebootBoard;
    TextFieldButton initAlea;

    public Edition(){
        super(2);
        this.setPadding(new Insets(3));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-border-color : black;-fx-background-color: mediumseagreen;");

        Label title = new Label("Edition du plateau");
        title.setStyle("-fx-font-weight: bold");

        Ressort ressort = new Ressort();

        this.sizeBoard = new TextFieldButton("Changer taille plateau");
        this.rebootBoard = new Button("Reinitialiser plateau");
        this.initAlea = new TextFieldButton("Rénitialisation aléatoire");
        
        this.getChildren().addAll(title,ressort,sizeBoard,rebootBoard,initAlea);

    }

    public TextFieldButton getSizeBoard(){
        return sizeBoard;
    }

    public Button getRebootBoard(){
        return rebootBoard;
    }

    public TextFieldButton getInitAlea(){
        return initAlea;
    }
}
