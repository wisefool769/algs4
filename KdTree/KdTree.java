import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private Node root;

	private static class Node {
		private Point2D p;
		private RectHV bounds;
		private Node left;
		private Node right;
		private static double epsilon = 0.000001;
		private boolean cmpX;

		public Node(Point2D p, boolean cmpX, RectHV bounds) {
			this.p = p;
			this.bounds = bounds;
			this.cmpX = cmpX;			
		}

		public Double x() {
			return p.x();
		}

		public Double y() {
			return p.y();
		}

		public boolean pointEquals(Point2D p){
			return this.p.distanceSquaredTo(p) < epsilon;
		}

		public Point2D getPointCopy(){
			return new Point2D(x(), y());
		}

		public int compareTo(Point2D p) {
			int cmp;
			if(this.cmpX){
				cmp = x().compareTo(p.x());
			} else {
				cmp = y().compareTo(p.y());
			}
			return cmp;
		}

		public boolean cmpX() {
			return cmpX;
		}

		Point2D getPoint(){
			return p;
		}

		RectHV getBounds() {
			return this.bounds;
		}

		void draw() {
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.01);

			this.p.draw();

			StdDraw.setPenRadius();
			double x1, y1, x2, y2;
			if(this.cmpX) {
				StdDraw.setPenColor(StdDraw.RED);
				x1 = this.x();
				x2 = this.x();
				y1 = this.bounds.ymin();
				y2 = this.bounds.ymax();

			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				x1 = this.bounds.xmin();
				x2 = this.bounds.xmax();
				y1 = this.y();
				y2 = this.y();
			}

			StdDraw.line(x1, y1, x2, y2);

		}
	}

	// state to be tracked in the nearest method
	private static class NearState {
		private Point2D goal;
		private Point2D champion;
		private double distance;

		public NearState(Point2D goal) {
			this.goal = goal;
			this.champion = null;
			this.distance = Double.POSITIVE_INFINITY;
		}

		public Point2D getChampion() {
			return this.champion;
		}

		public void updateChampion(Point2D new_champ, double new_dist) {
			this.champion = new_champ;
			this.distance = new_dist;
		}

		public double getDistance() {
			return this.distance;
		}

		public Point2D getGoal() {
			return this.goal;
		}

	}

	private void validateArg(Object o, String err) {
		if(o == null){
			throw new NullPointerException(err);
		}
	}

	// construct an empty set of points 
	public KdTree() {
		root = null;
	}
	// is the set empty? 
	public boolean isEmpty() {
		return this.root == null;
	}

	private int size(Node node){
		if(node == null){
			return 0;
		}

		return 1 + size(node.left) + size(node.right);

	}

	// number of points in the set 
	public int size() {
		if(this.root == null){
			return 0;
		}

		return size(this.root);
	}


	private RectHV getNewBounds(Node node, int cmp) {
		final RectHV bounds = node.getBounds();
		double xmin = bounds.xmin();
		double ymin = bounds.ymin();
		double xmax = bounds.xmax();
		double ymax = bounds.ymax();

		if(node.cmpX()) {
			if(cmp < 0){
				xmax = Math.min(xmax, node.x());
			} else {
				xmin = Math.max(xmin, node.x());
			}
		} else {
			if(cmp < 0) {
				ymax = Math.min(ymax, node.y());
			} else {
				ymin = Math.max(ymin, node.y());
			}
		}

		return new RectHV(xmin, ymin, xmax, ymax);
	}

	private Node insert(Node node, Point2D p, boolean cmpX, RectHV bounds) {
		if(node == null){
			return new Node(p, cmpX, bounds);
		}

		int cmp = (-1) * node.compareTo(p);
		RectHV new_bounds = null;

		// System.out.println(node.getPoint().toString());
		// System.out.println("original: " + bounds.toString());
		// System.out.println("new: " + new_bounds.toString());

		if(cmp < 0) {
			if(node.left == null){
				new_bounds = getNewBounds(node, cmp);
			}
			node.left = insert(node.left, p, !cmpX, new_bounds);
			
		} else {
			if(node.right == null){
				new_bounds = getNewBounds(node, cmp);
			}
			node.right = insert(node.right, p, !cmpX, new_bounds);
		}
		return node;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		validateArg(p, "null argument supplied to KdTree.insert");
		RectHV bounds = new RectHV(0.0, 0.0, 1.0, 1.0);
		root = insert(this.root, p, true, bounds);
	}

	private void insert(double x, double y){
		insert(new Point2D(x, y));
	}

	private boolean contains(Node node, Point2D p, boolean cmpX){
		if(node == null) {
			return false;
		}

		if(node.pointEquals(p)){
			return true;
		}

		int cmp = (-1) * node.compareTo(p);
		if(cmp < 0) {
			return contains(node.left, p, !cmpX);
		} else {
			return contains(node.right, p, !cmpX);
		}
	}

	// does the set contain point p? 
	public boolean contains(Point2D p) {
		validateArg(p, "null argument supplied to KdTree.contains");
		return contains(root, p, true);
	} 

	private void draw(Node node){
		if(node == null)
			return;

		node.draw();
		draw(node.left);
		draw(node.right);

	}

	// draw all points to standard draw 
	public void draw() {
		draw(this.root);
	}

	// the state includes: the current node, and an arraylist to put everything in.
	private void range(Node node, ArrayList<Point2D> in_rect, RectHV rect){ 
		if(node == null) {
			return;
		}

		boolean cmp_left, cmp_right;

		if(rect.contains(node.getPoint())){
			in_rect.add(node.getPointCopy());
			cmp_left = true;
			cmp_right = true;
		} else {
			if (node.cmpX()) {
				cmp_left = rect.xmin() <= node.x();
				cmp_right = (rect.xmax() >= node.x());
			} else {
				cmp_left = rect.ymin() <= node.y();
				cmp_right = (rect.ymax() >= node.y());
			}
		}

		if(cmp_left){
			range(node.left, in_rect, rect);
		}

		if(cmp_right) {
			range(node.right, in_rect, rect);
		}

	}

	// // all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		validateArg(rect, "null argument supplied to KdTree.range");
		ArrayList<Point2D> in_rect = new ArrayList<Point2D>();
		range(this.root, in_rect, rect);
		return in_rect;
	}

	private NearState nearest(Node node, NearState state) {
		if(node == null) {
			return state;
		}

		double distance = node.getPoint().distanceSquaredTo(state.getGoal());
		if(distance < state.getDistance()) {
			state.updateChampion(node.getPointCopy(), distance);
		}

		Node close, far;
		if((node.cmpX() && (state.getGoal().x() < node.x())) || (!node.cmpX() && (state.getGoal().y() < node.y()))) {
			close = node.left;
			far = node.right;
		} else {
			close = node.right;
			far = node.left;
		}
		

		state = nearest(close, state);
		if(state.getDistance() < distance) {
			double planeDist;
			if(node.cmpX()) {
				planeDist = Math.abs(node.x() - state.getGoal().x());
			} else {
				planeDist = Math.abs(node.y() - state.getGoal().y());
			}

			if(planeDist < state.getDistance()){
				state = nearest(far, state);
			}

		} else {
			state = nearest(far, state);
		}

		return state;

	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		validateArg(p, "null argument supplied to KdTree.nearest");
		NearState initial = new NearState(p);
		return nearest(root, initial).getChampion();
	} 

	private static void testIsEmpty() {
		KdTree kdt = new KdTree();
		assert kdt.isEmpty();
	}

	private static void testSize() {
		KdTree kdt = new KdTree();
		assert kdt.size() == 0;
		Point2D p = new Point2D(0, 0);
		kdt.insert(p);
		assert kdt.size() == 1;
	}

	private static void testInsert() {
		KdTree kdt = new KdTree();
		try{
			kdt.insert(null);
			assert false;
		} catch (NullPointerException e){
		}

		
		kdt.insert(0, 0);
		kdt.insert(0.1, 0.1);
		kdt.insert(0.2, 0.2);
		kdt.insert(0.15, 0.15);
		assert kdt.size() == 4;

	}

	private static void testContains() {
		KdTree kdt = new KdTree();
		try{
			kdt.contains(null);
			assert false;
		} catch (NullPointerException e){
		}

		Point2D root = new Point2D(0.5, 0.5);
		Point2D pl = new Point2D(0.25, 0.25);
		Point2D pr = new Point2D(0.75, 0.75);
		Point2D prr = new Point2D(0.85, 0.65);
		Point2D prl = new Point2D(0.7, 0.6);
		Point2D pll = new Point2D(0.1, 0);
		Point2D plr = new Point2D(0.4, 0.4);

		kdt.insert(root);
		kdt.insert(pl);
		kdt.insert(pr);
		kdt.insert(prr);
		kdt.insert(prl);
		kdt.insert(pll);
		kdt.insert(plr);

		assert (kdt.contains(root));
		assert (kdt.contains(pl));
		assert (kdt.contains(pr));
		assert (kdt.contains(prr));
		assert (kdt.contains(prl));
		assert (kdt.contains(pll));
		assert (kdt.contains(plr));

		assert (!kdt.contains(new Point2D(0.4, 0.45)));
		assert (!kdt.contains(new Point2D(0, 0)));

	}

	private static <T> int iterSize(Iterable<T> iterable) {
		int ct = 0;
		for(T p : iterable) {
			ct++;
		}
		return ct;
	}

	private static void testRangeHelper(KdTree kdt, double xmin, double ymin, double xmax, double ymax, int sz) {
		RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
		Iterable<Point2D> kdr = kdt.range(rect);
		assert iterSize(kdr) == sz;
	}

	private static void testRange(){
		KdTree kdt = new KdTree();
		Point2D root = new Point2D(0.5, 0.5);
		Point2D pl = new Point2D(0.25, 0.25);
		Point2D pr = new Point2D(0.75, 0.75);
		Point2D prr = new Point2D(0.85, 0.65);
		Point2D prl = new Point2D(0.7, 0.6);
		Point2D pll = new Point2D(0.1, 0);
		Point2D plr = new Point2D(0.4, 0.4);

		kdt.insert(root);
		kdt.insert(pl);
		kdt.insert(pr);
		kdt.insert(prr);
		kdt.insert(prl);
		kdt.insert(pll);
		kdt.insert(plr);

		testRangeHelper(kdt, 0.45, 0.45, 0.55, 0.55, 1);
		testRangeHelper(kdt, 0, 0, 0.3, 0.3, 2);
		testRangeHelper(kdt, 0.69, 0.59, 0.86, 0.66, 2);

		testRangeHelper(kdt, 0.25, 0.5, 0.4, 0.75, 0);

	}

	private static void testNearest() {
		KdTree kdt = new KdTree();
		Point2D root = new Point2D(0.5, 0.5);
		Point2D pl = new Point2D(0.25, 0.25);
		Point2D pr = new Point2D(0.75, 0.75);
		Point2D prr = new Point2D(0.85, 0.65);
		Point2D prl = new Point2D(0.7, 0.6);
		Point2D pll = new Point2D(0.1, 0);
		Point2D plr = new Point2D(0.4, 0.4);

		kdt.insert(root);
		kdt.insert(pl);
		kdt.insert(pr);
		kdt.insert(prr);
		kdt.insert(prl);
		kdt.insert(pll);
		kdt.insert(plr);

		assert kdt.nearest(new Point2D(0.5, 0.5)).equals(root);

		assert kdt.nearest(new Point2D(0.7, 0.61)).equals(prl);
		assert kdt.nearest(new Point2D(0.0, 0.0)).equals(pll);

	}

	// unit testing of the methods (optional) 
    public static void main(String[] args) {
    	testIsEmpty();
    	testSize();
    	testInsert();
    	testContains();
    	testRange();
    	testNearest();
    }
}