/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> tree;

    public PointSET(){
        tree= new TreeSet<Point2D>();
    }

    public int size(){return tree.size(); }

    public boolean isEmpty(){return tree.isEmpty();}

    public void insert(Point2D p){
        if (p==null) throw new IllegalArgumentException();
        tree.add(p);
    }

    public boolean contains(Point2D p){
        if (p==null) throw new IllegalArgumentException();
        return tree.contains(p);
    }

    public void draw(){
        for (Point2D p:tree){
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect){
        if (rect==null) throw new IllegalArgumentException();
        if (isEmpty()) return new ArrayList<Point2D>();//empty arrayList
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        for(Point2D p:tree){
            if (rect.contains(p)) a.add(p);
        }
        return a;
    }

    public Point2D nearest(Point2D p){
        if (p==null) throw new IllegalArgumentException();
        if (isEmpty()) return null; //i don't know if this is what they want if there is an empty tree when this method is called
        double minDist= 100.0;//all in the unit square so maximum distance will just be sqrt(2)
        Point2D ans = tree.first();//can be any element, will always change to minimun distance element when the loop runs
        for(Point2D other:tree){
            if (p.distanceTo(other)<minDist) {
                minDist = p.distanceTo(other);
                ans = other;
            }
        }
        return ans;
    }


    public static void main(String[] args) {
        PointSET p = new PointSET();
        System.out.println(p.isEmpty());
        for (int i = 0; i < Math.random() * 10;i++)
            p.insert(new Point2D(randomRounded(),randomRounded()));
        p.draw();
        RectHV r = new RectHV(0.1,.3,.3,.7);
        r.draw();
        Point2D point = new Point2D(.5,.5);
        System.out.println(p.isEmpty());
        System.out.println(p.toString());
        System.out.println("Closest point to " + point + " is " + p.nearest(point));
        System.out.print("Contained in Rectangle " + r + " are " + p.range(r));
    }

    private static double randomRounded(){
        return ((int)(Math.random()*100))/100.0;
    }
}
