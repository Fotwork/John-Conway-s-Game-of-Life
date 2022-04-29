package Model;

public class Cellule {
    private int nbVoisins;
    private final int posx;
    private final int posy;
    private Boolean state;

    public Cellule(int x , int y ){
        this.posx = x;
        this.posy = y;
        this.state = true;
        this.nbVoisins = 0;
    }

    public void setState(Boolean newState){
        this.state = newState;
    }

    public void setNbVoisins(int nb){
        this.nbVoisins = nb;
    }

    public int getNbVoisins(){
        return nbVoisins;
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
