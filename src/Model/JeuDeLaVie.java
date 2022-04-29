package Model;

import java.util.Random;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * JeuDeLaVie
 */
public class JeuDeLaVie {

    private final static int DEFAULT_VIEMIN = 3;
    private final static int DEFAULT_VIEMAX = 3;
    private final static int DEFAULT_MORTAS = 4;
    private final static int DEFAULT_MORTIS = 1;
    
    private IntegerProperty tour;
    private IntegerProperty boardSize;

    private BooleanProperty[][] boolTab;
    private Cellule[][] cellTab;

    private IntegerProperty vieMin = new SimpleIntegerProperty(DEFAULT_VIEMIN);
    private IntegerProperty vieMax = new SimpleIntegerProperty(DEFAULT_VIEMAX);
    private IntegerProperty mortAs = new SimpleIntegerProperty(DEFAULT_MORTAS);
    private IntegerProperty mortIs = new SimpleIntegerProperty(DEFAULT_MORTIS);

    private DoubleProperty proba;

    public JeuDeLaVie(int size){
        this.boardSize = new SimpleIntegerProperty(size);
        this.tour = new SimpleIntegerProperty(0);
        this.cellTab = new Cellule[size][size];
        this.boolTab = new SimpleBooleanProperty[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.cellTab[i][j] = null;
                this.boolTab[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }

    public void setSize(int newSize){
        SimpleBooleanProperty[][] newBooleanTab = new SimpleBooleanProperty[newSize][newSize];
        Cellule[][] newCellTab = new Cellule[newSize][newSize];
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

    public boolean isFinished(){
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                if(this.boolTab[i][j].getValue() == true){
                    return false;
                }
            }
        }
        Platform.runLater(()->{
            this.tour.setValue(0);
        });
        return true;
    } 

    public void initProba(double proba){
        this.proba = new SimpleDoubleProperty(proba);
        Platform.runLater(()->{
            this.tour.setValue(0);
        });
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                if(getRandomBoolean()){
                    this.cellTab[i][j] = new Cellule(i, j);
                    this.boolTab[i][j].setValue(true);
                }
                else{
                    this.cellTab[i][j] = null;
                    this.boolTab[i][j].setValue(false);
                }
            }
        }
    }

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

    public int calculateNbNeighbors(int x , int y){
        int nbNeighbors = 0;
        for (int i = x-1; i < x+2; i++) {
            for (int j = y-1; j < y+2 ; j++) {
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

    public void computeTour(){
        Platform.runLater(()-> {
            this.tour.setValue(this.tour.add(1).getValue());
        });
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            for (int j = 0; j < this.boardSize.getValue(); j++) {

                
                if (this.cellTab[i][j] == null){
                    //cas ou la cellule est vide et donc on test si elle mérite la vie 
                    if( calculateNbNeighbors(i, j)>= this.getVieMin().getValue() && calculateNbNeighbors(i, j)<= this.getVieMax().getValue()){
                        this.cellTab[i][j] = new Cellule(i, j);
                    }
                }
                else{
                    //On calcule le nb de voisins pour chaque cellule
                    this.cellTab[i][j].setNbVoisins(calculateNbNeighbors(i, j));

                    //Ensuite on fait le traitement de vie/mort pour chaque cellule
                    if(this.cellTab[i][j].getNbVoisins() >=this.mortAs.getValue() || this.cellTab[i][j].getNbVoisins() <= this.mortIs.getValue()){
                        this.cellTab[i][j].kill();
                    }
                    else this.cellTab[i][j].setState(true);
                }
            }
        }
        //On synchronie les deux table une fois tout ca fait
        this.synchonizeTabs();
    }

 
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

    public Cellule[][] getCellTab(){
        return this.cellTab;
    }

    public Cellule getCell(int x , int y){
        return this.cellTab[x][y];
    }

    public IntegerProperty getTour(){
        return this.tour;
    }

    public IntegerProperty getBoardSize(){
        return this.boardSize;
    }


    public void afficheTab(){
        for (int i = 0; i < this.boardSize.getValue(); i++) {
            System.out.println();
            for (int j = 0; j < this.boardSize.getValue(); j++) {
                if(this.cellTab[i][j] == null){
                    System.out.print("N ");
                }
                else if(this.cellTab[i][j].getState() == true){
                    System.out.print("T ");
                }
                else System.out.print("F ");
            }
        }
        System.out.println("Nombre de tour : " + this.tour.getValue());
    }

    public static void main(String[] args) {
    
        Scanner scan = new Scanner(System.in);
        JeuDeLaVie jdv = new JeuDeLaVie(3);
        jdv.initProba(50);
        while(!jdv.isFinished()){
            jdv.afficheTab();
            jdv.computeTour();
            scan.nextLine();
        }
    }

}