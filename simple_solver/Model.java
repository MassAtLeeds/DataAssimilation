import java.util.*;



public class Model {

	double result = 0.0;

	public Model (String args[]) {
	
		result = args.length; // for something to do, not because it makes sense.
	
	}
	
    public void runModel(double x) {    
  
		result = (x*x)+ (result*x) - 3;
            
    } 
    
    public int[] getGoldAtIterations() {
        return new int[] {(int)result};
    }

}
