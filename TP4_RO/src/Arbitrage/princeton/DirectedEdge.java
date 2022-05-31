package arbitrage.princeton;

import edu.princeton.cs.introcs.StdOut;


public class DirectedEdge { 
    private final int v;
    private final int w;
    private final double weight;
    
    private String name;  

  
    public DirectedEdge(int v, int w, double weight) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    
    public DirectedEdge(int v, int w, double weight, String name) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.name = name;
    }

  
    public int from() {
        return v;
    }

    
    public int to() {
        return w;
    }

    
    public double weight() {
        return weight;
    }

    public String getBankName() {
        return name;
    }
    
    

    @Override
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }

    
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 23, 3.14);
        StdOut.println(e);
    }
}
