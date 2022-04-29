package Controleur;

import Model.Cellule;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * ListenerClickPrincipalBoard
 */
public class ListenerClickPrincipalBoard implements EventHandler<MouseEvent> {
    Controleur controleur ;

    public ListenerClickPrincipalBoard(Controleur controleur){
        this.controleur = controleur;
    }

    @Override
    public void handle(MouseEvent event) {
        Rectangle node = (Rectangle) event.getTarget();
        int row = GridPane.getRowIndex(node);
        int column = GridPane.getColumnIndex(node);

        //Si l'action a été provoqué par le clique droit et le boutton shift
        if(event.getButton() == MouseButton.SECONDARY && event.isShiftDown()){
            this.controleur.setBoardPrincipalMotif(row, column);
        }
        else if(event.getButton() == MouseButton.SECONDARY){
            Platform.runLater(() -> {
                this.controleur.setZoneFromBoard(row,column);
            });
        }
        else if (event.getButton() == MouseButton.PRIMARY){
            if (node.getFill() == Paint.valueOf("white")){
                Platform.runLater(() -> {
                    node.setFill(Paint.valueOf("red"));
                });
                this.controleur.getModel().getCellTab()[row][column] = new Cellule(row, column);  
                this.controleur.getModel().getBoolTab()[row][column].setValue(true);                                                                                                                                                       
            }
            else{
                Platform.runLater(() -> {
                    node.setFill(Paint.valueOf("white"));
                });
                this.controleur.getModel().getCellTab()[row][column] = null;
                this.controleur.getModel().getBoolTab()[row][column].setValue(false);
            }      
        }
    }
}
