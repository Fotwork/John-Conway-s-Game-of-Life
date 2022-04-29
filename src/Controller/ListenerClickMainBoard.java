package Controller;

import Model.Cell;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * ListenerClickPrincipalBoard
 */
public class ListenerClickMainBoard implements EventHandler<MouseEvent> {
    Controller controller ;

    public ListenerClickMainBoard(Controller controller){
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent event) {
        Rectangle node = (Rectangle) event.getTarget();
        int row = GridPane.getRowIndex(node);
        int column = GridPane.getColumnIndex(node);

        //Si l'action a été provoqué par le clique droit et le boutton shift
        if(event.getButton() == MouseButton.SECONDARY && event.isShiftDown()){
            this.setMainBoardMotif(row, column);
        }
        else if(event.getButton() == MouseButton.SECONDARY){
            Platform.runLater(() -> {
                this.setZoneFromBoard(row,column);
            });
        }
        else if (event.getButton() == MouseButton.PRIMARY){
            if (node.getFill() == Paint.valueOf("white")){
                Platform.runLater(() -> {
                    node.setFill(Paint.valueOf("red"));
                });
                this.controller.getModel().getCellTab()[row][column] = new Cell(row, column);  
                this.controller.getModel().getBoolTab()[row][column].setValue(true);                                                                                                                                                       
            }
            else{
                Platform.runLater(() -> {
                    node.setFill(Paint.valueOf("white"));
                });
                this.controller.getModel().getCellTab()[row][column] = null;
                this.controller.getModel().getBoolTab()[row][column].setValue(false);
            }      
        }
    }

    /**
     * Cette fonction permet de faire un copier de la coller de notre zone tampon vers notre plateau principal.
     * Elle sera appelee par l'ecouteur ListenerClickMainBoard.
     * @param rowClick Ce paramètre correspond à la coordonnée en x du click durant l'action.
     * @param columnClick Ce paramètre correspond à la coordonnée en y du click durant l'action.
     */ 
    public void setMainBoardMotif(int rowClick , int columnClick){
        for (Node node : this.controller.getView().getRightSide().getZoneTompon().getChildren()) {
            Rectangle rect = (Rectangle)node;
            int rectRow = GridPane.getRowIndex(rect), rectColumn = GridPane.getColumnIndex(rect);
            if(rect.getFill() == Paint.valueOf("white")){
                if(this.controller.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] != null){
                    this.controller.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] = null;
                }
                this.controller.getView().getBoard().getNodeByRowColumnIndex(rowClick + rectRow, columnClick + rectColumn).setFill(Paint.valueOf("white"));
                this.controller.getModel().getBoolTab()[rowClick + rectRow][columnClick + rectColumn].setValue(false);
            }
            else {
                this.controller.getView().getBoard().getNodeByRowColumnIndex(rowClick + rectRow, columnClick + rectColumn).setFill(Paint.valueOf("red"));
                this.controller.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] = new Cell(rowClick + rectRow, columnClick + rectColumn);;
                this.controller.getModel().getBoolTab()[rowClick + rectRow][columnClick + rectColumn].setValue(true);
            }
        }
    }

    /**
     * Cette fonction permet de faire un copier/coller de notre plateau principal vers la zone tampon.
     * Elle sera appelee par l'ecouteur ListenerClickMainBoard.
     * @param proba
     * @param rowClick Ce paramètre correspond à la coordonnée en x du click durant l'action.
     * @param columnClick Ce paramètre correspond à la coordonnée en y du click durant l'action.
     */ 
    public void setZoneFromBoard(int rowClick , int columnClick){
        for (int i = rowClick; i < rowClick + 10; i++) {
            for (int j = columnClick; j < columnClick + 10; j++) {
                Rectangle rectBoard = this.controller.getView().getBoard().getNodeByRowColumnIndex(i, j);
                Rectangle rectTampon = this.controller.getView().getRightSide().getZoneTompon().getNodeByRowColumnIndex(i - rowClick , j - columnClick);
                if(i>= this.controller.getModel().getBoardSize().getValue() || j>= this.controller.getModel().getBoardSize().getValue()){
                    rectTampon.setFill(Paint.valueOf("white"));
                }
                else{
                    if(rectBoard.getFill() == Paint.valueOf("white")){
                        rectTampon.setFill(Paint.valueOf("white"));
                    }
                    else rectTampon.setFill(Paint.valueOf("red"));
                }
            }
        }
    }
}
