package Model;

import java.util.Random;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * JeuDeLaVie
 * @ymouacha
 * @smasson
 */
public class JeuDeLaVie {

    private final static int DEFAULT_VIEMIN = 3;
    private final static int DEFAULT_VIEMAX = 3;
    private final static int DEFAULT_MORTAS = 4;
    private final static int DEFAULT_MORTIS = 1;
    
    private IntegerProperty tour;
    private IntegerProperty boardSize;

    private BooleanProperty[][] boolTab;
    private Cell[][] cellTab;

    private IntegerProperty vieMin = new SimpleIntegerProperty(DEFAULT_VIEMIN);
    private IntegerProperty vieMax = new SimpleIntegerProperty(DEFAULT_VIEMAX);
    private IntegerProperty mortAs = new SimpleIntegerProperty(DEFAULT_MORTAS);
    private IntegerProperty mortIs = new SimpleIntegerProperty(DEFAULT_MORTIS);

    private DoubleProperty proba;

    public JeuDeLaVie(int size){
        this.boardSize = new SimpleIntegerProperty(size);
        this.tour = new SimpleIntegerProperty(0);
        this.cellTab = new Cell[size][size];
        this.boolTab = new SimpleBooleanProperty[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.cellTab[i][j] = null;
                this.boolTab[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }

    /**
     * Cette fonction a pour but de modifier la taille du plateau de jeu.
     * Pour cela on crée un tableau de booleen ainsi qu'un nouveau tableau de cellule avec la nouvelle taille qu'on inserera par la suite dans notre model
     * @param newSize
     */ 
    public void setSize(int newSize){
        SimpleBooleanProperty[][] newBooleanTab = new SimpleBooleanProperty[newSize][newSize];
        Cell[][] newCellTab = new Cell[newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                newBooleanTab[i][j] = new SimpleBooleanProperty(false);
                newCellTab[i][j] = null;
            }
        }
        this.cellTab = newCellTab;
        this.boolTab = newBooleanTab;
        this.boardSize.setValue(newSize);;
    }

    /**
     * Fonction qui verifie si la partie est fini.
     * Pour cela on verifie dans notre tableau de booleen si une cellule est vivante.
     */ 
    public boolean isFinished(){
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                if(this.boolTab[i][j].getValue() == true){
                    return false;
                }
            }
        }
        //Comme on a pas le droit de modifier l'interface depuis un autre thread que le JaFXAT on est oblige de passer par un Platform.runlater
        Platform.runLater(()-> {
            this.tour.setValue(0);
        });
        return true;
    } 

    /**
     * Fonction qui reinitialise le board actuel
     */ 
    public void resetBoard(){
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                    this.cellTab[i][j] = null;
                    this.boolTab[i][j].setValue(false);
            }
        }
    }

    /**
     * Fonction qui initialise le board actuel avec une certaine probabilite.
     * @param proba
     */ 
    public void initProba(double proba){
        this.proba = new SimpleDoubleProperty(proba);
        this.tour.setValue(0);
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                if(getRandomBoolean()){
                    this.cellTab[i][j] = new Cell(i, j);
                    this.boolTab[i][j].setValue(true);
                }
                else{
                    this.cellTab[i][j] = null;
                    this.boolTab[i][j].setValue(false);
                }
            }
        }
    }

    /**
     * Cette fonction synchronise notre tableau de boolean avec notre tableau de cellule.
     * En effet,comme le tableau de boolean est induit du tableau de cellule on part du principe que notre tableau de boolean 
     * aura un tour de retard vis à vis de notre tableau de cellule. Il faudra donc synchroniser les deux tableaux a la fin de chaque tour.
     * 
     */ 
    public void synchonizeTabs(){
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                if (this.cellTab[i][j] == null || cellTab[i][j].getState() == false){
                    this.boolTab[i][j].setValue(false);
                }
                else this.boolTab[i][j].setValue(true);
            }
        }
    }

    /**
     * Fonction qui calcule le nombre de voisins d'une cellule en fonction du tableau de booleen.
     * Pour cela on parcours les 8 voisins de chaque cellule sur notre tableau de boolen
     * True signife que une cellule est vivante
     * 
     */ 
    public int calculateNbNeighbors(int x , int y){
        int nbNeighbors = 0;
        for (int i = x-1; i < x+2; i++) {
            for (int j = y-1; j < y+2 ; j++) {
                //il faut gerer les effets de bord histoire de pas declancher d'erreur
                if((i == x && j ==y) || (i<0 || i>=this.boardSize.getValue()) || (j<0 || j>=this.boardSize.getValue())){
                    continue;
                }
                else if(this.boolTab[i][j].getValue()){
                    nbNeighbors++;
                }
            }
        }
        return nbNeighbors;
    }

    /**
     * Cette fonction à pour but de simuler un tour de jeu
     */ 
    public void computeTour(){
        //Comme on a pas le droit de modifier l'interface depuis un autre thread que le JaFXAT on est oblige de passer par un Platform.runlater
        Platform.runLater(()-> {
            this.tour.setValue(this.tour.add(1).getValue());
        });
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                int cellNbNeighbors = this.calculateNbNeighbors(i, j);
                
                //cas ou c'est vide
                if (this.cellTab[i][j] == null ){
                    //on test si le nombre de voisins de cette case est suffisant pour donner vie a une nouvelle cellule.
                    if( cellNbNeighbors>= this.getVieMin().getValue() && cellNbNeighbors<= this.getVieMax().getValue()){
                        this.cellTab[i][j] = new Cell(i, j);
                    }
                }
                else{
                    this.cellTab[i][j].setNbVoisins(calculateNbNeighbors(i, j)); //on calcule le nombre de voisins de chaque cellule 
                    //on fait pareil dans le cas ou une cellule existe mais est morte.
                    if(this.cellTab[i][j].getNbNeighbors()>= this.getVieMin().getValue() && this.cellTab[i][j].getNbNeighbors()<= this.getVieMax().getValue()){
                        this.cellTab[i][j].setState(true);
                    }
                    else{
                        //Ensuite on vérifie pour chaque cellule vivante si cette dernière doit mourir en fonction de mortAs et mortIs.
                        if(this.cellTab[i][j].getNbNeighbors() >=this.mortAs.getValue() || this.cellTab[i][j].getNbNeighbors() <= this.mortIs.getValue()){
                            this.cellTab[i][j].kill();
                        }
                    }
                }
            }
        }
        //On synchronie les deux table une fois tout cela fait
        this.synchonizeTabs();
    }

     /**
     * Cette fonction genere un boolean en fonction d'une probabilite
     */ 
    public boolean getRandomBoolean(){
        int c;
        Random rd = new Random();
        c = rd.nextInt(100);
        return c< proba.getValue();
    }

    public IntegerProperty getVieMax(){
        return this.vieMax;
    }

    public IntegerProperty getVieMin(){
        return this.vieMin;
    }

    public IntegerProperty getMortAs(){
        return this.mortAs;
    }

    public IntegerProperty getMortIs(){
        return this.mortIs;
    }

    public BooleanProperty[][] getBoolTab(){
        return this.boolTab;
    }

    public Cell[][] getCellTab(){
        return this.cellTab;
    }

    public IntegerProperty getTour(){
        return this.tour;
    }

    public IntegerProperty getBoardSize(){
        return this.boardSize;
    }
}