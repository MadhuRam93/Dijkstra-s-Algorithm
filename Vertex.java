/**
 *
 * @author madhu ramachandra
 */

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a vertex in the graph
 */
public class Vertex
{
    public String name;                     // Vertex name
    public List<Vertex> adj;                // Adjacent vertices
    public List<Edge> edges;                // Incident edges
    public Vertex prev;                     // Previous vertex on shortest path
    public double dist;                     // Distance of path
    public String status;                   // Status of vertex - up or down

    public Vertex( String nm ){             // Constructor
        name = nm; 
        status = "up";
        adj = new LinkedList<>( );
        edges = new LinkedList<>(); 
        reset( ); 
    }

    public void reset() { 
        dist = graph.INFINITY; 
        prev = null; 
    } 
    
    /**
     * Getter & setter for class variable Status 
     */
    public String getStatus(){
       return this.status;
   }
   
   public void setStatus(String status){
       this.status = status;
   }
}