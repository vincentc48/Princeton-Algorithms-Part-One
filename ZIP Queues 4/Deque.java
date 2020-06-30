/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public Deque()
    {
        size =0;
        first =null;
        last =null;
    }

    private class Node<Item>{
        Item value;
        Node<Item> next;
        Node<Item> previous;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }

    public void addFirst(Item item){
        if (item==null) throw new IllegalArgumentException();
        Node<Item> f = new Node<Item>();
        f.value = item;
        if (size ==0) {
            first =f;
            last =f;
        }
        else{
            Node<Item> Oldfirst = first;
            first = f;
            f.next = Oldfirst;
            Oldfirst.previous=f;
        }
        size++;
    }

    public void addLast(Item item){
        if (item==null) throw new IllegalArgumentException();
        Node<Item> l = new Node<Item>();
        l.value = item;
        if (size ==0) {
            first =l;
            last =l;
        }
        else{
            Node<Item> Oldlast = last;
            last = l;
            Oldlast.next = l;
            l.previous=Oldlast;
        }
        size++;
    }

    public Item removeLast(){
        if (size ==0){
            throw new NoSuchElementException("");
        }
        else if(size ==1){
            Node<Item> Oldlast = last;
            first = null;
            last = null;
            size--;
            return Oldlast.value;
        }
        else {
            Node<Item> Oldlast = last;
            last = Oldlast.previous;
            last.next = null; // may cause a problem if first is null because you removed the only elemen
            size--;
            return Oldlast.value;
        }
    }

    public Item removeFirst(){
        if (size ==0){
            throw new NoSuchElementException("");
        }
        else if (size==1){
            Node<Item> Oldfirst = first;
            first = null;
            last = null;
            size--;
            return Oldfirst.value;
        }
        else{
            Node<Item> Oldfirst = first;
            first = Oldfirst.next;
            first.previous = null;// may cause a problem if first is null because you removed the only element
            size--;
            return Oldfirst.value;
        }
    }

    public Iterator<Item> iterator(){
        return new thisIterator();
    }

    private class thisIterator implements Iterator<Item>{

        private Node<Item> current = first;

        public boolean hasNext(){return current!=null;}
        public Item next(){
            if (!hasNext()) throw new NoSuchElementException("");
            Node<Item> n = current;
            current = current.next;
            return n.value;
        }
        public void remove(){
            throw new UnsupportedOperationException("");
        }
    }

    public static void main(String[] args){
        Deque<Integer> deque = new Deque<Integer>();
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.size());
        deque.addFirst(6);
        deque.addLast(2);
        for(Integer i:deque){
            StdOut.print(i+" ");
        }
        StdOut.println("");
        StdOut.println("Removing: "+deque.removeFirst());
    }
}
