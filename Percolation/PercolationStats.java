import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private int ntrials;
    private double[] results;
    private double sample_mean;
    private double sample_std;
    
    
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials){
        if((n <= 0) || (trials <= 0)){
            throw new IllegalArgumentException("grid size and trials must be > 0");
        }
        ntrials = trials;
        
        results = new double[trials];
        int sum = 0;
        for(int i = 0; i != trials; i ++){
            Percolation perc = new Percolation(n);
            int[] ordering = new int[n * n];
           
            for(int j = 0; j != n * n; j ++){
                ordering[j] = j;
            }
            
            StdRandom.shuffle(ordering);
            
            int opened = 0;
            while(!perc.percolates()){
                int raw_col = ordering[opened] % n;
                int raw_row = (ordering[opened] - raw_col) / n;
                int col = raw_col + 1;
                int row = raw_row + 1;
                perc.open(row, col);
                opened ++;
            }
            
            results[i] = (double)opened / (n * n);
        }
        sample_mean = StdStats.mean(results);
        sample_std = StdStats.stddev(results);
    }
   // sample mean of percolation threshold
    public double mean(){
        return sample_mean;
    }
     // sample standard deviation of percolation threshold
    public double stddev(){
       return sample_std;
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo(){
        return sample_mean - 1.96 * sample_std / ntrials;
    }
// high endpoint of 95% confi}dence interval
    public double confidenceHi(){
        return sample_mean + 1.96 * sample_std / ntrials;
    }
 // test client (described below)
    public static void main(String[] args)  {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean \t\t= " + ps.mean());
        System.out.println("stddev \t\t= " + ps.stddev());
        System.out.println("95% confidence interval \t\t= [" + ps.confidenceLo() 
                          + ", " + ps.confidenceHi() + "]");
    }
    
}
