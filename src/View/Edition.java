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
 * @author ymouacha
 */
public class Edition extends VBox{
    TextFieldButton sizeBoardTfButton;
    Button resetBoardButton;
    TextFieldButton initAleaTfButton;

    public Edition(){
        super(2);
        this.setPadding(new Insets(3));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: mediumseagreen;");

        //Label titre
        Label title = new Label("Edition du plateau");
        title.setStyle("-fx-font-weight: bold");


        //Composantes pour l'edition du jeu
        this.sizeBoardTfButton = new TextFieldButton("Changer taille plateau");
        this.resetBoardButton = new Button("Reinitialiser plateau");
        this.initAleaTfButton = new TextFieldButton("Rénitialisation aléatoire");
        
        //ressorts
        Ressort ressort = new Ressort();

        this.getChildren().addAll(title,ressort,sizeBoardTfButton,resetBoardButton,initAleaTfButton);

    }

    public TextFieldButton getSizeBoardTfButton(){
        return this.sizeBoardTfButton;
    }

    public Button getResetBoardButton(){
        return this.resetBoardButton;
    }

    public TextFieldButton getInitAleaTfButton(){
        return this.initAleaTfButton;
    }
}
