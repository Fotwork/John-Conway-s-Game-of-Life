package Controleur;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Rectangle;

/**
 * ListenerScrollPirncipalBoard
 */
public class ListenerScrollPirncipalBoard implements EventHandler<ScrollEvent>{

    Controleur controleur;

    public ListenerScrollPirncipalBoard(Controleur controleur){
        this.controleur = controleur;
    }

    @Override
    public void handle(ScrollEvent event) {
        double newsize;
        double firstRectSize = ((Rectangle)this.controleur.getVue().getBoard().getChildren().get(0)).getWidth();
        if(event.getDeltaY()>0){
            newsize = firstRectSize+event.getDeltaY()-38;
        }
        else{
            newsize = firstRectSize+event.getDeltaY()+38;
        }
        this.controleur.getVue().getBoard().setRectSize(newsize);
        for (Node node : this.controleur.getVue().getBoard().getChildren()) {
            Rectangle rect = (Rectangle)node;
            rect.setWidth(newsize);
            rect.setHeight(newsize);
        }
    }
}
