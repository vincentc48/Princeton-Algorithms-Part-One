/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board
{
    private final int[][] board;
    private final int n;

    public Board(int[][] tiles)
    {
        if (tiles.length!=tiles[0].length) throw new IllegalArgumentException();
        n = tiles.length;
        board = new int[n][n];//cannot do board=tiles, because only the reference is final, once tiles is changed, the object at the reference will change, so it is not immutable this way.
        for (int i =0;i<n;i++){
            for (int v =0;v<n;v++){
                board[i][v] = tiles[i][v];
            }
        }
    }

    public String toString(){
        String x = n + "";
        for (int[] i: board){
            x +="\n";
            for (int v:i){
                x += " " + v;
            }
        }
        return x;
    }

    public int dimension(){
        return n;
    }

    public int hamming(){
        int count =0;
        int row =1;
        for (int[] i :board){
            int col =1;
            for (int v :i){
                if (v!=(row-1)*n+col&&v!=0) count++;
                col++;
            }
            row++;
        }
        return count;
    }

    public int manhattan(){
        int count=0;
        for (int i =0;i<n;i++){
            for(int v=0;v<n;v++){
                if (board[i][v]!=0) count += Math.abs((board[i][v]-1)%n+1-(v+1)) + Math.abs(Math.ceil((double)board[i][v]/n)-(i+1)); //differnce in row + difference in col.
            }
        }
        return count;
    }

    public boolean isGoal(){
        return (hamming()==0&&manhattan()==0);
    }



    //How do you get the board out of any object??
    public boolean equals(Object y){
        if (y==null) return false;
        if (this==y) return true;
        if (getClass()!=y.getClass()) return false;
        int[][] board2 = ((Board) y).board;
        if (n!=board2.length||n!=board2[0].length) return false;
        for (int i =0;i<n;i++){
            for (int v =0;v<n;v++){
                if (board[i][v]!=board2[i][v]) return false;
            }
        }
        return true;
    }

    /* How to implement equals method
    public boolean equals(Object o) {
    // self check
    if (this == o)
        return true;
    // null check
    if (o == null)
        return false;
    // type check and cast
    if (getClass() != o.getClass())
        return false;
    Person person = (Person) o;
    // field comparison
    return Objects.equals(firstName, person.firstName)
            && Objects.equals(lastName, person.lastName);
}
    *
    * */

    public Iterable<Board> neighbors(){
        return new iterableListOfNeighboringBoards(board);
    }

    private class iterableListOfNeighboringBoards implements Iterable<Board>{
        ArrayList<Board> l = new ArrayList<Board>();

        public iterableListOfNeighboringBoards(int[][] a){

            //checking for index of zero
            int z1, z2; //index of the zero;
            z1=z2=0;
            for(int i =0;i<a.length;i++){
                for (int v =0;v<a[0].length;v++){
                    if (a[i][v]==0) {z1=i; z2 =v;}
                }
            }

            //checking if adjacent square is in bounds, if so, then swap them and add the board to the list
            if (z1-1>=0) {
                l.add(new Board(swap(a,z1-1,z2,z1,z2)));
                swap(a,z1-1,z2,z1,z2);//then swap back because changes a by reference
            }
            if (z1+1<a.length) {
                l.add(new Board(swap(a,z1+1,z2,z1,z2)));
                swap(a,z1+1,z2,z1,z2);
            }
            if (z2-1>=0) {
                l.add(new Board(swap(a,z1,z2-1,z1,z2)));
                swap(a,z1,z2-1,z1,z2);
            }
            if (z2+1<a[0].length) {
                l.add(new Board(swap(a,z1,z2+1,z1,z2)));
                swap(a,z1,z2+1,z1,z2);
            }
        }

        public Iterator<Board> iterator(){
            return new boardIterator();
        }

        //WARNING: Does NOT pass by value, passes by refernce, so must swap back the argument arr above
        private int[][] swap(int[][] arr, int id1, int id2, int zeroid1, int zeroid2){
            /*System.out.println("Swapping: " + arr);
            System.out.println("Zero At: " + zeroid1+","+zeroid2);
            System.out.println("Switching to: " + id1 +","+ id2);*/
            int temp = arr[id1][id2];
            arr[id1][id2]=arr[zeroid1][zeroid2];
            arr[zeroid1][zeroid2]=temp;
            //System.out.println("successful!");
            return arr;
        }

        private class boardIterator implements Iterator<Board>{
            private int index=0;

            public boolean hasNext(){
                return (index<l.size());
            }
            public Board next(){
                if (index>=l.size()) throw new NoSuchElementException();
                return l.get(index++);
            }
            public void remove(){
                throw new UnsupportedOperationException();
            }
    }
    }

    public Board twin(){
        int[][] b2 = new int[n][n];// cannot just copy over b2 =board, because then changing the array which is referenced by board when you later change b2, mutating board.
        for (int i =0;i<n;i++){
            for (int v =0;v<n;v++){
                b2[i][v]=board[i][v];
            }
        }
        int i =0;
        while (b2[i][0]==0||b2[i][1]==0){
            i++;
        }
        int temp = b2[i][0];
        b2[i][0]=b2[i][1];
        b2[i][1]=temp;
        return new Board(b2);
    }

    public static void main(String[] args)
    {
        int[][] a = {{1,11,2,3},{4,5,0,12},{6,8,7,10},{9,13,15,14}};
        int[][] a2 = {{0,2},{1,3}};
        Board b = new Board(a2);
        Board b2 = new Board(a2);
        System.out.println(b.twin());
        a[0][0]=7;
        System.out.println(b);
        System.out.println(b.hamming() + " "+ b.manhattan());
        System.out.println(b.isGoal());
        System.out.println(b.equals(b2));
        System.out.println(b.dimension());
        for (Board neighbor: b.neighbors()){
            System.out.println(neighbor);
        }

    }
}
