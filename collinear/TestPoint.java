import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class TestPoint{
    @Test
    public void testCompareTo(){
        Point p1 = new Point(1,2);
        Point p2 = new Point(1,2);
        assertEquals(0, p1.compareTo(p2));
        
        Point p3 = new Point(1,3);
        assertTrue(p1.compareTo(p3) < 0);
        assertTrue(p3.compareTo(p1) > 0);
        
        Point p4 = new Point(2,3);
        assertTrue(p3.compareTo(p4) < 0);
        assertTrue(p4.compareTo(p3) > 0);
    }
    
    @Test
    public void testSlopeTo(){
        double epsilon = 1e-5;
        Point p1 = new Point(1,1);
        assertEquals(Double.NEGATIVE_INFINITY, p1.slopeTo(p1), epsilon);
        
        Point p3 = new Point(1, 4);
        assertEquals(Double.POSITIVE_INFINITY, p1.slopeTo(p3), epsilon);
        
        Point p4 = new Point(2, 2);
        assertEquals(1.0, p1.slopeTo(p4), epsilon);
    }
    
    @Test
    public void testSlope2() {
        double epsilon = 1e-5;
        Point p1 = new Point(19000,10000);    
        Point p2 = new Point(1234, 5678);
        assertEquals(0.2432736688, p1.slopeTo(p2), epsilon);
    }
    
    @Test
    public void testSlopeOrder(){
        double epsilon = 1e-5;
        Point p1 = new Point(1,1);
        Comparator<Point> SlopeOrder = p1.slopeOrder();
        assertEquals(0, SlopeOrder.compare(p1,p1));
        
        Point p2 = new Point(1, 2);
        assertTrue(SlopeOrder.compare(p1, p2) < 0);
        assertTrue(SlopeOrder.compare(p2, p1) > 0);
        
        Point p3 = new Point(2, 1);
        Point p4 = new Point(2, 2);
        assertTrue(SlopeOrder.compare(p3, p4) < 0);
    }
}