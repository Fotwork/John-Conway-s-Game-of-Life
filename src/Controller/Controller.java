package Controller;

import Model.Cell;
import Model.Factory;
import Model.JeuDeLaVie;
import View.Board;
import View.JeuDeLaVieView;
import javafx.application.Platform;
import javafx.concurrent.Service;
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

    /**
     * Cette methode sera appelée dans le constructeur afin d'animer toutes les composantes de notre vue(Ecouteurs, Evenements...)
    */
    public void computeVue(){

        this.computeLeftSide();

        this.computeBoard();

        this.computeRightSide();
    }

    /**
     * LEFTSIDE : methode qui va animer la partie gauche de la vue.
     */
    public void computeLeftSide(){
        /**
         * EDITION : action sur le button qui permet de changer la taille du plateau principale
         */
        this.getView().getLeftSide().getEdition().getSizeBoard().getButton().setOnAction(h->{
            String textSize = this.getView().getLeftSide().getEdition().getSizeBoard().getTextField().textProperty().getValue();
            if(this.getView().getLeftSide().getEdition().getSizeBoard().getTextField().textProperty().getValue().length() == 0){
                /** Description */
                this.getView().showAlert("Erreur : Modificaiton taille du plateau de jeu", "Vous devez rentrer une valeur valide !", "Vous devez au moins rentrer une valeur !");
            }
            /**
             * C'est une expression régulière qui permet de vérifier si c'est un chiffre numérique et qu'il est positif.
             */
            else if(!textSize.matches("[+]?\\d*(\\.\\d+)?")){
                    this.getView().showAlert("Erreur : Modificaiton taille du plateau de jeu", "Vous devez rentrer une valeur valide ! ", "Rentrez une taille strictement supérieur a 0.");
                }
            else{
                int newsize = Integer.parseInt(textSize);
                if(newsize != this.getModel().getBoardSize().getValue()){
                    this.getModel().setSize(newsize);
                    Board newBoard = new Board(newsize, 5);
                    this.getView().setBoard(newBoard);
                    this.computeBoard();
                }
            }   
        });

        /**
         * EDITION : evenement sur le button qui permet de reinitialiser le plateau de jeu.
         */
        this.getView().getLeftSide().getEdition().getRebootBoard().setOnAction(h->{ 
            if(this.getView().showConfirmation("Confirmation : réinitialisation du plateau de jeu", "Voulez vous vraiment réinitialiser le plateu de jeu?", "")){
                //la reinitialisation du plateau de jeu nous amene a appeler notre fonction initBoardProba avec une probabilitee de 0%.
                this.initBoardProba(0);
            }
        });
        
        /**
         * EDITION : ecouteur sur le button qui permet de generer un plateau de jeu avec une certaine probabilite p.
        */
        this.getView().getLeftSide().getEdition().getInitAlea().getButton().setOnAction(h->{
            String textProba = this.getView().getLeftSide().getEdition().getInitAlea().getTextField().textProperty().getValue();
            if(this.getView().getLeftSide().getEdition().getInitAlea().getTextField().textProperty().getValue().length() == 0){
                this.getView().showAlert("Erreur : Initialisation du plateau avec une probabilité", "Veuillez entrer une valeur de probabilité conforme.", "Vous devez au moins rentrer une valeur !");
            }
            //C'est une expression reguliere qui permet de verifier si c'est un chiffre numerique et qu'il est positif.
            else if(!textProba.matches("[+]?\\d*(\\.\\d+)?")){
                    this.getView().showAlert("Erreur : Modificaiton taille du plateau", "Vous devez rentrer une valeur valide ! ", "Rentrez une probabilité supérieur ou égal a 0.");
                }
            else{
                Integer proba = Integer.parseInt(textProba);
                this.getModel().initProba(proba);
                this.initBoardProba(proba);
            }
        });

        /**
         * PARAMETRES : Action sur la Combobox qui permet de choisir une valeur pour la MortAsphyxie
         */
        this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().setOnAction(h -> {
            //si il y a une incohérence sur les valeurs alors on affiche une fenêtre d'alerte
            if(this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().getValue()){
                this.getView().showAlert("Erreur Mort ashpyxie", "Incohérence au niveau des paramètres du jeu","Il est impossible d'avoir MortAsphyxie<MortIsolation");

            }
            else{
                this.getModel().getMortAs().setValue(this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().getValue());
            }
        });

        /**
         * PARAMETRES : Action sur la Combobox qui permet de choisir une valeur pour la MortIsolation
         */
        this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().setOnAction(h -> {
            if(this.getView().getLeftSide().getSettings().getMortAsphyxie().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().getValue()){
                this.getView().showAlert("Erreur mort isolation", "Vos modifications n'ont pas été prises en compte !","Il est impossible d'avoir MortAsphyxie<MortIsolation");
            }
            else{
                this.getModel().getMortIs().setValue(this.getView().getLeftSide().getSettings().getMortIsolation().getCombobox().getValue());
            }
        });

        /**
         * PARAMETRES : Action sur la Combobox qui permet de choisir une valeur pour la VieMax
         */
        this.getView().getLeftSide().getSettings().getVieMax().getCombobox().setOnAction(h -> {
            if(this.getView().getLeftSide().getSettings().getVieMax().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getVieMin().getCombobox().getValue()){
                this.getView().showAlert("Erreur vie max", "Vos modifications n'ont pas été prises en compte !","Il est impossible d'avoir VieMin>VieMax");
            }
            else{
                this.getModel().getVieMax().setValue(this.getView().getLeftSide().getSettings().getVieMax().getCombobox().getValue());
            }
        });

        /**
         * PARAMETRES : Action sur la Combobox qui permet de choisir une valeur pour la VieMin
         */
        this.getView().getLeftSide().getSettings().getVieMin().getCombobox().setOnAction(h -> {
            if(this.getView().getLeftSide().getSettings().getVieMax().getCombobox().getValue()<this.getView().getLeftSide().getSettings().getVieMin().getCombobox().getValue()){
                this.getView().showAlert("Erreur vie min", "Vos modifications n'ont pas été prises en compte !","Il est impossible d'avoir VieMin>VieMax");
            }
            else{
                this.getModel().getVieMin().setValue(this.getView().getLeftSide().getSettings().getVieMin().getCombobox().getValue());
            }
        });

        /**
         * BOUTTON PLAY : Paramètres liés à notre service ServiceLaunchGame après un cancel ou un succès.
         */
        this.calculateTaskLaunchGame.setOnCancelled(h -> {
            this.calculateTaskLaunchGame.reset();
        });
        this.calculateTaskLaunchGame.setOnSucceeded(h -> {
            this.calculateTaskLaunchGame.reset();
            this.getModel().resetBoard(); //après un succès, logiquement on réinitialise notre model.
            this.getView().getLeftSide().getPlay().setText("Play");
            this.getView().getBoard().setDisable(false);
            this.getView().getLeftSide().getEdition().setDisable(false);
            this.getView().getBoard().setBorderRect(); //On reaffiche les bords de chaque rectangle.
        });

        /**
         * BOUTTON PLAY : mise en place d'un écouteur sur le boutton qui permet de lancer/stoper notre jeu
         */
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
    }

    /**
     * BOARD : méthode qui va animer plateau principal de la vue.
     */
    public void computeBoard(){
        this.getView().getBoard().addEventHandler(MouseEvent.MOUSE_CLICKED, new ListenerClickMainBoard(this) );
        this.getView().getBoard().addEventHandler(ScrollEvent.SCROLL, new ListenerScrollMainBoard(this));
    }

    /**
     * RIGHTSIDE : methode qui va animer le cote droit de la vue.
     */
    public void computeRightSide(){
        /**
         * BOUTTON LOAD : action sur notre bouttion load qui permet de charger un motif sur notre zone tampon
         */
        this.getView().getRightSide().getLoad().setOnAction(h -> {
            Boolean[][] motif = null;
            //switch case en fonction du motif selectionné, on appele notre motif predefini qui se situe dans notre class Factory et on l'applique sur notre zone tampon.
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

        /**
         * LABEL TOUR : Bind entre le tour du model et le label qui affiche le tour.
         */
        CounterStringBinding csb = new CounterStringBinding(this.getModel().getTour());
        this.getView().getLeftSide().getLabelTour().textProperty().bind(csb);;
    }


    /**
     * Cette fonction permet d'initialiser le board en fonction d'une probabilité .
     * @param proba
     */ 
    public void initBoardProba(double proba){
        this.getModel().initProba(proba); //il faut initialiser d'abord le model 
        for (Node node : this.getView().getBoard().getChildren()) {
            Rectangle rect = (Rectangle)node;
            //ensuite en fonction du model(tableau de bool) on colorie notre board
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

    /**
     * Cette fonction permet de charger un motif sur la zone tampon.
     * @param motif ce paramètre correspond au motif predefini se trouvant dans la classe Factory
     */ 
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

    public JeuDeLaVie getModel(){
        return this.model;
    }

    public JeuDeLaVieView getView(){
        return this.view;
    }
}