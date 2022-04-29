package Controller;

import Model.Cell;
import Model.Factory;
import Model.JeuDeLaVie;
import View.Board;
import View.JeuDeLaVieView;
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
public class Controller {

    JeuDeLaVie model;
    JeuDeLaVieView view;

    Service<Void> calculateTaskLaunchGame;

    public Controller(JeuDeLaVieView view){
        super();
        this.view = view;
        this.model = new JeuDeLaVie(100);
        
        this.calculateTaskLaunchGame = new ServiceLaunchGame(this);

        this.computeVue();
    }

    public JeuDeLaVie getModel(){
        return this.model;
    }

    public JeuDeLaVieView getView(){
        return this.view;
    }

    public void computeVue(){

        //LEFTSIDE\\


        //PARAMETRES : ACTION SUR LES FENETRES

        this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().setOnAction(h -> {
            if(this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().getValue()){
                this.getView().showAlert("Erreur Mort ashpyxie", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir MortAsphyxie<MortIsolation");
            }
            else{
                this.getModel().getMortAs().setValue(this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().getValue());
            }
        });

        
        this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().setOnAction(h -> {
            if(this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().getValue()){
                this.getView().showAlert("Erreur mort isolation", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir MortAsphyxie<MortIsolation");
            }
            else{
                this.getModel().getMortIs().setValue(this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().getValue());
            }
        });

        
        this.getView().getLeftSide().getSettings().getVieMax().getCombobox().setOnAction(h -> {
            if(this.getView().getLeftSide().getSettings().getVieMax().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getVieMin().getCombobox().getValue()){
                this.getView().showAlert("Erreur vie max", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir VieMin>VieMax");
            }
            else{
                this.getModel().getVieMax().setValue(this.getView().getLeftSide().getSettings().getVieMax().getCombobox().getValue());
            }
        });

        
        this.getView().getLeftSide().getSettings().getVieMin().getCombobox().setOnAction(h -> {
            if(this.getView().getLeftSide().getSettings().getVieMax().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getVieMin().getCombobox().getValue()){
                this.getView().showAlert("Erreur vie min", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir VieMin>VieMax");
            }
            else{
                this.getModel().getVieMin().setValue(this.getView().getLeftSide().getSettings().getVieMin().getCombobox().getValue());
            }
        });

        //PARAMETRES : bindings entre les comboBox et les paramètres de notre jeu.

        // this.getModel().getMortAs().bind(this.getView().getLeftSide().getParameteres().getMortAsphyxie().getCombobox().valueProperty());
        // this.getModel().getMortIs().bind(this.getView().getLeftSide().getParameteres().getMortSolitude().getCombobox().valueProperty());
        // this.getModel().getVieMin().bind(this.getView().getLeftSide().getParameteres().getVieMin().getCombobox().valueProperty());
        // this.getModel().getVieMax().bind(this.getView().getLeftSide().getParameteres().getVieMax().getCombobox().valueProperty());

        //EDITION : evenement sur le button qui permet de changer la taille du plateau principale.
        this.getView().getLeftSide().getEdition().getSizeBoard().getButton().setOnAction(h->{
            String textSize = this.getView().getLeftSide().getEdition().getSizeBoard().getTextField().textProperty().getValue();
            if(this.getView().getLeftSide().getEdition().getSizeBoard().getTextField().textProperty().getValue().length() == 0){
                this.getView().showAlert("Erreur : Modificaiton taille du plateau", "", "Vous devez au moins rentrer une valeur !");
            }
            else if(!textSize.matches("[+]?\\d*(\\.\\d+)?")){
                    this.getView().showAlert("Erreur : Modificaiton taille du plateau", "Vous devez rentrer une valeur valide ! ", "Rentrez une taille strictement supérieur a 0.");
                }
            else{
                int newsize = Integer.parseInt(textSize);
                if(newsize != this.getModel().getBoardSize().getValue()){
                    this.getModel().setSize(newsize);
                    Board newBoard = new Board(newsize, this.getView().getBoard().getRectSize());
                    newBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, new ListenerClickMainBoard(this) );
                    newBoard.addEventHandler(ScrollEvent.SCROLL, new ListenerScrollMainBoard(this));
                    this.getView().setBoard(newBoard);
                }
            }   
        });

        //EDITION : evenement sur le button qui permet de réinitialiser le plateau de jeu.
        this.getView().getLeftSide().getEdition().getRebootBoard().setOnAction(h->{ 
            if(this.getView().showConfirmation("Confirmation : réinitialisation du plateau de jeu", "Voulez vous vraiment réinitialiser le plateu de jeu?", "", h)){
                this.initBoardProba(0);
            }
        });
        
        //EDITION : ecouteur sur le button qui permet de generer un plateau de jeu avec une certaine probabilité p.
        this.getView().getLeftSide().getEdition().getInitAlea().getButton().setOnAction(h->{
            String textProba = this.getView().getLeftSide().getEdition().getInitAlea().getTextField().textProperty().getValue();
            if(this.getView().getLeftSide().getEdition().getInitAlea().getTextField().textProperty().getValue().length() == 0){
                this.getView().showAlert("Erreur : Initialisation du plateau avec une probabilité", "Veuillez entrer une valeur de probabilité conforme.", "Vous devez au moins rentrer une valeur !");
            }
            else if(!textProba.matches("[+]?\\d*(\\.\\d+)?")){
                    this.getView().showAlert("Erreur : Modificaiton taille du plateau", "Vous devez rentrer une valeur valide ! ", "Rentrez une probabilité supérieur ou égal a 0.");
                }
            else{
                Integer proba = Integer.parseInt(textProba);
                this.getModel().initProba(proba);
                this.initBoardProba(proba);
            }
        });


        //Paramètre liée a mon service launchGame après un cancel ou un succès
        this.calculateTaskLaunchGame.setOnCancelled(h -> {
            this.calculateTaskLaunchGame.reset();
        });

        this.calculateTaskLaunchGame.setOnSucceeded(h -> {
            this.calculateTaskLaunchGame.reset();
            this.getModel().resetBoard();
            this.getView().getLeftSide().getPlay().setText("Play");
            this.getView().getBoard().setDisable(false);
            this.getView().getLeftSide().getEdition().setDisable(false);
            this.getView().getBoard().setBorderRect();
        });

        //BOUTTON PLAY : ecouteur sur le boutton qui permet de lancer/stoper notre jeu
        this.getView().getLeftSide().getPlay().setOnAction(h -> {
            if(this.calculateTaskLaunchGame.isRunning()){
                this.getView().getLeftSide().getPlay().setText("Play");
                this.getView().getBoard().setDisable(false);
                this.getView().getLeftSide().getEdition().setDisable(false);
                this.calculateTaskLaunchGame.cancel();
                this.getView().getBoard().setBorderRect();
            }
            else {
                this.getView().getLeftSide().getPlay().setText("Pause");
                this.getView().getBoard().setDisable(true);
                this.getView().getLeftSide().getEdition().setDisable(true);
                this.getView().getBoard().eraseBorderRect();
                this.calculateTaskLaunchGame.start();
            }
        });

        //BOARD : plateau principal\\
        

        //BOARD : ecouteur sur les différents click du board
        this.getView().getBoard().addEventHandler(MouseEvent.MOUSE_CLICKED, new ListenerClickMainBoard(this) );

        //BOARD : ecouteur sur le scroll du board qui permet de zoomer/dezoomer sur ce dernier.
        this.getView().getBoard().addEventHandler(ScrollEvent.SCROLL, new ListenerScrollMainBoard(this));


        //RIGHTSIDE\\

        //BOUTTON LOAD : ecouteur qui permet de charger notre motif sur notre zone tampon
        this.getView().getRightSide().getLoad().setOnAction(h -> {
        Boolean[][] motif = null;
        //switch case en fonction du motif selectionné, on appele notre motif prédefini qui se situe dans notre class Factory et on l'applique sur notre zone tampon.
        switch (this.getView().getRightSide().getPredefCombo().getCombobox().getValue()) {
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

        //LABEL TOUR : Bind entre la nombre de tour du model et celle de la vue
        CounterStringBinding csb = new CounterStringBinding(this.getModel().getTour());
        this.getView().getLeftSide().getLabelTour().textProperty().bind(csb);;
    }

    public void initBoardProba(double proba){
        this.getModel().initProba(proba);
        for (Node node : this.getView().getBoard().getChildren()) {
            Rectangle rect = (Rectangle)node;
            if(this.getModel().getBoolTab()[GridPane.getRowIndex(rect)][GridPane.getColumnIndex(rect)].getValue()){
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
    }

    public void setZoneFromBoard(int rowClick , int columnClick){
        for (int i = rowClick; i < rowClick + 10; i++) {
            for (int j = columnClick; j < columnClick + 10; j++) {
                Rectangle rectBoard = this.getView().getBoard().getNodeByRowColumnIndex(i, j);
                Rectangle rectTampon = this.getView().getRightSide().getZoneTompon().getNodeByRowColumnIndex(i - rowClick , j - columnClick);
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
        for (Node node : this.getView().getRightSide().getZoneTompon().getChildren()) {
            Rectangle rect = (Rectangle)node;
            int rectRow = GridPane.getRowIndex(rect), rectColumn = GridPane.getColumnIndex(rect);
            if(rect.getFill() == Paint.valueOf("white")){
                if(this.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] != null){
                    this.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] = null;
                }
                this.getView().getBoard().getNodeByRowColumnIndex(rowClick + rectRow, columnClick + rectColumn).setFill(Paint.valueOf("white"));
                this.getModel().getBoolTab()[rowClick + rectRow][columnClick + rectColumn].setValue(false);
            }
            else {
                this.getView().getBoard().getNodeByRowColumnIndex(rowClick + rectRow, columnClick + rectColumn).setFill(Paint.valueOf("red"));
                this.getModel().getCellTab()[rowClick + rectRow][columnClick + rectColumn] = new Cell(rowClick + rectRow, columnClick + rectColumn);;
                this.getModel().getBoolTab()[rowClick + rectRow][columnClick + rectColumn].setValue(true);
            }
        }
    }

    public void loadMotifZoneTampon(Boolean[][] motif){
        for (Node node : this.getView().getRightSide().getZoneTompon().getChildren()) {
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

    //Task qui lance le jeu
    private class TaskLaunchGame extends Task<Void> {

        private Controller controleur;

        public TaskLaunchGame(Controller control){
            this.controleur = control;
        }

        @Override
        protected Void call() throws Exception {
            while(!this.controleur.getModel().isFinished()){
                if(isCancelled()){
                    break;
                }
                this.controleur.getModel().computeTour();
                for (Node node : this.controleur.getView().getBoard().getChildren()) {
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
                Thread.sleep(1000);
            }
            return null;
        }
    }

    //service qui permet de relancer la task launchgame autant de fois qu'on le souhaite
    private class ServiceLaunchGame extends Service<Void> {
  
        private Controller controleur;
      
        public ServiceLaunchGame (Controller controleur) {
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