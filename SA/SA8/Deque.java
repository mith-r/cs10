public class Deque<T> implements SimpleDeque<T>{
    private Element head; //front of the linked list
    private Element tail; //tail of the linked list
    private int size; //size of linked list

    /**
     * The linked elements
     */

    private class Element {
        private T data;
        private Element next;
        private Element prev;

        public Element(T data){
            this.data = data;
            this.next = null;
            this.prev= null;
        }
    }

    public Deque(){ //initializes deque as empty
        head = null;
        tail = null;
    }

    /**
     * Adds data to beginning of deque
     * @param item - data to add
     */
    @Override
    public void addFirst(T item) { //Θ(1)
        Element newElement = new Element(item);
        if (head == null){
            head = newElement;
            tail = newElement;
        } else{
            newElement.next = head;
            head.prev = newElement;
            head = newElement;
        }
        size++;
    }

    /**
     * Adds data to end of deque
     * @param item - data to add
     */
    @Override
    public void addLast(T item) { //Θ(1)
        Element newElement = new Element(item);
        if (head == null){
            head = newElement;
            tail = newElement;
        } else{
            tail.next = newElement;
            newElement.prev = tail;
            tail = newElement;
        }
        size ++;
    }

    /**
     * retrieves data at beginning of deque and deletes
     * @return - data at beginning of deque
     * @throws Exception - if no data in deque
     */

    @Override
    public T removeFirst() throws Exception { //Θ(1)
        if (head == null){
            throw new Exception("No items!");
        }

        T res = head.data;
        head = head.next;
        size--;
        return res;
    }

    /**
     * retrieves data at end of deque and deletes
     * @return - data at end of deque
     * @throws Exception - if no data in deque
     */
    @Override
    public T removeLast() throws Exception { //Θ(1)
        if (tail == null){
            throw new Exception("No items!");
        }

        T res = tail.data;
        tail = tail.prev;
        size--;
        return res;
    }

    /**
     * retrieves data at beginning of deque
     * @return - data at beginning of deque
     * @throws Exception - if no data in deque
     */
    @Override
    public T getFirst() throws Exception { //Θ(1)
        if (head == null){
            throw new Exception("No items!");
        }

        return head.data;
    }

    /**
     * retrieves data at end of deque
     * @return - data at end of deque
     * @throws Exception - if no data in deque
     */
    @Override
    public T getLast() throws Exception { //Θ(1)
        if (tail == null){
            throw new Exception("No items!");
        }

        return tail.data;
    }

    /**
     * clears deque
     */
    @Override
    public void clear() { //Θ(1)
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * checks to see if item in deque
     * @param item - item to check if in deque
     * @return - boolean
     */
    @Override
    public boolean contains(T item) { //O(n)
        Element current = head;
        while (current != null){
            if (current.data.equals(item)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Checks to see if deque is empty
     * @return boolean
     */
    @Override
    public boolean isEmpty() { //Θ(1)
        return size() == 0;
    }

    /**
     * size of deque
     * @return int
     */
    @Override
    public int size() { //Θ(1)
        return size;
    }

    public String toString(){
        Element current = head;
        String res = "[/] <-> ";
        while (true){
            if (current == null){
                res +="[/]";
                break;
            }

            res += current.data;
            res += " <-> ";
            current = current.next;
        }

        return res;
    }

    public static void main(String[] args) throws Exception {
        Deque deque = new Deque(); //creates new Deque
        deque.addFirst(1); //Adds 1 to beginning of Deque
        deque.addLast(2); //Adds 2 to end 0f Deque
        System.out.println(deque); //Shows Deque
        System.out.println("Size: " + deque.size()); //Returns size (2)
        System.out.println();

        deque.addFirst(0); //Adds 0 to beginning of Deque;
        deque.addLast(3); //Adds 3 to end of Deque;
        System.out.println(deque); //Shows Deque;
        System.out.println("Size: " + deque.size()); //Returns size (4)
        System.out.println("Contains 3?: " + deque.contains(3)); //Returns if contains 3 (true);
        System.out.println("Contains 6?: " + deque.contains(6)); //Returns if contains 6 (false);
        System.out.println("Remove first: " + deque.removeFirst()); //Should return 0;
        System.out.println("Remove last: " + deque.removeLast()); //should return 3;
        System.out.println("Size: " + deque.size()); //Should return size as 2

    }
}
