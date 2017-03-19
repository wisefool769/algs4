public class Percolation {
    private int gridSize;
    private boolean[][] grid;
    private int[] roots;
    private int[] sizes;
    private int nOpen;
    private int topIdx;
    private int bottomIdx;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        gridSize = n;
        grid = new boolean[n][n];
        roots = new int[n * n + 2];
        sizes = new int[n * n + 2];
        
        topIdx = n * n;
        bottomIdx = topIdx + 1;
        
        roots[topIdx] = topIdx;
        roots[bottomIdx] = bottomIdx;
        
        for (int i = n; i < n * (n - 1); i++) {
            roots[i] = i;
            sizes[i] = 1;
        }
        
        for (int i = 0; i < n; i++) {
            roots[i] = topIdx;
            sizes[i] = n + 1;
        }
        
        boolean alreadyConnected = false;
        for (int i = n * n - n; i < n * n; i++) {
            if (roots[i] != topIdx) {
                roots[i] = bottomIdx;
                sizes[i] = n + 1;
            } 
            else {
                alreadyConnected = true;
                break;
            }
         
        }

        if (alreadyConnected) {
            roots[bottomIdx] = topIdx;
        }
        

        for (int i = 0; i != n; i++) {
            for (int j = 0; j != n; j++) {
                grid[i][j] = false;
            }
        }

        nOpen = 0;
    }
    
    private int getRoot(int idx)
    {
        while (roots[idx] != idx) {
            idx = roots[idx];
        }

        return idx;
    }
    
    private void union(int i, int j) {
        int ri = getRoot(i);
        int rj = getRoot(j);
        if (ri != rj) {
            if (sizes[ri] > sizes[rj]) {
                roots[rj] = ri;
                sizes[ri] += sizes[rj];
            } 
            else {
                roots[ri] = rj;
                sizes[rj] += sizes[ri];
            }
        }
    }
    
    
    private boolean connected(int i, int j) {
        return getRoot(i) == getRoot(j);
    }
    
    private int getRootIdx(int row, int col) {
        return row * gridSize + col;
    }
    
    private boolean inBounds(int axisVal) {
        return ((axisVal > -1) && (axisVal < gridSize));
    }
    
    private void connectCells(int r1, int c1, int r2, int c2) {
        if (inBounds(r2) && inBounds(c2) && isOpen(r2, c2)) {
            union(getRootIdx(r1, c1), getRootIdx(r2, c2));
        }
    }
    
    private void connectNeighbors(int r1, int c1) {
        connectCells(r1, c1, r1 + 1, c1);
        connectCells(r1, c1, r1, c1 + 1);
        connectCells(r1, c1, r1 - 1, c1);
        connectCells(r1, c1, r1, c1 - 1);
    }
    
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isFull(row, col)) {
            grid[row][col] = true;
            nOpen++;
            connectNeighbors(row, col);
        }
    }
    
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row][col];
    }
   // is site (row, col) full?
    public boolean isFull(int row, int col)  {
        return (!grid[row][col]);
    }
    
   // number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }
    
    // does the system percolate?
    public boolean percolates() {
        return connected(topIdx, bottomIdx);
    }
  // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(1);
        perc.open(0, 0);
        System.out.println("number of open sites: " + perc.numberOfOpenSites());
        System.out.println("percolates: " + perc.percolates());
    }
}