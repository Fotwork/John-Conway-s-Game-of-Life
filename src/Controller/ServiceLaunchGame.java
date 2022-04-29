package Controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * ServiceLaunchGame qui va avoir pour role de lancer la task TaskLaunchGame
 *  @author ymouacha
 *  @author smasson
 */

public class ServiceLaunchGame extends Service<Void> {
  
    private Controller controller;
  
    public ServiceLaunchGame (Controller controller) {
      super();
      this.controller = controller;
    }

    @Override
    protected Task<Void> createTask () {
        TaskLaunchGame task = new TaskLaunchGame(this.controller);
      return task;
    }

    /**
     * TaskLaunchGame qui va avoir pour role de lancer le jeu
     */
    private class TaskLaunchGame extends Task<Void> {

        private Controller controller;

        public TaskLaunchGame(Controller controller){
            this.controller = controller;
        }

        @Override
        protected Void call() throws Exception {
            
            //tant que le jeu n'est pas fini et que la task n'a pas été cancel alors on continue
            while(!(this.controller.getModel().isFinished() || isCancelled())){
                this.controller.getModel().computeTour();//on simule un tour dans le model
                for (Node node : this.controller.getView().getBoard().getChildren()) {
                    Rectangle rect = (Rectangle)node;
                    if(this.controller.getModel().getBoolTab()[GridPane.getRowIndex(rect)][GridPane.getColumnIndex(rect)].getValue()){
                        //On applique les modifications sur la vue en fonction du model
                        rect.setFill(Paint.valueOf("red"));
                    }
                    else{
                        rect.setFill(Paint.valueOf("white"));
                    }   
                }
                //On met en pause le thread pour qu'on puisse ensuite apercevoir les modifications dans la vue d'un tour a l'autre
                Thread.sleep(1000);
            }
            return null;
        }
    }
}
