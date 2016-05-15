/**
 *
 * @author madhu ramachandra
 */

/**
 * Represents an edge in the graph.
 */
public class Edge {
    
    private final Vertex source;        // tail vertex
    private final Vertex dest;          // head vertex
    private double weight;              // weight of edge
    public String status;               // Status of edge - up or down
    
    public Edge (Vertex source, Vertex dest, double weight){        // Constructor
        this.source = source;
        this.dest = dest;
        this.weight = weight;
        this.status = "up";
    }
    
    /**
     * getters & setters for class variables
     */
   public Vertex getSource(){
       return this.source;
   }
   
   public Vertex getDest(){
       return this.dest;
   }
    
   public double getWeight(){
       return this.weight;
   }
   
   public void setWeight(Double weight){
       this.weight = weight;
   }
    
   public String getStatus(){
       return this.status;
   }
   
   public void setStatus(String status){
       this.status = status;
   }
}