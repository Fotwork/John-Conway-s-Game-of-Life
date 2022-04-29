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

    //fonction qui recupere le rectangle dans notre plateau de jeu en fonction de ses coordonnées x et y.
    public Rectangle getNodeByRowColumnIndex (final int x, final int y) {
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

    public void eraseBorderRect(){
        for (Node node : this.getChildren()) {
            Rectangle rect = (Rectangle)node;
            rect.setStroke(Paint.valueOf("white"));
        }
    }

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
