/**
 * Implementation of Set ADT using BST
 * @author Mithun Rameshkumar
 */

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BSTSet<T extends Comparable<T>> implements SimpleSet<T>{
    public BST<T,String> root; //Creates BST called root
    public int size; //size of binary tree

    /**
     * initializes root as null and size as 0
     */

    public BSTSet(){
        root = null;
        size = 0;
    }

    /**
     * adds item to binary tree
     * @param item - item to add
     * @return - if item was added
     */
    @Override
    public boolean add(T item) {
        if (root == null){
            this.root = new BST<T,String>(item, null);
            this.size += 1;
            return true;
        }

        if (!this.contains(item)){
            this.root.insert(item,null);
            this.size += 1;
            return true;
        }

     return false;
    }

    /**
     * removes item from binary tree
     * @param item - item to remove
     * @return - if item was removed
     */
    @Override
    public boolean remove(T item) {
        if (root == null){
            return false;
        }

        try{
            this.root.delete(item);
            this.size --;
            return true;
        } catch (InvalidKeyException e) {
            return false;
        }
    }

    /**
     * clears binary tree
     */
    @Override
    public void clear() {
        this.root = null;
        size = 0;
    }

    /**
     * @return if item is in BST
     */
    @Override
    public boolean contains(T item) {
        if (root == null){
            return false;
        }

        if (this.isEmpty()){
            return false;
        }

        try{
            root.find(item);
            return true;
        } catch (InvalidKeyException e) {
            return false;
        }
    }

    /**
     * @return - if item is empty
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     *
     * @return - size of BST
     */
    @Override
    public int size() {
        return size;
    }


    /**
     *
     * @return BST's toString
     */
    public String toString(){
        return root.toString();
    }

    @Override
    public Iterator<T> iterator(){
        return new BSTSetIterator();
    }

    private class BSTSetIterator implements Iterator<T>{
        private List<T> list;
        private int index;
        private BST<T, String> current;

        public BSTSetIterator(){
            list = new ArrayList<>();
            inOrderTraversal(root,list);
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public T next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more values");
            }
            return list.get(index++);
        }
    }

    public void inOrderTraversal(BST<T, String> node, List<T> list){
        if (node == null) return;
        if (node.hasLeft()){
            inOrderTraversal(node.getLeft(), list);
        }
        list.add(node.getKey());
        if (node.hasRight()){
            inOrderTraversal(node.getRight(),list);
        }
    }

    public static void main(String[] args) {
        // Create a BSTSet of integers
        BSTSet<Integer> set = new BSTSet<>();

        System.out.println("Add 5: " + set.add(5));//adds 5 and prints "true"
        System.out.println("Add 2: " + set.add(2)); //adds 2 and prints "true"
        System.out.println("Add 9: " + set.add(9)); //adds 9 and prints "true"
        System.out.println("Add 2: " + set.add(2)); //should print "false"
        System.out.println();

        System.out.println(set); //Should show 5 as parent, then 2, then 9;
        System.out.println("Size: " + set.size()); //Should print "3"
        System.out.println("Contains 5: " + set.contains(5)); //Should print "true"
        System.out.println("Contains 1: " + set.contains(1)); //Should print "false"

        System.out.println("Remove 9: " + set.remove(9)); //Should print "true"
        System.out.println("Remove 2: " + set.remove(2)); //Should print "true"
        System.out.println("Remove 7: " + set.remove(7)); //Should print "false"
        System.out.println();

        System.out.println(set); //Should print 5
        System.out.println("Empty: " + set.isEmpty()); //Should print "false"
        System.out.println("Clearing...");
        set.clear();
        System.out.println("Empty: " + set.isEmpty()); //Should print "true"

        //Iterator test
        BSTSet<Integer> bst = new BSTSet<>();
        bst.add(5);
        bst.add(3);
        bst.add(7);
        bst.add(2);
        bst.add(4);

        Iterator<Integer> iterator = bst.iterator();

        for(int num: bst){
            System.out.print(num + " ");
        }



    }


}
