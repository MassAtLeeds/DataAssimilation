

/**
* This code is a parameter sweeper for an agent based model of cops and robbers.

* The code can be run as is, by moving to the directory it is in and typing:
* java ModelRunner
* (without the starting asterisk) or, you can change the values by giving it command line arguments in this form:
* java ModelRunner numberOfIterations(whole number) numberOfRealisations(whole number) numberOfAgents(whole number) numberOfParams(whole number) paramDefault1 (between 0 and < 1) paramMin1(between 0 and < 1) paramMax1(between 0 and < 1) paramIncrement1 (between 0 and < 1) paramNumberOfBins1 (whole number) paramMin2(between 0 and < 1) etc...
* e.g.
* java ModelRunner 1000 100 100 2 0.5 0.2 1.0 0.1 100 0.5 0.0 10.0 1.0 100

* The command line parameters are:
* numberOfIterations(whole number) The number of increments the model makes each time it runs.
* numberOfRealisations(whole number) The number of times to run each model for each parameter value bin.
* numberOfAgents(whole number) The number of agents the model makes each time it runs.
* numberOfParams(whole number) The number of parameters you want to sweep. Note that parameters are swept independently using the default for other parameters.
* paramDefault1 (between 0 and < 1) Default value for parameter for other parameters' sweeps.
* paramMin1(between 0 and < 1) Starting value for sweep.
* paramMax1(between 0 and < 1) Final value for sweep.
* paramIncrement1 (between 0 and < 1) Increment for sweep.
* paramNumberOfBins1 (whole number) Number of bins for the model results for a specific parameter sweep. Note this is not the parameter bins, which are set 
*    up using the paramMin1, paramMax1, and paramIncrement1. ModelRunner requests the maximum estimate of the model, and then bins the model results in paramNumberOfBins.
* The last 5 command line params can be repeated for multiple variables as indicated by numberOfParams.

* Note that the code is currently written for one specific model and needs rewriting to take in an Interfaced model. 

* @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
*/
public class ModelRunner {

	// Defaults.
	public int numberOfIterations = 1000;
	public int numberOfRealisations = 100;
	public int numberOfAgents = 1000;
	public String reportSteps = "500,999"; // Needs command line parameter.
	public int numberOfParams = 1;
	public double[] paramDefaults = {0.5};
	public double[] paramMins = {0.1};
	public double[] paramMaxs = {1.0};
	public double[] paramIncrements = {0.1};
	public int[] paramSteps = {(int)((paramMaxs[0] - paramMins[0]) / paramIncrements[0]) + 1};
	public int[] paramNumberOfBins = {100};
	

	public ModelRunner(String[] args) {
	
		// For reporting time taken, to help adjust run numbers.
		long startTime = System.currentTimeMillis();
		
		// Parse command line params.
		if (args.length > 0) {
            try {
                numberOfIterations = Integer.parseInt(args[0]);
				numberOfRealisations = Integer.parseInt(args[1]);
				numberOfAgents = Integer.parseInt(args[2]);
				reportSteps = args[3];
                numberOfParams = Integer.parseInt(args[4]);
				
				paramMins = new double[numberOfParams];
				paramMaxs = new double[numberOfParams];
				paramIncrements = new double[numberOfParams];
			
				for (int i = 0; i < numberOfParams; i=i+5) {
					paramDefaults[i] = Double.parseDouble(args[5+i]);
					paramMins[i] = Double.parseDouble(args[6+i]);
					paramMaxs[i] = Double.parseDouble(args[7+i]);
					paramIncrements[i] = Double.parseDouble(args[8+i]);
					paramSteps[i] = (int)((paramMaxs[i] - paramMins[i]) / paramIncrements[i]) + 1;
					paramNumberOfBins[i] = Integer.parseInt(args[9+i]);
				}
               
            } catch (NumberFormatException e) {
                System.out.println("Error with command line parameters - see ModelRunner javadoc for details of these.");
                System.exit(1);
            }
        }
	
		Model m = new Model(new String[] {"100","1000","0.1","0.5","600,800"}); // Needs setting up with params
		
		int maxEstimate = m.getMaxEstimate();
		int numberOfreports = m.getOutputIterations().length;
		double results[][][][] = new double[numberOfParams][numberOfreports][][];
		
		
		
		// Temp loop variables
		String modelArgs[] = null;
		int[] gold = null; 
		
		// Start looping through each param.
		for (int i = 0; i < numberOfParams; i++) {
			 
			// Make an appropriate result frequency bin array for the param value bins.	
			for (int j = 0; j < numberOfreports; j++) {
				results[i][j] = new double[paramSteps[i]][paramNumberOfBins[i]];
			}
			
			// Start constructing the command line arguments for the Model.
			modelArgs = new String[5];
			modelArgs[0] = String.valueOf(numberOfAgents);
			modelArgs[1] = String.valueOf(numberOfIterations);
			modelArgs[2] = String.valueOf(0.1); //probabilityOfCop
			
			// Run through the values for the parameter.
			for (int j = 0; j < paramSteps[i]; j++) {
			
				// Construct the parameter specific Model arguments.
				modelArgs[3] = String.valueOf(j * paramIncrements[i] + paramMins[i]);
				System.out.println("For param " + i + " running value " + modelArgs[3]);
				modelArgs[4] = reportSteps;
				
				// For each parameter value, run the model with the same arguments multiple times for stochastic evaluation (multiple realisations).
				for (int realisation = 0; realisation < numberOfRealisations; realisation++) {
				
					Model model = new Model(modelArgs);
					model.runModel();
					gold = model.getGoldAtIterations();
					
					// Get the value for a specific report point and store it.
					for (int k = 0; k < numberOfreports; k++) {
						results[i][k][j][(int)(((double)gold[k]/(double)maxEstimate)*(double)(paramNumberOfBins[i] - 1))]++;
					}
				}
			}

		}
		
		for (int i = 0; i < numberOfParams; i++) {
			
			/**
			System.out.println("--------- Param:" + i);
			
			for (int k = 0; k < numberOfreports; k++) {
			
				System.out.println("--------- Timestep:" + k);
				
				for (int paraStep = 0; paraStep < numberOfSteps[i]; paraStep++) {
				
					for (int goldStep = 0; goldStep < numberOfBins; goldStep++) {
					
						System.out.print(results[i][k][paraStep][goldStep] + " ");
					}
					System.out.println();
				}
			}
			**/
			
			// Display as image.
			for (int r = 0; r < numberOfreports; r++) {
		
				new Display("surface " + r, results[i][r]);
		
			}
			
			// Report run time.
			startTime = (System.currentTimeMillis() - startTime)/(long)1000;
			System.out.println("it has taken" + startTime + " s to run " + numberOfParams * numberOfRealisations + " models");
		}

		
	
	}
	
	
	public static void main (String args[]) {
		new ModelRunner(args);
	}
}