import java.util.NoSuchElementException;

public class BST<K extends Comparable<K>, V> {
    private K key;
    private V value;
    private BST<K, V> left, right;

    public BST(K key, V value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public BST(K key, V value, BST<K, V> left, BST<K, V> right) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return !hasLeft() && !hasRight();
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public V find(K search) throws NoSuchElementException {
        if (key.equals(search)) {
            return value;
        }

        if (search.compareTo(key) < 0) {
            if (hasLeft()) return left.find(search);
        } else {
            if (hasRight()) return right.find(search);
        }

        throw new NoSuchElementException("Key not found: " + search);
    }

    public K min() {
        if (!hasLeft()) {
            return key;
        }
        return left.min();
    }

    public K minIter() {
        BST<K, V> res = this;
        while (res.hasLeft()) {
            res = res.left;
        }
        return res.key;
    }

    public void insert(K newKey, V newValue) {
        if (newKey.compareTo(this.key) < 0) {
            if (hasLeft()) {
                left.insert(newKey, newValue);
            } else {
                left = new BST<>(newKey, newValue);
            }
        } else if (newKey.compareTo(this.key) > 0) {
            if (hasRight()) {
                right.insert(newKey, newValue);
            } else {
                right = new BST<>(newKey, newValue);
            }
        } else {
            this.value = newValue; // Update value if key already exists
        }
    }

    public String toString() {
        String leftStr = hasLeft() ? left.toString() : "-";
        String rightStr = hasRight() ? right.toString() : "-";
        return key + " (" + leftStr + ", " + rightStr + ")";
    }

    public void inOrderTraversal() {
        if (hasLeft()) left.inOrderTraversal();
        System.out.print(key + " ");
        if (hasRight()) right.inOrderTraversal();
    }

    public int countNodes() {
        int leftCount = hasLeft() ? left.countNodes() : 0;
        int rightCount = hasRight() ? right.countNodes() : 0;
        return 1 + leftCount + rightCount;
    }

    public int height() {
        int leftHeight = hasLeft() ? left.height() : -1;
        int rightHeight = hasRight() ? right.height() : -1;
        return 1 + Math.max(leftHeight, rightHeight);
    }

    public boolean isBalanced() {
        if (isLeaf()) return true;

        int leftHeight = hasLeft() ? left.height() : -1;
        int rightHeight = hasRight() ? right.height() : -1;

        return Math.abs(leftHeight - rightHeight) <= 1 &&
                (hasLeft() ? left.isBalanced() : true) &&
                (hasRight() ? right.isBalanced() : true);
    }

    // ❗❗ YOUR TASK: Implement delete(K search) ❗❗
    public BST<K, V> delete(K search) throws NoSuchElementException {
        // COMPLETE: Implement the delete function
        if (search.compareTo(key) < 0){
            if (hasLeft()){
                left = left.delete(search);
            } else{
                throw new NoSuchElementException();
            }
        }

        if (search.compareTo(key) > 0){
            if (hasRight()){
                right = right.delete(search);
            } else{
                throw new NoSuchElementException();
            }
        }

        if (search.compareTo(key) == 0){
            if (isLeaf()){
                return null;
            }

            if (hasLeft() && !hasRight()){
                return left;
            }

            if (!hasLeft() && hasRight()){
                return right;
            }

            if (hasLeft() && hasRight()){
                K successorKey = right.min();
                V successorValue = right.find(successorKey);

                this.key = successorKey;
                this.value = successorValue;
                right = right.delete(successorKey);
            }
        }
        return this;
    }

    // Test methods
    public static void main(String[] args) {
        testBST();
    }

    public static void testBST() {
        BST<Integer, String> tree = new BST<>(10, "Root");
        tree.insert(5, "Left");
        tree.insert(15, "Right");
        tree.insert(2, "Left-Left");
        tree.insert(7, "Left-Right");
        tree.insert(12, "Right-Left");
        tree.insert(17, "Right-Right");

        System.out.println("=== BST Structure ===");
        System.out.println(tree.toString());

        testFind(tree);
        testMin(tree);
        testTreeProperties(tree);

        System.out.println("\n=== Now, Test Delete Function ===");
        testDelete(tree);
    }

    public static void testFind(BST<Integer, String> tree) {
        System.out.println("\n=== Find Tests ===");
        try {
            System.out.println("Find 7: " + tree.find(7)); // Expected: "Left-Right"
            System.out.println("Find 15: " + tree.find(15)); // Expected: "Right"
            System.out.println("Find 20: " + tree.find(20)); // Expected: Exception
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void testMin(BST<Integer, String> tree) {
        System.out.println("\n=== Min Tests ===");
        System.out.println("Min (Recursive): " + tree.min()); // Expected: 2
        System.out.println("Min (Iterative): " + tree.minIter()); // Expected: 2
    }

    public static void testTreeProperties(BST<Integer, String> tree) {
        System.out.println("\n=== Tree Properties ===");
        System.out.println("Node Count: " + tree.countNodes()); // Expected: 7
        System.out.println("Tree Height: " + tree.height()); // Expected: 2 (balanced BST)
        System.out.println("Is Balanced? " + tree.isBalanced()); // Expected: true
    }

    public static void testDelete(BST<Integer, String> tree) {
        System.out.println("\n=== DELETE TESTS ===");

        // COMPLETE: Test deleting elements (leaf, one child, two children)
        System.out.println("Tree before deletion: " + tree.toString());

        // Delete leaf node
        tree.delete(7);
        System.out.println("After deleting leaf (7): " + tree.toString());

        // Delete node with one child
        tree.delete(5);
        System.out.println("After deleting node with one child (5): " + tree.toString());

        // Delete node with two children
        tree.delete(10);
        System.out.println("After deleting node with two children (10): " + tree.toString());

        // COMPLETE: Add more cases if needed
    }
}