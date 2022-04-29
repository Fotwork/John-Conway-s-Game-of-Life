package Controller;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Rectangle;

/**
 * ListenerScrollPirncipalBoard
 * @author ymouacha
 */
public class ListenerScrollMainBoard implements EventHandler<ScrollEvent>{

    Controller controleur;

    public ListenerScrollMainBoard(Controller controleur){
        this.controleur = controleur;
    }

    @Override
    public void handle(ScrollEvent event) {
        double newsize;
        double firstRectSize = ((Rectangle)this.controleur.getView().getBoard().getChildren().get(0)).getWidth();
        if(event.getDeltaY()>0){
            // par zoom/dezoom on fait +40/-40 sur le deltaY, c'est pour cela qu'on a fait une difference afin d'avoir un zoom plus agreable
            newsize = firstRectSize+event.getDeltaY()-38;
        }
        else{
            newsize = firstRectSize+event.getDeltaY()+38;
        }
        //Cette condition permet de minimiser la taille des réctangles à 5
        if(newsize<5){
            newsize =5;
        }
        this.controleur.getView().getBoard().setRectSize(newsize);
        //on parcours chaque rectangle de notre noeud et on applique la nouvelle taille pour chaque un d'entre eux
        for (Node node : this.controleur.getView().getBoard().getChildren()) {
            Rectangle rect = (Rectangle)node;
            rect.setWidth(newsize);
            rect.setHeight(newsize);
        }
    }
}
