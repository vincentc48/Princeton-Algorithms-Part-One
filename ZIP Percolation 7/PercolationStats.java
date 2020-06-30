
public class PercolationStats
{
    public static void main(String[] args){
        PercolationStats p = new PercolationStats(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        System.out.println("Mean: " + p.mean());
        System.out.println("Standard Deviation: "+ p.stddev());
        System.out.println("95% Confidence: [" + p.confidenceLo() + "," + p.confidenceHi() +"]");
    }
    
    private int d;
    private int trials;
    private int[] results;
    
    public PercolationStats(int dim, int tri){
        if(dim<1||tri<0) throw new IllegalArgumentException("");
        d = dim;
        trials = tri;
        results = new int[trials];
        //implementation of the percolations
        for (int i =0; i<results.length;i++){
            Percolation p = new Percolation(d);
            while (!p.percolates()){
                p.open((int)(Math.random()*d+1),(int)(Math.random()*d+1));
            }
            results[i] = p.numberOfOpenSites();
        }
    }

    public double mean()
    {
        double sum = 0.0;
        for (int i:results){
            sum += (double)i;
        }
        return sum/(d*d*trials);
    }
    
    public double stddev(){
        double av =mean();
        double r =0.0;
        for (int i:results){
            r += Math.pow((double)i/(d*d)-av,2.0);
        }
        r /= (trials-1.0);
        return Math.sqrt(r);
    }
    
    public double confidenceLo(){
        return mean()-1.96*stddev()/Math.sqrt(trials);
    }
    
    public double confidenceHi(){
        return mean()+1.96*stddev()/Math.sqrt(trials);
    }
    
}
