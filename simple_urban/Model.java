import java.util.*;

/**
* Basic urban flows model.
*
* The model just has an input per time step, an output per time step, and 
* a probability that agents will leave through some other route not monitored.
* 
* Parameters set at the command line are:
* currentPop, startTime, endTime, parameterString
* where parameterString is made of lose rate to alternative routes, rate in per time step, rate out per time step 
* as doubles.
* 
* @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
*/
public class Model {

    // These parameters can be set on the command line
    int numberOfAgents = 100;
    int timeStart = 100;
	int timeEnd = 110;
	double [] parameters = {0.01,100,100}; 

	
    // Others	
	int iterations = 1;
	int width = 100;
    int height = 100;
    boolean debug = false;
	int[][] environment = null;
	
    // Used for identifying parameters within array.
	// Used just incase we want variable parameter lengths.
	final static int RATE_OF_LOSE = 0;
	final static int PEOPLE_IN = 1;
	final static int PEOPLE_OUT = 2;

    
	
	
    // A list of all the agents
    ArrayList<Agent> allAgents = new ArrayList<Agent>();

    public Model(){}
    public Model(String[] args) {
    
        // This just picks up if the user has run with any command line arguments, 
        // otherwise defaults above used.
        if (args.length > 0) {
            try {
                numberOfAgents = Integer.parseInt(args[0]);
                timeStart = Integer.parseInt(args[1]);
				timeEnd = Integer.parseInt(args[2]);
				iterations = timeEnd - timeStart;
                String parametersStr = args[3];



                String[] temp =  parametersStr.split(",");
				parameters = new double[temp.length];
                for (int i=0; i<temp.length; i++) {
                    parameters[i] = Double.parseDouble(temp[i]);
                }

				
				
            } catch (NumberFormatException e) {
                System.out.println("Run with: java Model currentPop startTime endTime parametersAsDoublesInString");
                System.exit(1);
            }
        }

        // Make a grid, and set some cells as banks i.e. give the cell some gold.
        environment = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Unused at mo.
            }
        }


        for (int counter = 0; counter < numberOfAgents; counter = counter + 1) {

            Agent agent = new Agent();
            //agent.x = (int)(Math.random() * width);     // Give a random x coordinate.
			//agent.y = (int)(Math.random() * height);    // Give a random y coordinate.
            
            // Make sure the agent knows how big the environment is so it 
            // doesn't wander off the edge.
            agent.environment = environment;
            agent.width = width;
            agent.height = height;
			agent.parameters = parameters;
            
            // Make sure each agent has the list of all agents.
            agent.allAgents = allAgents;

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
        
			addAgents((int)parameters[Model.PEOPLE_IN]);
		
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

			allAgentsIterator = allAgents.iterator();
			int removeNumber = Model.PEOPLE_OUT;
            while (allAgentsIterator.hasNext()) {
				if (removeNumber > 0) {
					Agent agent = allAgentsIterator.next();
					allAgentsIterator.remove();
					removeNumber--;
				} else {
					break;
				}
            } 

            
            
        } // for counter
        
		System.out.println(allAgents.size());
        
        
    
    }

	
	public void addAgents(int addNumber) {
		for (int counter = 0; counter < addNumber; counter = counter + 1) {

            Agent agent = new Agent();
            //agent.x = (int)(Math.random() * width);     // Give a random x coordinate.
			//agent.y = (int)(Math.random() * height);    // Give a random y coordinate.
            
            // Make sure the agent knows how big the environment is so it 
            // doesn't wander off the edge.
            agent.environment = environment;
            agent.width = width;
            agent.height = height;
			agent.parameters = parameters;
            
            // Make sure each agent has the list of all agents.
            agent.allAgents = allAgents;

            // Add agent to the list of all agents.
            allAgents.add(agent);
            
        }
	
	}
	
	
    public int getNumberOfAgents() {
        return this.numberOfAgents;
    }

    

    // Model ignition system.
    public static void main (String args[]) {
	System.out.println("start");
        Model m = new Model(args);
        m.runModel();
    }

}
