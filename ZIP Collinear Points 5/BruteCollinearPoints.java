/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;

public class BruteCollinearPoints {

    private final LineSegment[] collinearPoints;

    public BruteCollinearPoints(Point[] points){
        if (points==null) throw new IllegalArgumentException();
        for (int i =0;i<points.length;i++){
            if (points[i]==null) throw new IllegalArgumentException();
            for (int j =i-1;j>=0;j--){
                if (points[j].compareTo(points[i])==0) throw new IllegalArgumentException();
            }
        }
        ArrayList<LineSegment> a = new ArrayList<LineSegment>();
        for(int i =0; i<points.length;i++){
            for(int j=i+1;j<points.length;j++){
                for (int k =j+1;k<points.length;k++){
                    for (int l =k+1;l<points.length;l++){
                        if (isCollinear(points[i],points[j],points[k],points[l])){
                            LineSegment s = furthest(points[i],points[j],points[k],points[l]);
                            a.add(s);
                        }
                    }
                }
            }
        }
        collinearPoints = new LineSegment[a.size()];
        int index=0;
        for (LineSegment l:a) {
            collinearPoints[index] = l;
            index++;
        }
    }
    public int numberOfSegments(){
     return collinearPoints.length;
    }

    public LineSegment[] segments(){
        return collinearPoints;
    }

    private boolean isCollinear(Point a, Point b, Point c, Point d){
        return (a.slopeTo(b)==a.slopeTo(c)&&a.slopeTo(b)==a.slopeTo(c)&&a.slopeTo(b)==a.slopeTo(d));
    }

    private LineSegment furthest(Point a, Point b, Point c, Point d){//returns linesegment two ends of the line segment
        Point[] x = {a,b,c,d};
        Point max = new Point(0,0);
        Point min = new Point(0,0);
        for (Point p: x){
            int num1 =0;
            for (Point v:x){
                if (p.compareTo(v)>0) num1++;//might want to come back and change to < instead of <=
            }
            if (num1==3) max = p;
            if (num1==0) min = p;
        }
        return new LineSegment(min,max);
    }

    /*private static void main(String[] args){
        Point[] a = new Point[1000];
        for (int i =0;i<a.length;i++){
            a[i]= new Point(gen(1000),gen(1000));
        }
        long l = System.currentTimeMillis();
        BruteCollinearPoints b = new BruteCollinearPoints(a);
        System.out.println(Arrays.toString(b.segments()));
        System.out.println("Final Time :"+(System.currentTimeMillis()-l));
    }

    private static int gen(int num){
        return (int)(Math.random()*num)+1;
    }*/

}
