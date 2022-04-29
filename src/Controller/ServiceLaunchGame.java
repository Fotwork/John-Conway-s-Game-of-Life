package Controller;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * ServiceLaunchGame
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

    //Task qui va permettre de lancer notre jeu
    private class TaskLaunchGame extends Task<Void> {

        private Controller controller;

        public TaskLaunchGame(Controller controller){
            this.controller = controller;
        }

        @Override
        protected Void call() throws Exception {
            
            while(!this.controller.getModel().isFinished()){
                //tant que le jeu n'est pas fini et que la task n'a pas été cancel alors on continue
                if(isCancelled()){
                    break;
                }
                this.controller.getModel().computeTour();//on simule un tour dans le model
                for (Node node : this.controller.getView().getBoard().getChildren()) {
                    Rectangle rect = (Rectangle)node;
                    if(this.controller.getModel().getBoolTab()[GridPane.getRowIndex(rect)][GridPane.getColumnIndex(rect)].getValue()){
                        //on applique les modifications sur la vue en fonction du model
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
