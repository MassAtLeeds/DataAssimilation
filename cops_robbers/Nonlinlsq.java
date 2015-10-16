// Compile: javac -cp .:commons-math3-3.4.1.jar Nonlinlsq.java
// run: java -cp .:commons-math3-3.4.1.jar Nonlinlsq 10 1000 1000 0.1 0.61 200,400,600,800,999


import org.apache.commons.math3.optimization.univariate.*;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import java.io.*;

public class Nonlinlsq implements UnivariateFunction {

    int numberofrealisations = 10;
    int[] z;
    //int[] z=new int[]{10,20,30,40};
    double[] xb=new double[]{0.5};
    String[] modelargs;

    //    public Nonlinlsq() {}
    public Nonlinlsq(String[] args) {

	modelargs=Arrays.copyOfRange(args,args.length-5,args.length);
	System.out.println(Arrays.toString(modelargs));
	// This just picks up if the user has run with any command line arguments,
	// otherwise defaults above used.
	if (args.length > 0) {
	    try {
		numberofrealisations = Integer.parseInt(args[0]);
		
	    } catch (NumberFormatException e) {
		System.out.println("Run with: java Model numberofrealisations(whole number)");
		System.exit(1);
	    }
	}

	// Read in observation vector
    try {
    z = readSyntheticData();
    System.out.println("Read synthetic data: "+Arrays.toString(z));
    }
    catch (IOException e) {
        System.err.println("Cannot read synthetic data. Can't continue");
            System.err.println(e);
            e.printStackTrace();
            System.exit(1);
        }

    // Run minimiser
     BrentOptimizer b = new BrentOptimizer(0.01, 0.01);
     UnivariatePointValuePair ans = b.optimize(20, this, GoalType.MINIMIZE, 0, 1);
     System.out.println(ans.getPoint());
     System.out.println(ans.getValue());

    }



    // Compute objective function
    public double value(double y) {

	double[] x=new double[] {y};
	//Compute sum of squares z-H(x)	
	double[] hval=Hmean(x);
	double sumofsquaresobs=0;
	for (int i=0; i<z.length; i++){
	    sumofsquaresobs=sumofsquaresobs+((double)z[i]-hval[i])*((double)z[i]-hval[i]);
	}

	double sumofsquaresprior=0;
	for (int i=0; i<x.length; i++){
	    sumofsquaresprior=sumofsquaresprior+(x[i]-xb[i])*(x[i]-xb[i]);
	}
	double val=sumofsquaresobs+sumofsquaresprior;
	
	System.out.println("x  = " + x[0] + " y = " + val);
	
	return val;
    }
    
    // Run simulation
    public double[] Hmean(double[] x) {
	
	// Run many realisations of model and compute mean output
	// Loop
	// String[] stringargs= {"25","1000","0.1",String.valueOf(x[0]),outputIterationsStr};
	Model m = new Model(modelargs);
	double[] zall=new double[m.outputIterations.length]; 
	for (int counter= 0; counter < numberofrealisations; counter = counter + 1) {	    
	    m = new Model(modelargs);
	    m.runModel();
	    for(int i=0; i<zall.length;i++){
		zall[i]=zall[i]+m.getGoldAtIterations()[i];
	    }
	}
	// Output is average
	for(int i=0; i<zall.length; i++){
	    zall[i]=zall[i]/(double)numberofrealisations;
	    // System.out.println(zall[i]);
	}
	return zall;
    }    

    /**
     * Function to read a text file that contains synthetic data
     *
     * @return The amount of gold that cops have at some chosen iterations
     */
    int[] readSyntheticData() throws IOException{
        BufferedReader br;
        int[] synth_data;
            br = new BufferedReader(new FileReader(new File("./synthetic_data.csv")));
            String[] lineArray = br.readLine().split(",");
            br.close();
            synth_data = new int[lineArray.length];
            for (int i=0; i<lineArray.length; i++) {
                synth_data[i] = Integer.parseInt(lineArray[i]);
            }
        return synth_data;
    }
    // Model ignition system.
    public static void main (String args[]) {
        Nonlinlsq nonlinlsq = new Nonlinlsq(args);
        //double out=nonlinlsq.value(1);
	// System.out.println(out);
    }

}
