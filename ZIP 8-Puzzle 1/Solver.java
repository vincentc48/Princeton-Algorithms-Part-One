/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private final ArrayList<Board> solution;
    private final int numMoves;
    private final boolean solvable;

    public Solver(Board initial){

        if (initial==null) throw new IllegalArgumentException();

        Board twin = initial.twin();
        //System.out.println(twin);

        int m=0;
        int m2 =0;

        MinPQ<SearchNode>  pq = new MinPQ<SearchNode>(new byPriority());
        MinPQ<SearchNode> pq2 = new MinPQ<SearchNode>(new byPriority());


        boolean isSolvable = false;
        boolean isDone = false;
        SearchNode sol = null;

        pq.insert(new SearchNode(null,initial,0));//add on the first;
        pq2.insert(new SearchNode(null,twin,0));//same for twin

        while(!isDone){
            SearchNode min = pq.delMin();//extract the least priority
            //System.out.println("Next Move: "+min.b);
            if (min.b.isGoal()) {
                isSolvable = true;
                sol = min;
                isDone =true;
                break;
            }
            for (Board neighbor : min.b.neighbors()) { //then add all its neighbors
                if (min.previous==null||!neighbor.equals(min.previous.b)) pq.insert(new SearchNode(min, neighbor, min.moves+1));// avoid infinite loops by not inserting already traversed boards
            }


            //do the same for the twin
            SearchNode min2 = pq2.delMin();//extract the least priority
            //don't need to keep track of all moves for twin board
            if (min2.b.isGoal()) {
                isSolvable = false;
                isDone =true;
                break;
            }
            for (Board neighbor : min2.b.neighbors()) { //then add all its neighbors
                if (min2.previous==null||!neighbor.equals(min2.previous.b)) pq2.insert(new SearchNode(min2, neighbor, min2.moves+1));
            }
        }
        solvable = isSolvable;
        if (solvable){
            numMoves = sol.moves;
            solution = new ArrayList<Board>();
            while (sol.previous!=null){
                solution.add(0,sol.b);
                sol = sol.previous;
            }
            solution.add(0,sol.b);
        }
        else{
            solution = null;
            numMoves =-1;
        }




    }

    private class SearchNode{
        private final Board b;
        private final int manhattan;
        private final int priority;
        private final int moves;
        private final SearchNode previous;

        public SearchNode(SearchNode previous1, Board current, int moves1){
            previous = previous1;
            b = current;
            manhattan = b.manhattan();
            moves = moves1;
            priority = moves + manhattan;

            //System.out.println("Creating SearchNode with " + priority + " priority and board of:  " + b);
        }
    }

    //COME BACK TO THIS, Don't know how MinPQ uses the compare method, could be right or wrong, should work.
    private class byPriority implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b){
            return a.priority-b.priority;
        }
    }

    public int moves(){return numMoves;}

    public boolean isSolvable(){ return solvable;}

    public Iterable<Board> solution(){ return solution; }// an ArrayList is Iterable

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    /*
    public static void main(String[] args) {
        int[][] a = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,15,14,0}};
        Board b = new Board(a);
        Solver s = new Solver(b);
        System.out.println("Solution: "+s.solution());
        System.out.println("Moves: "+ s.moves());
        System.out.println("Is Solvable: " + s.isSolvable());
    }*/
}
