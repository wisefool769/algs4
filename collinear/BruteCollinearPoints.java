import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private LineSegment[] collinear_segs;
    public BruteCollinearPoints(Point[] points){
        if(points == null)
            throw new NullPointerException("");
        
        int N = points.length;
        ArrayList<LineSegment> col_segs = new ArrayList<LineSegment>();
        for(int i0 = 0; i0 < N; i0 ++){
            if (points[i0] == null)
                throw new NullPointerException("");
            for(int i1 = i0 + 1; i1 < N; i1 ++){
                if(points[i0].compareTo(points[i1]) == 0)
                    throw new IllegalArgumentException("");
                for(int i2 = i1 + 1; i2 < N; i2 ++){
                    if(points[i0].slopeOrder().
                           compare(points[i1], points[i2]) == 0) {
                        for(int i3 = i2 + 1; i3 < N; i3++) {
                            if(points[i0].slopeOrder().
                                   compare(points[i1], points[i3]) == 0) {
                                col_segs.add(getLS(points[i0],
                                                   points[i1],
                                                   points[i2],
                                                   points[i3]));
                                   }
                        }
                    }
                }
            }
        }

        collinear_segs = new LineSegment[col_segs.size()];
        for(int i = 0; i < col_segs.size(); i ++)
            collinear_segs[i] = col_segs.get(i);
    }
    
                            
    private  LineSegment getLS(Point p0, Point p1, Point p2, Point p3) {
        Point[] points = new Point[]{p0, p1, p2, p3};
        
        Point min_pt = p0;
        Point max_pt = p0;
        for(int i = 0; i < 4; i ++){
            if(points[i].compareTo(min_pt) < 0)
                min_pt = points[i];
            if(points[i].compareTo(max_pt) > 0)
                max_pt = points[i];
        }
        return new LineSegment(min_pt, max_pt);
    }
                                
   
   // the number of line segments
   public int numberOfSegments(){
       return collinear_segs.length;
   
   }
   
   // the line segments
   public LineSegment[] segments(){
       return collinear_segs;
   }
   
//   private static void main(String[] args) {
//       Point[] pts = new Point[4];
//       try { 
//           BruteCollinearPoints fail = new BruteCollinearPoints(pts);
//       } catch (NullPointerException e) {
//       }
//       
//       for(int i = 0; i != 4; i ++)
//           pts[i] = new Point(i, i);
//       BruteCollinearPoints BCP = new BruteCollinearPoints(pts);
//       LineSegment ls = BCP.getLS(pts[0], pts[1], pts[2], pts[3]);
//       assert ls.toString() == "(0, 0) -> (3, 3)";
//       int nsegs = BCP.numberOfSegments();
//       assert nsegs == 1;
//       
//       LineSegment [] segs = BCP.segments();
//       assert segs[0].toString() == "(0, 0) -> (3, 3)";
//       
//   }
   public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}
   
}
