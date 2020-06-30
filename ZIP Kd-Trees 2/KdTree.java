/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int size;

    private class Node{
        private Point2D value;
        private Node left,right;
        private boolean isVert;

        public Node(Point2D v,boolean hv){
            value = v;
            isVert = hv;
        }
    }

    public KdTree(){
        size=0;
    }

    public int size(){return size;}

    public boolean isEmpty(){return size==0;}

    public void insert(Point2D p){
        if (p==null) throw new IllegalArgumentException();
        if (contains(p)) return;
        if (isEmpty()) root = new Node(p,true);
        else{
            insert(p,root);
        }
        size++;
    }

    private void insert(Point2D p, Node current){// must pass Node current by reference for this to work
        if(current.isVert){//then search the x values
            if (p.x()<current.value.x()){
                if (current.left==null) current.left = new Node(p,!current.isVert);//opposite of isVert
                else insert(p,current.left);// if child is not null, continue going down the tree.
            }
            else{
                if (current.right==null) current.right = new Node(p,!current.isVert);
                else insert(p,current.right);
            }
        }
        else{//if is horizontal, then search the y values
            if (p.y()<current.value.y()){
                if (current.left==null) current.left = new Node(p,!current.isVert);
                else insert(p,current.left);
            }
            else{
                if (current.right==null) current.right = new Node(p,!current.isVert);
                else{ insert(p,current.right);}
            }
        }
    }

    public boolean contains(Point2D p){
        if (p==null) throw new IllegalArgumentException();
        if (root==null) return false;
        return search(p,root);
    }

    private boolean search(Point2D query, Node current) {//recursive method to check if query is in the tree, by going down Nodes, "current" each time;
        if (current.value.equals(query)) return true;
        if (current.isVert){
            if (query.x()<current.value.x()){
                if (current.left==null) return false;//search miss, if we reach a null Node where the query should have been in the tree, we have not found it.
                else return search(query, current.left);//else keep going down the tree
            }
            else{
                if (current.right==null) return false;
                else return search(query, current.right);
            }
        }
        else{
            if (query.y()<current.value.y()){
                if (current.left==null) return false;
                else return search(query,current.left);
            }
            else{
                if (current.right==null) return false;
                else return search(query, current.right);
            }
        }
    }

    public void draw(){
        if (size==0) return;
        searchBelow(root);
    }

    private void searchBelow(Node current){//iterates through tree recursively and draws each node along the way
            if (current.left != null) searchBelow(current.left);
            action(current);//action between searching left and right, in order
            if (current.right != null) searchBelow(current.right);
    }

    private void action(Node n){//action performed when we iterate and hit each node
        n.value.draw();
    }

    public Iterable<Point2D> range(RectHV rect){
        if (rect==null) throw new IllegalArgumentException();
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        if(isEmpty()) return a;
        return rangeSearch(a,rect,root);
    }

    private ArrayList<Point2D> rangeSearch(ArrayList<Point2D> a, RectHV r, Node current){//must pass a in by reference
        if (r.contains(current.value)) a.add(current.value);
        if (current.isVert){
            if (r.xmin()<=current.value.x()&&current.left!=null) a= rangeSearch(a,r,current.left);//set a to a plus any other points found in this subtree
            if (r.xmax()>=current.value.x()&&current.right!=null) a= rangeSearch(a,r,current.right);//now search right subtree if rectangle could be there
        }
        else{
            if (r.ymin()<=current.value.y()&&current.left!=null) a= rangeSearch(a,r,current.left);
            if (r.ymax()>=current.value.y()&&current.right!=null) a= rangeSearch(a,r,current.right);
        }
        return a;
    }

    public Point2D nearest(Point2D p){
        if (p==null) throw new IllegalArgumentException();
        if (isEmpty()) return null;//i don't know if this is what they want if there is an empty tree when this method is called
        return nearestSearch(p,root.value,root);
    }

    private Point2D nearestSearch(Point2D query, Point2D champion,Node current){
        if (current.value.distanceSquaredTo(query)<champion.distanceSquaredTo(query)) champion = current.value; //if the current node is closer, then it is the new champion
        if (current.isVert){
            if (query.x()<current.value.x()) {//if query node on the left, search left subtree to see if there is a new champion
                if (current.left!=null)champion = nearestSearch(query,champion,current.left);
                if (champion.distanceSquaredTo(query)>Math.pow(query.x()-current.value.x(),2.0)&&current.right!=null) champion = nearestSearch(query,champion,current.right);//if there could still be a point closer than the current champion from the already searched left subtree in the right subtree, than do a right subtree search
            }
            else if (query.x()>=current.value.x()) {// else, (if right subtree is not null) search right subtree.
                if (current.right!=null)champion = nearestSearch(query,champion,current.right);
                if (champion.distanceSquaredTo(query)>Math.pow(current.value.x()-query.x(),2.0)&&current.left!=null) champion = nearestSearch(query, champion,current.left);//search left if there still is a possibility that a closer point can be found, just like above
            }
        }
        else{
            if (query.y()<current.value.y()) {
                if (current.left!=null) champion = nearestSearch(query, champion, current.left);
                if (query.distanceSquaredTo(champion)>Math.pow(current.value.y()-query.y(),2.0)&&current.right!=null) champion = nearestSearch(query,champion,current.right);
            }
            else if (query.y()>=current.value.y()){
                if (current.right!=null) champion = nearestSearch(query, champion,current.right);
                if (query.distanceSquaredTo(champion)>Math.pow(current.value.y()-query.y(),2.0)&&current.left!=null) champion = nearestSearch(query, champion,current.left);
            }
        }
        return champion;
    }

    public static void main(String[] args) {
        KdTree k = new KdTree();
        k.insert(new Point2D(0.56,0.72));
        k.insert(new Point2D(0.56,0.5));
        k.insert(new Point2D(0.56,0.8));
        k.insert(new Point2D(0.56,0.03));
        k.insert(new Point2D(0.56,0.61));
        k.insert(new Point2D(0.56,0.34));
        System.out.println(k.range(new RectHV(0.02,0.03,0.56,0.80)));
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        for(int i =0;i<10;i++){
            Point2D p = new Point2D(randomRounded(),randomRounded());
            a.add(p);
            k.insert(p);
            System.out.println(k.size());
        }
        System.out.println(a);
        for (Point2D p: a){
            System.out.print(k.contains(p));
        }
        System.out.println(k.size());
        System.out.println(k.isEmpty());
        k.draw();
    }

    private static double randomRounded(){
        return ((int)(Math.random()*100))/100.0;
    }
}
