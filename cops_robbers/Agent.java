import java.util.*;

/**
* This code is used in an agent based model of cops and robbers.

* It is a template for agents.

* Robbers will take gold from other robbers they meet, but also take it from the environment, 
* where there are piles of it (banks).

* Cops will take gold from robbers, and then arrest them, removing them from the 
* model.

* Note that the code is written to be as simple as possible for beginners to read, 
* rather than for efficiency and elegance. 

* @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
*/

public class Agent {

    // Variables storing agent knowledge and properties.
    
    // Spatial coodinates.
    int x = 0;
    int y = 0;

    // Probability of being arrested if found by a cop
    double probabilityOfArrest = 0;
    
    int gold = 0;
    
    // Whether a cop or a robber.
    String name = "";
    
    // List of all the agents. 
    ArrayList <Agent> allAgents = null;
    
    // Environment so agents can check for banks.
    int[][] environment = null;
    int width = 0;
    int height = 0;
    
    // Whether this agent has been arrested.
    boolean toBeRemoved = false;
    
    
    void move () {
    
        // Move randomly up or down and left or right.
        if (Math.random() > 0.5) {
            x = x + 1;
        } else {
            x = x - 1;
        }
        
        if (Math.random() > 0.5) {
            y = y + 1; 
        } else { 
            y = y - 1;
        }
        
        
        // Boundary check: make sure we don't wander off the world.
        if (x < 0) x = 0;
        if (x >= width) x = width - 1;
        if (y < 0) y = 0;
        if (y >= height) y = height - 1;        
        
    }
    
    
    void act () {   
        
        // If this agent has been arrested already this round, skip this section.
        if (toBeRemoved == true) return;
        
        // If the current agent made from this template is a robber
        // and wasn't removed by the line above, check the current cell for gold.
        if (name.equals("robber")) {
            if (environment[x][y] > 0) {
                gold = gold + environment[x][y];
                environment[x][y] = 0;
            }
        }

        // When it acts, the agent based on this template will run through the list of 
        // all agents, acting on them.
        for (int agentNumber = 0; agentNumber < allAgents.size(); agentNumber = agentNumber + 1) {
        
            // Get the next agent in the list.
            Agent otherAgent = allAgents.get(agentNumber);
            
            // If the agent in the list is itself, skip this loop.
            if (otherAgent == this) continue;
        
            // If the agent from the list is in the same cell...
            if ((otherAgent.x == x) && (otherAgent.y == y)) {
            
                // If the agent made from this template is a cop, and the agent 
                // from the list is a robber, set the robber as arrested.
                // The main Model code will later go through and remove these agents.
                if ((name.equals("cop")) && (otherAgent.name.equals("robber"))) {
                    // Put a probability of arrest - maybe the robber will avoid the cop
                    if (Math.random() < probabilityOfArrest) { 
                        otherAgent.toBeRemoved = true;
                        gold = gold + otherAgent.gold;
                    }
                }
                // Otherwise, if this agent is a robber, and so is the other agent, then
                // take its gold (robbers rob each other)
                else if (name.equals("robber") && otherAgent.name.equals("robber")) {
                    gold = gold + otherAgent.gold;
                    otherAgent.gold = 0;
                }
            
            }
            
        } 
    
    }
}
    
