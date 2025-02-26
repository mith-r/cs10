import java.util.HashSet;
import java.util.Set;

public class Similarity {
    public double similarity(String A, String B){
        int common = 0;
        int unique = 0;
        char[] charA = A.toCharArray();
        char[] charB = B.toCharArray();
        Set<Character> commonSet = new HashSet<>();
        Set<Character> uniqueSet = new HashSet<>();
        for (char letter: charA){
            uniqueSet.add(letter);
        }
        for (char letter: charB){
            boolean both = uniqueSet.add(letter);
            if (!both){
                commonSet.add(letter);
            }
        }
        common += commonSet.size();
        unique += uniqueSet.size();
        double res = (double) common/unique;
        return res;
    }

    public static void main(String[] args) {
        Similarity sim = new Similarity();
        System.out.println(sim.similarity("night", "naught"));
    }
}
