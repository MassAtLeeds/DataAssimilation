import java.util.*;

/**
* It is a template for agents.
* @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
*/

public class Agent {

    // Variables storing agent knowledge and properties.
    
    // Spatial coodinates.
    int x = 0;
    int y = 0;

    
    
    // List of all the agents. 
    ArrayList <Agent> allAgents = null;
    
    // Environment so agents can check for banks.
    int[][] environment = null;
    int width = 0;
    int height = 0;
    double [] parameters = {0};
	
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
        
		
		if (Math.random() < parameters[Model.RATE_OF_LOSE]) toBeRemoved = true;
		
        if (toBeRemoved == true) return;
        
        

        // When it acts, the agent based on this template will run through the list of 
        // all agents, acting on them.
        for (int agentNumber = 0; agentNumber < allAgents.size(); agentNumber = agentNumber + 1) {
        
            // Get the next agent in the list.
            Agent otherAgent = allAgents.get(agentNumber);
            
            // If the agent in the list is itself, skip this loop.
            if (otherAgent == this) continue;
        
            // If the agent from the list is in the same cell...
            if ((otherAgent.x == x) && (otherAgent.y == y)) {
            
                // Unused
            
            }
            
        } 
    
    }
}
    
