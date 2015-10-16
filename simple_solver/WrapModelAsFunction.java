import org.apache.commons.math3.analysis.*;
import java.util.*;

 public class WrapModelAsFunction implements UnivariateFunction {
     public double value(double x) {
	 double y = x*x+4*x-3;
	 System.out.println("x = " + x + " y = " + y);
	 /**
		 Model m = new Model (new String [] {"aa"});
		 m.runModel(x);
         double y = (double) m.getGoldAtIterations()[0];
		 System.out.println("x = " + x + " y = " + y);
         if (Double.isNaN(x)) {
           throw new IllegalArgumentException("x = " + x);
         }
		 **/
         return y;
     }
 }