public class Percolation
{
    private int[] board;
    private boolean[] openings;
    private int[] size;
    private int dimension; 
    private int numOpen;
    
    public Percolation(int n)
    {
        if (n<1) throw new IllegalArgumentException("");
        board = new int[n*n];
        size = new int[n*n];
        for (int i =0; i<board.length;i++){
            board[i]=i;
            size[i] =1;
        }
        openings = new boolean[n*n];
        dimension = n;
        numOpen = 0;
    }

    /*private void printBoard(){
      for (int i =0; i<dimension;i++){
        System.out.print("[");
        for (int v =0;v<dimension;v++){
          System.out.print(board[dimension*i+v]+" ,");
        }
        System.out.println("]");
      }
      for (int i =0; i<dimension;i++){
        System.out.print("[");
        for (int v =0;v<dimension;v++){
          System.out.print(size[dimension*i+v]+" ,");
        }
        System.out.println("]");
      }
      for (int i =0; i<dimension;i++){
        System.out.print("[");
        for (int v =0;v<dimension;v++){
          System.out.print(openings[dimension*i+v]+" ,");
        }
        System.out.println("]");
      }
    }*/
    
    private void union(int a , int b){
        int ra = root(a);
        int rb = root(b);
        if (ra==rb) return;
        else{
          if (size[ra]>size[rb]) {board[rb]=ra; size[ra]+=size[rb];}
          else{board[ra]=rb; size[rb] += size[ra];}
        }
    }
    
    private boolean isConnected(int a, int b){
        return root(a) == root(b);
    }
    
    private int root(int a){//takes the index in the array as an argument. first convert r,c to arrayIndex by arrayIndex(r,c) method
        while (board[a]!=a) a = board[a];
        return a;
    }
    
    public void open(int r, int c){
        //don't forget to make exceptions for corner and edge cases
        if (!isValid(r)||!isValid(c)) throw new IllegalArgumentException("");
        if(isOpen(r,c)) return;
        openings[arrayIndex(r,c)] = true;
        numOpen++;
        if(isValid(r)&&isValid(c+1)&&isOpen(r,c+1))
        {union(arrayIndex(r,c+1),arrayIndex(r,c));}

        if(isValid(r)&&isValid(c-1)&&isOpen(r,c-1))
        {union(arrayIndex(r,c-1),arrayIndex(r,c));}

        if(isValid(r+1)&&isValid(c)&&isOpen(r+1,c))
        {union(arrayIndex(r+1,c),arrayIndex(r,c));}

        if(isValid(r-1)&&isValid(c)&&isOpen(r-1,c))
        {union(arrayIndex(r-1,c),arrayIndex(r,c));}
    }
    
    public boolean isOpen(int r, int c){
        if (!isValid(r)||!isValid(c)) throw new IllegalArgumentException("");
        return openings[arrayIndex(r,c)];
    }
    
    public boolean isFull(int r, int c){
        if (!isValid(r)||!isValid(c)) throw new IllegalArgumentException("");
        int r1 = root(arrayIndex(r,c));
        for (int i =0;i<dimension;i++) if (isOpen(r,c)&&isOpen(1,i+1)&&root(i)==r1) return true; 
        return false;
    }
    
    public int numberOfOpenSites(){
        return numOpen;
    }
    
    public boolean percolates(){
        //this takes quadratic time. fix. probably use virtual node, but also store original values in an array so that you can return back to orginal array
        for (int n =1;n<=dimension;n++){
            if (isFull(dimension,n)) return true;
        }
        return false;
    }
    
    private int arrayIndex(int row, int column){
        if (!isValid(row)||!isValid(column)) throw new IllegalArgumentException("");
        else{
            return (row-1)*dimension+(column-1);
        }
    }
    
    private boolean isValid(int a){
        return (a>0&&a<=dimension);
    }
    
}
