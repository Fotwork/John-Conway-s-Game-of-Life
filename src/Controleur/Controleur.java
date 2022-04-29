package Controleur;

import Model.Cellule;
import Model.Factory;
import Model.JeuDeLaVie;
import Vue.Board;
import Vue.JeuDeLaVieVue;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * Controleur
 */
public class Controleur {

    JeuDeLaVie model;
    JeuDeLaVieVue vue;

    Service<Void> calculateTaskInitProbaBoard;
    Service<Void> calculateTaskLaunchGame;

    public Controleur(JeuDeLaVieVue vue){
        super();
        this.vue = vue;
        this.model = new JeuDeLaVie(100);
        
        this.calculateTaskLaunchGame = new ServiceLaunchGame(this);

        this.computeVue();
    }

    public JeuDeLaVie getModel(){
        return this.model;
    }

    public JeuDeLaVieVue getVue(){
        return this.vue;
    }

    public void computeVue(){

        //LEFTSIDE\\


        //PARAMETRES : ACTION SUR LES FENETRES

        this.getVue().getLeftSide().getParameteres().getMortAsphyxie().getCombobox().setOnAction(h -> {
            if(this.getVue().getLeftSide().getParameteres().getMortAsphyxie().getCombobox().getValue()<this.getVue().getLeftSide().getParameteres().getMortSolitude().getCombobox().getValue()){
                this.getVue().showAlert("Erreur Mort ashpyxie", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir MortAsphyxie<MortIsolation");
            }
            else{
                this.getModel().getMortAs().setValue(this.getVue().getLeftSide().getParameteres().getMortAsphyxie().getCombobox().getValue());
            }
        });

        
        this.getVue().getLeftSide().getParameteres().getMortSolitude().getCombobox().setOnAction(h -> {
            if(this.getVue().getLeftSide().getParameteres().getMortAsphyxie().getCombobox().getValue()<this.getVue().getLeftSide().getParameteres().getMortSolitude().getCombobox().getValue()){
                this.getVue().showAlert("Erreur mort isolation", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir MortAsphyxie<MortIsolation");
            }
            else{
                this.getModel().getMortIs().setValue(this.getVue().getLeftSide().getParameteres().getMortSolitude().getCombobox().getValue());
            }
        });

        
        this.getVue().getLeftSide().getParameteres().getVieMax().getCombobox().setOnAction(h -> {
            if(this.getVue().getLeftSide().getParameteres().getVieMax().getCombobox().getValue()<this.getVue().getLeftSide().getParameteres().getVieMin().getCombobox().getValue()){
                this.getVue().showAlert("Erreur vie max", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir VieMin>VieMax");
            }
            else{
                this.getModel().getVieMax().setValue(this.getVue().getLeftSide().getParameteres().getVieMax().getCombobox().getValue());
            }
        });

        
        this.getVue().getLeftSide().getParameteres().getVieMin().getCombobox().setOnAction(h -> {
            if(this.getVue().getLeftSide().getParameteres().getVieMax().getCombobox().getValue()<this.getVue().getLeftSide().getParameteres().getVieMin().getCombobox().getValue()){
                this.getVue().showAlert("Erreur vie min", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir VieMin>VieMax");
            }
            else{
                this.getModel().getVieMin().setValue(this.getVue().getLeftSide().getParameteres().getVieMin().getCombobox().getValue());
            }
        });

        //PARAMETRES : bindings entre les comboBox et les paramètres de notre jeu.

        // this.getModel().getMortAs().bind(this.getVue().getLeftSide().getParameteres().getMortAsphyxie().getCombobox().valueProperty());
        // this.getModel().getMortIs().bind(this.getVue().getLeftSide().getParameteres().getMortSolitude().getCombobox().valueProperty());
        // this.getModel().getVieMin().bind(this.getVue().getLeftSide().getParameteres().getVieMin().getCombobox().valueProperty());
        // this.getModel().getVieMax().bind(this.getVue().getLeftSide().getParameteres().getVieMax().getCombobox().valueProperty());

        //EDITION : evenement sur le button qui permet de changer la taille du plateau principale.
        this.vue.getLeftSide().getEdition().getSizeBoard().getButton().setOnAction(h->{
            String textSize = this.vue.getLeftSide().getEdition().getSizeBoard().getTextField().textProperty().getValue();
            if(this.vue.getLeftSide().getEdition().getSizeBoard().getTextField().textProperty().getValue().length() == 0){
                this.getVue().showAlert("Erreur : Modificaiton taille du plateau", "", "Vous devez au moins rentrer une valeur !");
            }
            else if(!textSize.matches("[+]?\\d*(\\.\\d+)?")){
                    this.getVue().showAlert("Erreur : Modificaiton taille du plateau", "Vous devez rentrer une valeur valide ! ", "Rentrez une taille strictement supérieur a 0.");
                }
            else{
                int newsize = Integer.parseInt(textSize);
                if(newsize != this.getModel().getBoardSize().getValue()){
                    this.getModel().setSize(newsize);
                    Board newBoard = new Board(newsize, this.getVue().getBoard().getRectSize());
                    newBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, new ListenerClickPrincipalBoard(this) );
                    newBoard.addEventHandler(ScrollEvent.SCROLL, new ListenerScrollPirncipalBoard(this));
                    this.getVue().setBoard(newBoard);
                }
            }   
        });

        //EDITION : evenement sur le button qui permet de réinitialiser le plateau de jeu.
        this.vue.getLeftSide().getEdition().getRebootBoard().setOnAction(h->{ 
            if(this.getVue().showConfirmation("Confirmation : réinitialisation du plateau de jeu", "Voulez vous vraiment réinitialiser le plateu de jeu?", "", h)){
                this.calculateTaskInitProbaBoard= new ServiceInitProbaBoard(this, 0 );
                this.calculateTaskInitProbaBoard.setOnSucceeded(ev -> {
                    this.calculateTaskInitProbaBoard.reset();
                });
                this.calculateTaskInitProbaBoard.start();
            }
        });
        
        //EDITION : ecouteur sur le button qui permet de generer un plateau de jeu avec une certaine probabilité p.
        this.vue.getLeftSide().getEdition().getInitAlea().getButton().setOnAction(h->{
            String textProba = this.vue.getLeftSide().getEdition().getInitAlea().getTextField().textProperty().getValue();
            if(this.vue.getLeftSide().getEdition().getInitAlea().getTextField().textProperty().getValue().length() == 0){
                this.getVue().showAlert("Erreur : Initialisation du plateau avec une probabilité", "Veuillez entrer une valeur de probabilité conforme.", "Vous devez au moins rentrer une valeur !");
            }
            else if(!textProba.matches("[+]?\\d*(\\.\\d+)?")){
                    this.getVue().showAlert("Erreur : Modificaiton taille du plateau", "Vous devez rentrer une valeur valide ! ", "Rentrez une probabilité supérieur ou égal a 0.");
                }
            else{
                this.calculateTaskInitProbaBoard = new ServiceInitProbaBoard(this, Integer.parseInt(this.vue.getLeftSide().getEdition().getInitAlea().getTextField().textProperty().getValue()));
                this.calculateTaskInitProbaBoard.setOnSucceeded(eh -> {
                    this.calculateTaskInitProbaBoard.reset();
                });
                this.calculateTaskInitProbaBoard.start();
            }
        });

        this.calculateTaskLaunchGame.setOnCancelled(h -> {
            this.calculateTaskLaunchGame.reset();
        });
        this.calculateTaskLaunchGame.setOnSucceeded(h -> {
            this.calculateTaskLaunchGame.reset();
            this.vue.getLeftSide().getPlay().setText("Play");
            this.getVue().getBoard().setDisable(false);
            this.getVue().getLeftSide().getEdition().setDisable(false);
            this.getVue().getBoard().setBorderRect();
        });

        //BOUTTON PLAY : ecouteur sur le boutton qui permet de lancer/stoper notre jeu
        this.vue.getLeftSide().getPlay().setOnAction(h -> {
            if(this.calculateTaskLaunchGame.isRunning()){
                this.vue.getLeftSide().getPlay().setText("Play");
                this.getVue().getBoard().setDisable(false);
                this.getVue().getLeftSide().getEdition().setDisable(false);
                this.calculateTaskLaunchGame.cancel();
                this.getVue().getBoard().setBorderRect();
            }
            else {
                this.vue.getLeftSide().getPlay().setText("Pause");
                this.getVue().getBoard().setDisable(true);
                this.getVue().getLeftSide().getEdition().setDisable(true);
                this.getVue().getBoard().eraseBorderRect();
                this.calculateTaskLaunchGame.start();
            }
        });

        //BOARD : plateau principal\\
        
        //BOARD : reset de notre service qui permet de lancer notre jeu si ce dernier venait à être fini/annulé.

        //BOARD : ecouteur sur les différents click du board
        this.getVue().getBoard().addEventHandler(MouseEvent.MOUSE_CLICKED, new ListenerClickPrincipalBoard(this) );

        //BOARD : ecouteur sur le scroll du board qui permet de zoomer/dezoomer sur ce dernier.
        this.getVue().getBoard().addEventHandler(ScrollEvent.SCROLL, new ListenerScrollPirncipalBoard(this));

        //RIGHTSIDE\\

        //Bind sur le label nbTour
        CompteurStringBinding csb = new CompteurStringBinding(this.getModel().getTour());
        this.getVue().getLeftSide().getLabelTour().textProperty().bind(csb);;

        //BOUTTON LOAD : ecouteur qui permet de charger notre motif sur notre zone tampon
        this.vue.getRightSide().getLoad().setOnAction(h -> {
        Boolean[][] motif = null;
        //switch case en fonction du motif selectionné, on appele notre motif prédefini qui se situe dans notre class Factory et on l'applique sur notre zone tampon.
        switch (this.vue.getRightSide().getPredefCombo().getCombobox().getValue()) {
            case "Motif 1":
                motif = Factory.creatMotif1();
                this.loadMotifZoneTampon(motif);
                break;

            case "Motif 2":
                motif = Factory.creatMotif2();
                this.loadMotifZoneTampon(motif);
                break;
        
            default:
                break;
        }
        });
    }

    public void setZoneFromBoard(int rowClick , int columnClick){
        for (int i = rowClick; i < rowClick + 10; i++) {
            for (int j = columnClick; j < columnClick + 10; j++) {
                Rectangle rectBoard = this.getVue().getBoard().getNodeByRowColumnIndex(i, j);
                Rectangle rectTampon = this.getVue().getRightSide().getZoneTompon().getNodeByRowColumnIndex(i - rowClick , j - columnClick);
                if(i>= this.getModel().getBoardSize().getValue() || j>= this.getModel().getBoardSize().getValue()){
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

    public void setBoardPrincipalMotif(int rowClick , int columnClick){
        for (Node node : this.getVue().getRightSide().getZoneTompon().getChildren()) {
            Rectangle rect = (Rectangle)node;
            int rectRow = GridPane.getRowIndex(rect), rectColumn = GridPane.getColumnIndex(rect);
            if(rect.getFill() == Paint.valueOf("white")){
                if(this.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] != null){
                    this.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] = null;
                }
                this.getVue().getBoard().getNodeByRowColumnIndex(rowClick + rectRow, columnClick + rectColumn).setFill(Paint.valueOf("white"));
                this.getModel().getBoolTab()[rowClick + rectRow][columnClick + rectColumn].setValue(false);
            }
            else {
                this.getVue().getBoard().getNodeByRowColumnIndex(rowClick + rectRow, columnClick + rectColumn).setFill(Paint.valueOf("red"));
                this.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] = new Cellule(rowClick + rectRow, columnClick + rectColumn);;
                this.getModel().getBoolTab()[rowClick + rectRow][columnClick + rectColumn].setValue(true);
            }
        }
    }

    public void loadMotifZoneTampon(Boolean[][] motif){
        for (Node node : this.getVue().getRightSide().getZoneTompon().getChildren()) {
            Rectangle rect = (Rectangle)node;
            int rowRect = GridPane.getRowIndex(rect), columnRect = GridPane.getColumnIndex(rect);
            if(motif[rowRect][columnRect]){
                Platform.runLater(() -> {
                    rect.setStyle("-fx-fill: red; -fx-stroke: black;");
                });
            }
            else{
                Platform.runLater(() -> {
                    rect.setStyle("-fx-fill: white; -fx-stroke: black;"); 
                });
            }   
        }
    }

      //Task qui initialise le board avec une probabilité p.
      private static class TaskInitProbaBoard extends Task<Void> {

        private Controleur controleur;
        private int proba;
    
        public TaskInitProbaBoard(Controleur controleur, int proba){
            super();
            this.controleur = controleur;
            this.proba = proba;
        }
    
        @Override
        protected Void call() throws Exception {
            this.controleur.getModel().initProba(this.proba);
            for (Node node : this.controleur.getVue().getBoard().getChildren()) {
                Rectangle rect = (Rectangle)node;
                if(this.controleur.getModel().getBoolTab()[GridPane.getRowIndex(rect)][GridPane.getColumnIndex(rect)].getValue()){
                    Platform.runLater(() -> {
                        rect.setFill(Paint.valueOf("red"));
                    });
                }
                else{
                    Platform.runLater(() -> {
                        rect.setFill(Paint.valueOf("white")); 
                    });
                }   
            }
            return null;
        }
    }

    private class ServiceInitProbaBoard extends Service<Void> {
  
        private Controleur controleur;
        private int proba;

      
        public ServiceInitProbaBoard (Controleur controleur, int proba) {
            super();
            this.controleur = controleur;
            this.proba = proba;
          }

        @Override 
        protected Task<Void> createTask () {
            TaskInitProbaBoard task = new TaskInitProbaBoard(this.controleur, this.proba);
            return task;
        }
    }

    //Task qui lance le jeu
    private class TaskLaunchGame extends Task<Void> {

        private Controleur controleur;

        public TaskLaunchGame(Controleur control){
            this.controleur = control;
        }

        @Override
        protected Void call() throws Exception {
            do{
                this.controleur.getModel().computeTour();
                for (Node node : this.controleur.getVue().getBoard().getChildren()) {
                    Rectangle rect = (Rectangle)node;
                    if(this.controleur.getModel().getBoolTab()[GridPane.getRowIndex(rect)][GridPane.getColumnIndex(rect)].getValue()){
                        Platform.runLater(() -> {
                            rect.setStyle("-fx-fill: red; -fx-stroke: white;");
                        });
                    }
                    else{
                        Platform.runLater(() -> {
                            rect.setStyle("-fx-fill: white; -fx-stroke: white;");
                        });
                    }   
                }
                Thread.sleep(1000);
                if(isCancelled()){
                    break;
                }
            }while(!this.controleur.getModel().isFinished());
            return null;
        }
    }

    private class ServiceLaunchGame extends Service<Void> {
  
        private Controleur controleur;
      
        public ServiceLaunchGame (Controleur controleur) {
          super();
          this.controleur = controleur;
        }

        @Override 
        protected Task<Void> createTask () {
            TaskLaunchGame task = new TaskLaunchGame(this.controleur);
          return task;
        }
    }
}