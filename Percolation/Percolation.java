/*************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4  Percolation
 *  Dependencies: WeightedQuickUnionUF
 *
 *  Implements a grid with cells that can be opened. Used for the first project
 *  in Princeton Algs4 on Coursera. 
 *
 *************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
    
public class Percolation {
    private int width;
    private int gridSize;
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;
    private int nOpen;
    // index of virtual top of grid in uf 
    private int topIdx;
    // index of virtual bottom in uf
    private int bottomIdx;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        width = n;
        gridSize = width + 1;
        // n + 1 because indeces start at 1
        grid = new boolean[gridSize][gridSize];
        uf = new WeightedQuickUnionUF(width * width  + width + 2);
        
        topIdx = width * width + width;
        bottomIdx = topIdx + 1;

        for (int i = 1; i != gridSize; i++) {
            for (int j = 0; j != gridSize; j++) {
                grid[i][j] = false;
            }
        }

        nOpen = 0;
    }
   
    private int xyTo1D(int row, int col) {
        return (row - 1) * width + (col - 1);
    }
    
    private void validateIndices(int row, int col) {
        if ((row < 1) || (row > width))
            throw new IndexOutOfBoundsException("row index out of bounds");
        if ((col < 1) || (col > width))
            throw new IndexOutOfBoundsException("column index out of bounds");
    }
    
    private boolean checkIndices(int row, int col){
        return ((row > 0) && (row < gridSize) && (col > 0) && (col < gridSize));
    }
    
    private void checkBottomConnection(int r, int c){
        if(r == width){
            int local_bottom = width * width + c - 1;
            uf.union(xyTo1D(r, c), local_bottom);
            if (uf.connected(xyTo1D(r, c), topIdx))
                uf.union(local_bottom, bottomIdx);
        }
    }
    
    private void connectCells(int r1, int c1, int r2, int c2) {
        if (checkIndices(r2, c2) && isOpen(r2, c2)) {
            uf.union(xyTo1D(r1, c1), xyTo1D(r2, c2));
        }
        
    }
    
    private void connectNeighbors(int r1, int c1) {
        connectCells(r1, c1, r1 + 1, c1);
        connectCells(r1, c1, r1, c1 + 1);
        connectCells(r1, c1, r1 - 1, c1);
        connectCells(r1, c1, r1, c1 - 1);
        if(r1 == 1){
            uf.union(xyTo1D(r1, c1), topIdx);
        }
        checkBottomConnection(r1, c1);
        checkBottomConnection(r1 + 1, c1);
        
    }
    
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndices(row, col);
        if (!grid[row][col]) {
            grid[row][col] = true;
            nOpen++;
            connectNeighbors(row, col);
        }
    }
    
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return grid[row][col];
    }
   // is site (row, col) full?
    public boolean isFull(int row, int col)  {
        validateIndices(row, col);
        return uf.connected(xyTo1D(row, col), topIdx);
    }
    
   // number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }
    
    // does the system percolate?
    public boolean percolates() {
        return uf.connected(topIdx, bottomIdx);
    }
  // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(1);
        perc.open(0, 0);
        System.out.println("number of open sites: " + perc.numberOfOpenSites());
        System.out.println("percolates: " + perc.percolates());
    }
}