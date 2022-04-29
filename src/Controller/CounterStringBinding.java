package Controller;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;

/**
 * CompteurStringBinding
 */
public class CounterStringBinding extends StringBinding {
    
    IntegerProperty nbTour;
    
    public CounterStringBinding (IntegerProperty nb){
        this.nbTour = nb;
        this.bind(this.nbTour);
    }
    
    
    @Override
    public String computeValue(){
        return "Nombre de générations : " + this.nbTour.getValue();
    }
}
