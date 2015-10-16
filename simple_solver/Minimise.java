
import org.apache.commons.math3.optimization.univariate.*;
import org.apache.commons.math3.optimization.*;

public class Minimise {

	public Minimise() {
	//http://commons.apache.org/proper/commons-math/userguide/optimization.html
	//http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/optimization/univariate/BrentOptimizer.html#BrentOptimizer%28double,%20double%29
	//http://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/optimization/univariate/BaseAbstractUnivariateOptimizer.html#optimize%28int,%20org.apache.commons.math3.analysis.UnivariateFunction,%20org.apache.commons.math3.optimization.GoalType,%20double,%20double%29
	//http://stackoverflow.com/questions/3728246/what-should-be-the-epsilon-value-when-performing-double-value-equal-comparison
		BrentOptimizer b = new BrentOptimizer(0.000001, 0.000001);
		UnivariatePointValuePair ans = b.optimize(100, new WrapModelAsFunction(), GoalType.MINIMIZE, -10, 10);
		System.out.println(ans.getPoint());
		System.out.println(ans.getValue());
		
	}
	
	public static void main (String args[]) {
		new Minimise();
	}
	
}