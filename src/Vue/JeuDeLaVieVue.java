package Vue;

import java.util.Optional;

import Utils.Ressort;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * JeuDeLaVieVue
 */
public class JeuDeLaVieVue extends HBox{
    Leftside ls;
    Board board;
    RightSide rs;

    public JeuDeLaVieVue(){
        super(5);
        this.setPadding(new Insets(5));
        this.ls = new Leftside();
        VBox myVBox = new VBox();
        Ressort ressort1 = new Ressort();
        this.board = new Board(100,5);
        this.board.setMinSize(200, 200);
        myVBox.getChildren().addAll(this.board,ressort1);
        this.rs = new RightSide();
        Ressort ressort2 = new Ressort();
        Ressort ressort3 = new Ressort();
        this.getChildren().addAll(this.ls,ressort3, myVBox,ressort2, this.rs);
    }

    
    public Boolean showConfirmation(String title, String header, String content , ActionEvent h) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if ( option.get() == ButtonType.CANCEL ) {
            return false;
        }
        return true; 
    }

    public void showAlert(String title,String header, String content ) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}


    public Leftside getLeftSide(){
        return this.ls;
    }

    public RightSide getRightSide(){
        return this.rs;
    }


    public Board getBoard(){
        return this.board;
    }

    public void setBoard(Board board){
        VBox vbox = new VBox();
        Ressort ressort = new Ressort();
        vbox.getChildren().addAll(board,ressort);
        this.getChildren().remove(2);
        this.getChildren().add(2, vbox);
        this.board = board;
    }
}
