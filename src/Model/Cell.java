package Model;

/**
 * Cellule
 * @author ymouacha
 * @author smasson
 */
public class Cell {
    private int nbNeighbors;
    private final int posx;
    private final int posy;
    private Boolean state;

    public Cell(int x , int y ){
        this.posx = x;
        this.posy = y;
        this.state = true;
        this.nbNeighbors = 0;
    }

    public void setState(Boolean newState){
        this.state = newState;
    }

    public void setNbVoisins(int nb){
        this.nbNeighbors = nb;
    }

    public int getNbNeighbors(){
        return nbNeighbors;
    }

    public void kill(){
        this.state = false;
    }
    
    public Boolean getState(){
        return this.state;
    }

    public int getX(){
        return this.posx;
    }

    public int getY(){
        return this.posy;
    }
    
}
