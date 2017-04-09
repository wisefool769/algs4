import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.ArrayList;

public class PointSET {
	private TreeSet<Point2D> points;

	// construct an empty set of points 
	public PointSET(){
		points = new TreeSet<Point2D>();
	}

	//is the set empty? 
	public boolean isEmpty(){
		return points.isEmpty();
	}      

	// number of points in the set 
	public int size() {
		return points.size();
	}

	private void validateArg(Object o, String err) {
		if(o == null){
			throw new NullPointerException(err);
		}
	}

	private Point2D newPoint(Point2D p){
		return new Point2D(p.x(), p.y());
	} 

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		validateArg(p, "argument to PointSET.insert was null");
		points.add(newPoint(p));
	}

	// does the set contain point p? // does the set contain point p? // does the set contain point p? // does the set contain point p? 
	public boolean contains(Point2D p) {
		validateArg(p, "argument to PointSET.contains was null");
		return points.contains(p);
	}

	// draw all points to standard draw 
	public void draw() {
		for(Point2D p : points) {
			p.draw();
		}
	}

                     
	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect)  {
		ArrayList<Point2D> in_rect = new ArrayList<Point2D>();
		for(Point2D p : points) {
			if(rect.contains(p)) {
				in_rect.add(newPoint(p));
			}
		}
		return in_rect;

	}           
	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		validateArg(p, "argument to PointSET.nearest was null");
		if (isEmpty()){
			return null;
		}

		Point2D ret = points.first();
		double dist = ret.distanceSquaredTo(p);
		for(Point2D point : points) {
			double pdist = point.distanceSquaredTo(p);
			if(pdist < dist){
				dist = pdist;
				ret = point;
			}
		}

		return newPoint(ret);
	}             

	// unit testing of the methods (optional) 

	private static void testIsEmpty(){
		PointSET ps = new PointSET();
		assert (ps.isEmpty());
		Point2D p1 = new Point2D(0.0, 0.0);
		ps.insert(p1);
		assert (!ps.isEmpty());
	}

	private static void testSize(){
		PointSET ps = new PointSET();
		assert (ps.size() == 0);
		Point2D p1 = new Point2D(0.0, 0.0);
		ps.insert(p1);
		assert (ps.size() == 1);

	}

	private static void testInsert(){
		PointSET ps = new PointSET();

		try {
			ps.insert(null);
			assert false;
		} catch(NullPointerException e){

		}
		Point2D p1 = new Point2D(0.0, 0.0);
		ps.insert(p1);
		ps.insert(p1);
		assert (ps.size() == 1);
	}

	private static void testContains(){
		PointSET ps = new PointSET();
		Point2D p1 = new Point2D(0.0, 0.0);
		ps.insert(p1);
		assert (ps.contains(p1));
	}

	private static void testDraw() {
		PointSET ps = new PointSET();
		for(int coord = 0; coord != 10; coord ++){
			Point2D p = new Point2D(coord / 10.0, coord / 10.0);
			ps.insert(p);
		}
		ps.draw();
	}

	private static void testNearest() {
		PointSET ps = new PointSET();
		Point2D p1 = new Point2D(0.0, 0.0);
		Point2D n11 = ps.nearest(p1);
		assert (n11 == null);
		ps.insert(p1);
		Point2D n12 = ps.nearest(p1);
		assert (n12.equals(p1));
		assert (!(n12 == p1));
	}

	private static <T> int iterSize(Iterable<T> iterable) {
		int ct = 0;
		for(T p : iterable) {
			ct++;
		}
		return ct;
	}

	private static void testRange() {
		PointSET ps = new PointSET();
		for(int coord = 0; coord != 10; coord ++){
			Point2D p = new Point2D(coord / 10.0, coord / 10.0);
			ps.insert(p);
		}

		RectHV rect = new RectHV(0.2, 0.2, 0.6, 0.6);
		Iterable<Point2D> ir1 = ps.range(rect);
		int isz = iterSize(ir1);
		assert iterSize(ir1) == 5;

		// check immutability
		for(Point2D p : ir1){
			p = null;
		}

		Iterable<Point2D> ir2 = ps.range(rect);
		assert iterSize(ir2) == 5;


	}

	public static void main(String[] args) {
		testIsEmpty();
		testSize();
		testInsert();
		testContains();
		// testDraw();
		testNearest();
		testRange();
	}
}
