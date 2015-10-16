import java.util.*;

/**
* This code is an agent based model of cops and robbers.

* It makes a set of cops and robbers, the ratio dependent on a probability.

* Robbers will take gold from other robbers they meet, but also take it from the environment, 
* where there are piles of it (banks).

* Cops will take gold from robbers, and then arrest them, removing them from the 
* model.

* The code can be run as is, by moving to the directory it is in and typing:
* java Model
* (without the starting asterisk) or, you can change the values by giving it command line arguments in this form:
* java Model numberOfAgents(whole number) iterations(whole number) probabilityOfCop(between 0 and < 1)
* e.g.
* java Model 100 200 0.2

* Note that the code is written to be as simple as possible for beginners to read, 
* rather than for efficiency and elegance. 

* @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
*/

public class Model {

    // These parameters can be set on the command line
    int numberOfAgents = 100;
    int iterations = 100;
    double probabilityOfArrest = 0.5;
    double probabilityOfCop = 0.1;
    String outputIterationsStr="200,400,600,800"; // Iterations that we want to output the number of gold
    int[] outputIterations = {200,400,600,800}; // Stores the iterations (basically above but an integer list not string)
    int[] goldAtIterations = {10,20,30,40}; // The amout of gold the robbers have in each of the above iterations

    // Others
    double probabilityOfBank = 0.1;
    boolean randomBanks = false; // Whether to create banks randomly or at specific locations
    int bankGold = 10;
    int width = 100;
    int height = 100;
    boolean debug = false;
    
    // A list of all the agents
    ArrayList<Agent> allAgents = new ArrayList<Agent>();

    public Model(){}
    public Model(String[] args) {
    
        // This just picks up if the user has run with any command line arguments, 
        // otherwise defaults above used.
        if (args.length > 0) {
            try {
                numberOfAgents = Integer.parseInt(args[0]);
                iterations = Integer.parseInt(args[1]);
                probabilityOfCop = Double.parseDouble(args[2]);
                probabilityOfArrest = Double.parseDouble(args[3]);
                outputIterationsStr=args[4];

                // Work out when to output the amount of gold (convert string into a list of ints)
                String[] outputIterationsStrArray =  outputIterationsStr.split(",");
                outputIterations = new int[outputIterationsStrArray.length];
                goldAtIterations = new int[outputIterationsStrArray.length];
                for (int i=0; i<outputIterationsStrArray.length; i++) {
                    outputIterations[i] = Integer.parseInt(outputIterationsStrArray[i]);
                }

            } catch (NumberFormatException e) {
                System.out.println("Run with: java Model numberOfAgents(whole number) iterations(whole number) probabilityOfCop(between 0 and < 1) probabilityOfArrest(between 0 and < 1)");
                System.exit(1);
            }
        }

        // Make a grid, and set some cells as banks i.e. give the cell some gold.
        int[][] environment = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (randomBanks) {
                    if (Math.random() < probabilityOfBank) { 
                        environment[x][y] = bankGold;
                    }
                }
                else { // Don't put gold randomly, always put it in every tenth cell
                    if (width % (x+1) == 10 || height % (y+1) == 10) {
                        environment[x][y] = bankGold;
                    }

                }
            }
        }


        for (int counter = 0; counter < numberOfAgents; counter = counter + 1) {

            Agent agent = new Agent();
            agent.x = (int)(Math.random() * width);     // Give a random x coordinate.
            agent.y = (int)(Math.random() * height);    // Give a random y coordinate.
            
            // Make sure the agent knows how big the environment is so it 
            // doesn't wander off the edge.
            agent.environment = environment;
            agent.probabilityOfArrest = probabilityOfArrest;
            agent.width = width;
            agent.height = height;
            
            // Make sure each agent has the list of all agents.
            agent.allAgents = allAgents;

            // Make the agent either a cop or a robber.
            if (Math.random() < probabilityOfCop) {
                agent.name = "cop";
            } else { 
                agent.name = "robber";
            }
            
            // Add agent to the list of all agents.
            allAgents.add(agent);
            
        }

    }

    /**
     * Run the model
     *
     * @return the number of gold pieces owned by robbers
     */
    public void runModel() {    
    
        
        // Run the model the number of requested iterations.
        for (int counter = 0; counter < iterations; counter = counter + 1) {
        
            // Move all the agents.
            for (Agent agent: allAgents) {
                agent.move();
            }
            
            // Once all agents are in their new location, get them 
            // to act. Note that we don't get them to act until 
            // all the agents have had a fair chance to move.
            for (Agent agent: allAgents) {
                agent.act();
            }
            
            // Go through and remove any robbers the cops have arrested.
            // We use an iterator here, which is slightly more complicated than the 
            // loops above, as we want to remove agents as we run through the collection of 
            // them.
            Iterator <Agent> allAgentsIterator = allAgents.iterator();
            while (allAgentsIterator.hasNext()) {
                Agent agent = allAgentsIterator.next();
                if (agent.toBeRemoved == true) allAgentsIterator.remove();
            }       

            // Finally see if we want to remember the amount of gold that robbers have in this
            // iteration
            for (int i=0; i<outputIterations.length; i++) {
                if (outputIterations[i]==counter) {
                    // Want to output robber gold at this iteration
                    int gold = 0;
                    for (Agent agent: allAgents) {
                        if(agent.name.equals("cop")) {
                            gold += agent.gold;
                        }
                    }
                    goldAtIterations[i] = gold;
                    break;
                }
            }
            
        } // for counter
        
        
        // At this point the model has finished. We iterate through 
        // the remaining agents to find out about them and print some 
        // statistics to the screen.
        int countCops = 0;
        int copGold =  0;
        int countRobbers = 0;
        int robberGold = 0;
        
        for (Agent agent: allAgents) {
            if(agent.name.equals("cop")) {
                countCops = countCops + 1;
                copGold = copGold + agent.gold;
            } else { 
                countRobbers = countRobbers + 1;
                robberGold = robberGold + agent.gold;
            }
        }
        if (debug) {
            System.out.print("After " + iterations  + " iterations, there are ");
            System.out.print(countCops + " cops with " + copGold + " gold pieces and ");
            System.out.println(countRobbers + " robbers with " + robberGold + " gold pieces.");
            System.out.println("\tGold At Iterations "+Arrays.toString(outputIterations) +":" + Arrays.toString(goldAtIterations));
        }
    
    }

	public int[] getOutputIterations () {
		return outputIterations;
	}
	
    public int getNumberOfAgents() {
        return this.numberOfAgents;
    }

    public double getProbabilityOfArrest() {
        return this.probabilityOfArrest;
    }

    public int[] getGoldAtIterations() {
        return this.goldAtIterations;
    }

	
	public int getMaxEstimate() {
		return (int)((double)width * (double)height * probabilityOfBank * (double)bankGold);
	}
	

    // Model ignition system.
    public static void main (String args[]) {
        Model m = new Model(args);
        m.runModel();
    }

}
