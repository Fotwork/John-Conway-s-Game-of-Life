package View;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * Board
 */
public class Board extends GridPane {
    double rectSize;

    public Board(int size , double rectSize){
        super();
        this.setStyle("-fx-border-color: black;");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Rectangle rectangle = new Rectangle(rectSize,rectSize);
                rectangle.setStyle("-fx-fill: white; -fx-stroke: black;");
                rectangle.minWidth(5);
                rectangle.minHeight(5);
                this.add(rectangle, j, i);
            }
        }

        //action qui sert à modifier la coleur d'une case
        //s'applique surtout pour la zone tampon
        this.setOnMouseClicked(h -> {
            Rectangle rect = (Rectangle) h.getTarget();
            if(h.getButton() == MouseButton.PRIMARY){
                if(rect.getFill() == Paint.valueOf("white")){
                    rect.setFill(Paint.valueOf("red"));
                }
                else rect.setFill(Paint.valueOf("white"));
            }
        });
    }


    /**
     * Fonction qui recupere le rectangle dans notre plateau de jeu en fonction de ses coordonnées x et y.
     * @param x
     * @param y
     */
    public Rectangle getNodeByRowColumnIndex (int x, int y) {
        Rectangle result = null;
        ObservableList<Node> childrens = this.getChildren();
    
        for (Node node : childrens) {
            Rectangle rect = (Rectangle)node;
            if(GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                result = rect;
                break;
            }
        }
    
        return result;
    }

    /**
     * Cette fonction effache chaque bordudre de chaque rectangle de notre board.
     */
    public void eraseBorderRect(){
        for (Node node : this.getChildren()) {
            Rectangle rect = (Rectangle)node;
            rect.setStroke(Paint.valueOf("white"));
        }
    }

    /**
     * Cette fonction applique les bordudres de chaque rectangle de notre board.
     */
    public void setBorderRect(){
        for (Node node : this.getChildren()) {
            Rectangle rect = (Rectangle)node;
            rect.setStroke(Paint.valueOf("black"));
        }
    }

    public double getRectSize(){
        return this.rectSize;
    }

    public void setRectSize(double size){
        this.rectSize = size;
    }
}
