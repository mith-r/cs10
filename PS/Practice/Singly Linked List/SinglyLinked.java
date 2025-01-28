class SinglyLinked<T> {
    private Element<T> head; // Head of the linked list
    private int size; // Size of the linked list

    // Constructor to initialize an empty list
    public SinglyLinked() {
        this.head = null;
        this.size = 0;
    }

    // Add elements to the list (for testing purposes)
    public void add(T data) {
        if (head == null) {
            head = new Element<>(data);
        } else {
            Element<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Element<>(data);
        }
        size++;
    }

    // Print the list (for testing purposes)
    public void printList() {
        Element<T> current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    // Create a palindromic linked list
    public SinglyLinked<T> palindrome() {
        if (this.head == null){
            return new SinglyLinked<T>();
        }
        SinglyLinked<T> res = new SinglyLinked<>();
        Element oCopy = this.head;
        res.head = new Element(oCopy.data, new Element(oCopy.data,null));
        Element resCopy = res.head;
        oCopy = oCopy.next;
        while(oCopy != null){
            resCopy.next = new Element(oCopy.data, new Element(oCopy.data, resCopy.next));
            oCopy = oCopy.next;
            resCopy = resCopy.next;
        }
        return res;
    }

    // Private nested Element class
    private static class Element<T> {
        T data; // Data stored in the node
        Element<T> next; // Pointer to the next node

        Element(T data) {
            this.data = data;
            this.next = null;
        }

        Element(T data, Element<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        SinglyLinked<Integer> list = new SinglyLinked<>();
        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println("Original List:");
        list.printList();

        SinglyLinked<Integer> palindromeList = list.palindrome();

        System.out.println("Palindromic List:");
        palindromeList.printList();
    }
}
