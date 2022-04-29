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
 * @author smasson
 * @author ymouacha
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
        //Si l'action a été provoqué par le clique droit
        else if(event.getButton() == MouseButton.SECONDARY){
                this.setZoneFromBoard(row,column);
        }
        //Si l'action a été provoqué par le clique gauche
        else if (event.getButton() == MouseButton.PRIMARY){
            if (node.getFill() == Paint.valueOf("white")){
                
                Platform.runLater(()-> {
                    node.setFill(Paint.valueOf("red"));
                });
                this.controller.getModel().getCellTab()[row][column] = new Cell(row, column);  
                this.controller.getModel().getBoolTab()[row][column].setValue(true);                                                                                                                                                       
            }
            else{
                Platform.runLater(()-> {
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
            //on recupere les coordonnees en x et en y de chaque rectangle de la zone tampon
            int rectRowTampon = GridPane.getRowIndex(rect), rectColumnTampon = GridPane.getColumnIndex(rect);
            //on effectue la translation en fonction des coordonnées de chaque rectangle de la zone tampon.
            //je vous l'accorde ce n'est pas tres commode...
            int newRow = rowClick + rectRowTampon, newColumn = columnClick + rectColumnTampon;

            //il faut gerer ici les bords de notre board pour pas déclancher un NullPointerException
            if(!(newRow >= this.controller.getModel().getBoardSize().getValue() || newColumn >= this.controller.getModel().getBoardSize().getValue() || newRow<0 || newColumn<0)){
                if(rect.getFill() == Paint.valueOf("white")){
                    if(this.controller.getModel().getCellTab()[newRow][newColumn] != null){
                        this.controller.getModel().getCellTab()[newRow][newColumn] = null;
                    }
                    //Comme on a pas le droit de modifier l'interface depuis un autre thread que le JaFXAT on est oblige de passer par un Platform.runlater
                    Platform.runLater(()-> {
                        this.controller.getView().getBoard().getNodeByRowColumnIndex(newRow,newColumn).setFill(Paint.valueOf("white"));
                    });
                    this.controller.getModel().getBoolTab()[newRow][newColumn].setValue(false);
                }
                else {
                    Platform.runLater(()-> {
                        this.controller.getView().getBoard().getNodeByRowColumnIndex(newRow,newColumn).setFill(Paint.valueOf("red"));
                    });
                    this.controller.getModel().getCellTab()[newRow][newColumn] = new Cell(newRow, newColumn);;
                    this.controller.getModel().getBoolTab()[newRow][newColumn].setValue(true);
                }
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
                Rectangle rectBoard = this.controller.getView().getBoard().getNodeByRowColumnIndex(i, j); //on recupere le rectangle du board en fonction de la position  i et j.
                //on fait une translation en faisant la difference avec les coordonnées au moment du click et on recupere le cellule correspondante de la zone tampon.
                Rectangle rectTampon = this.controller.getView().getRightSide().getZoneTompon().getNodeByRowColumnIndex(i - rowClick , j - columnClick); 
                //il faut gerer ici les bords de notre board principal pour pas declancher un NullPointerException
                if(!(i>= this.controller.getModel().getBoardSize().getValue() || j>= this.controller.getModel().getBoardSize().getValue() || i<0 || j<0)){
                    if(rectBoard.getFill() == Paint.valueOf("white")){
                        Platform.runLater(()-> {
                            rectTampon.setFill(Paint.valueOf("white"));
                        });
                    }
                    else {
                        Platform.runLater(()-> {
                            rectTampon.setFill(Paint.valueOf("red"));
                        });
                    }
                }
            }
        }
    }
}
