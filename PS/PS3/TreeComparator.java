import java.util.Comparator;

/**
 *
 * Dartmouth CS 10, Winter 2025
 *
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 */

public class TreeComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) { //takes two objects
        //finds the frequencies of both objects:
        long f1 = ((BinaryTree<CodeTreeElement>)o1).getData().getFrequency();
        long f2 = ((BinaryTree<CodeTreeElement>)o2).getData().getFrequency();

        return Long.compare(f1, f2); //compare the two longs and return -1, 1, or 0 depending on their values
    }

}
