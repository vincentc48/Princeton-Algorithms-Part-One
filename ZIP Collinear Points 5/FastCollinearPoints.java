/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Comparator;

public class FastCollinearPoints {

    private LineSegment[] collinearPoints;

    public FastCollinearPoints(Point[] points){

        if (points==null) throw new IllegalArgumentException();
        for (int i =0;i<points.length;i++){
            if (points[i]==null) throw new IllegalArgumentException();
            for (int j =i-1;j>=0;j--){
                if (points[j].compareTo(points[i])==0) throw new IllegalArgumentException();
            }
        }

        //Step 2: loop through array proportional to NlogN time, and find all line segments, without duplicates
        ArrayList<LineSegment> a = new ArrayList<LineSegment>();
        int indexOfNotUse=0;
        Point[] notUse = new Point[points.length];
        for (Point p:points){
            notUse[indexOfNotUse++]=p;//after we check this point for all line segments going through it, we can't use it to check other line segments or else it will be counted as a duplicate
            int v =0;
            Point[] arr = new Point[points.length-1];
            for (Point s:points){//put in all other points into array arr besides p.
                if (s.compareTo(p)!=0){
                   arr[v]=s;
                   v++;
                }
            }

            sort(arr,p.slopeOrder());//sort the array of points by slope to p
            //System.out.println("Slope relative to "+p+" : "+ Arrays.toString(arr));
            //System.out.println("notUse:" + Arrays.toString(notUse));


            for (int i =0;i<arr.length-2;i++){
                if (arr[i].slopeTo(p)==arr[i+2].slopeTo(p)){//loop through to find line segments
                    boolean isDuplicate =false;
                    Point first = arr[i];//first point in line segment
                    if (contains(notUse,first)) isDuplicate = true;//must do this before the i++ in the loop
                    Point last;
                    while (i<arr.length-1&&arr[i].slopeTo(p)==arr[i+1].slopeTo(p)){//loop until last point in line segment (can be more than 4 points)
                        i++;//put this before, so the last element in the array that is on the linesegment will get checked
                        if(contains(notUse,arr[i])){//if any point on this line has already been checked, don't include this line because of duplicates
                            isDuplicate = true;
                            //don't break because "i" will increment, and we want to go past this line segment if duplicated
                        }
                    }

                    // these two cases check if the point being compared to is at an endpoint of the line segment
                    if (p.compareTo(first)<0) {
                        first =p; }
                    if (p.compareTo(arr[i])>0){//we can also say arr[i]=p too, because after i++, we don't check anthing i or before as we continue on in the loop
                        last=p; }
                    else{last=arr[i];}

                    //add all line segments that have 4 or more points collinear, and are not duplicates
                    if (!isDuplicate) {
                        a.add(new LineSegment(first,last));
                        //System.out.println("adding" + a.get(a.size()-1));
                    }
                }
            }
        }



        //Step 3: convert the ArrayList of LineSegments to an array and store it as the instance variable of all line segments
        collinearPoints = new LineSegment[a.size()];
        int index=0;
        for (LineSegment l: a){
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

    private static boolean contains(Point[] arr, Point a) {
        for (Point p : arr) {
            if (p!=null&&p.compareTo(a)==0) return true;
        }
        return false;
    }

    private static void sort(Point[] p, Comparator<Point> c){//sort by slopes relative to x
            Point[] aux = new Point[p.length];//sets all points to null
            for (int i =0;i<p.length;i++){//copy over to prevent nullPointerExceptions
                aux[i]=p[i];
            }
            sort(p,aux,0,p.length/2,c);
            sort(p,aux,p.length/2,p.length,c);
            merge(p,aux,0,p.length/2,p.length,c);
    }

    private static void sort(Point[] p, Point[] aux, int lo, int hi, Comparator<Point> c){
        if (Math.abs(hi-lo)<2) {
            aux[lo]=p[lo];
        }
        else{
            int mid = (hi+lo)/2;
            sort(p,aux,lo,mid,c);
            sort(p,aux,mid,hi,c);
            merge(p,aux,lo,mid,hi,c);
        }
    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Comparator<Point> c){
        int i,j;
        i = lo;
        j = mid;
        for (int v=lo;v<hi;v++){
            if (i>=mid) {aux[v] = a[j]; j++;}
            else if (j>=hi) {aux[v] = a[i]; i++;}
            else if (c.compare(a[i],a[j])<0) {aux[v]=a[i]; i++;}//if slope to a[i] is less than slope to a[j], a[i] comes first in sorted array
            else if (c.compare(a[i],a[j])>0) {aux[v]=a[j]; j++;}//if slope to a[j] is greater, then a[j] comes first;
            else{//if equal slopes, sort by increasing y value;
                if (a[i].compareTo(a[j])<0){aux[v]=a[i]; i++;}//comparing y values (or x values if horizontal line) of points now, using Point.compareTo(Point that)
                else { aux[v]=a[j];j++;}//otherwise, put j in first. Can't have two of the same points
            }
        }
        for (int v=lo;v<hi;v++){//only copy the sorted portion of aux into a
            a[v]=aux[v];
        }
    }
    /*
    private static void main(String[] args) {
        double[] d = new double[101];
        for (int i =0;i<d.length;i++){
            d[i]=g(1000);
        }
        System.out.println(Arrays.toString(d));
        for (int i =0;i<a.length;i++){
            a[i]= new Point(g(30),g(30));
        }

        Point[] a = new Point[10000];
        for (int i =0;i<a.length;i++){
            a[i]= new Point(g(10000),g(10000));
        }

        Point[] a = {new Point(1,3),new Point(3,4),new Point(5,5),new Point(7,6),new Point(9,7),new Point(11,8),new Point(15,14),new Point(8,9), new Point(5,10),new Point(2,11)};

        long mil = System.currentTimeMillis();
        FastCollinearPoints b = new FastCollinearPoints(a);
        System.out.println("Final Result: "+ Arrays.toString(b.segments()));
        System.out.println("Time in millis: "+ (System.currentTimeMillis()-mil));
    }

    private static int g(int num){
        return (int)(Math.random()*num)+1;
    }*/
}
