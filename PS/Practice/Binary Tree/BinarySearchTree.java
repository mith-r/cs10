public class BinarySearchTree {
    static class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    private Node root;

    // Insert a value into the BST
    public void insert(int value) {
        root = insertRec(root, value);
    }

    private Node insertRec(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }

        if (value < node.data) {
            node.left = insertRec(node.left, value);
        } else if (value > node.data) {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

    // Method to check if the tree is a valid BST
    public boolean isValidBST() {
        return isValidBSTRec(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isValidBSTRec(Node node, int min, int max) {
        if (node == null){
            return true;
        }

        if (node.data <= min || node.data >= max) {
            return false;
        }

        return isValidBSTRec(node.left, min, node.data) && isValidBSTRec(node.right, node.data, max);

    }



    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();

        // Create a valid BST
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        bst.insert(18);

        // Test isValidBST
        System.out.println(bst.isValidBST()); // Expected: true

        // Create an invalid BST (manually)
        BinarySearchTree invalidTree = new BinarySearchTree();
        Node root = new Node(10);
        root.left = new Node(5);
        root.right = new Node(15);
        root.left.right = new Node(12); // Invalid: 12 > 10
        root.right.left = new Node(8); // Invalid: 8 < 10
        invalidTree.root = root;

        System.out.println(invalidTree.isValidBST()); // Expected: false
    }
}