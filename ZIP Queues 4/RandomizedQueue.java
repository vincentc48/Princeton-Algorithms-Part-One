/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private int size;
    private Item[] rq;

    public RandomizedQueue()
    {
        size =0;
        rq = (Item[]) new Object[2];
    }

    public boolean isEmpty(){
        return size ==0;
    }

    public int size(){
        return size;
    }

    public void enqueue(Item item){
        if (item==null) throw new IllegalArgumentException();
        size++;
        if (size>rq.length){
            resize(rq.length*2);
        }
        rq[size-1] = item;
    }

    public Item dequeue(){
        if (isEmpty()) throw new NoSuchElementException();
        StdRandom.shuffle(rq,0,size);
        Item i = rq[--size];
        if (size<rq.length/4) {
            resize(rq.length / 2);
        }
        rq[size]=null;
        return i;
    }

    public Item sample(){
        if (isEmpty()) throw new NoSuchElementException();
        return rq[(int)(Math.random()*size)];
    }

    private void resize(int newlength){
        Item[] newarray = (Item[]) new Object[newlength];
        int v =0;
        for (int i =0; i<rq.length;i++){
            if (rq[i]!=null){
                newarray[v] = rq[i];
                v++;
            }
        }
        rq = newarray;
    }

    public Iterator<Item> iterator(){
        StdRandom.shuffle(rq,0,size);
        return new ArrayIterator<Item>();
    }

    private class ArrayIterator<Item> implements Iterator<Item>{

        private int index=0;

        public boolean hasNext(){
            return index<size;
        }
        public Item next(){
            if (!hasNext()) throw new NoSuchElementException();
            else{
                return (Item) rq[index++];
            }
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args){
        RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
        StdOut.println(r.isEmpty());
        r.enqueue(7);
        for (Integer i: r) {
            StdOut.print(i + " ");
        }
        StdOut.println("size:" + r.size());
        StdOut.println("sample: "+r.sample());
        StdOut.println("removing: " + r.dequeue());
    }
}
