import java.util.*;

public class InformationRetrieval {

    // TODO: Implement this method to return a set of unique words from a given text
    public static Set<String> getUniqueWords(String text) {
        // Use a Set to store unique words
        Set<String> res = new HashSet<>();
        String[] words = text.split("\\W+");
        for (String word: words){
            res.add(word);
        }
        return res;
    }

    // TODO: Implement this method to return a map of word counts from a given text
    public static Map<String, Integer> getWordCounts(String text) {
        // Use a Map to store word frequencies
        Map<String, Integer> res = new HashMap<>();
        String[] words = text.split("\\W+");
        for (String word: words){
            if (res.containsKey(word)){
                res.put(word, res.get(word) + 1);
            } else{
                res.put(word, 1);
            }
        }
        return res;
    }

    // TODO: Implement this method to return a map of word positions in a text
    public static Map<String, List<Integer>> getWordPositions(String text) {
        // Use a Map where the key is a word and the value is a list of positions
        Map<String, List<Integer>> res = new HashMap<>();
        String[] words = text.split("\\W+");
        for (int i = 0; i < words.length; i++){
            if (res.containsKey(words[i])){
                res.put(words[i], res.get(res.get(words[i]).add(i)));
            }
            ArrayList<Integer> position = new ArrayList<>();
            position.add(i);
            res.put(words[i],position);
        }
        return res;
    }

    // TODO: Implement this method to read a file and return its contents as a string
    public static String loadFileIntoString(String filename) throws Exception {
        return null;  // Placeholder
    }

    // TODO: Implement this method to search for a word in multiple documents and return filenames where it's found
    public static Set<String> searchSingleWord(String word, Map<String, Map<String, Integer>> documentIndex) {
        // Use a Set to store filenames that contain the word
        return null;  // Placeholder
    }

    // TODO: Implement this method to search for multiple words and return filenames where all words are found
    public static Set<String> searchMultipleWords(Set<String> words, Map<String, Map<String, Integer>> documentIndex) {
        // Perform set intersections to find documents containing all words
        return null;  // Placeholder
    }

    // TODO: Implement this method to calculate TF-IDF for a given word in a document
    public static double calculateTFIDF(String word, String filename, Map<String, Map<String, Integer>> documentIndex, int totalDocuments) {
        // Compute TF-IDF score
        return 0.0;  // Placeholder
    }

    // Main method to test the implementation
    public static void main(String[] args) throws Exception {
        // Example usage
        String sampleText = "To be or not to be, that is the question.";

        Set<String> uniqueWords = getUniqueWords(sampleText);
        System.out.println("Unique words: " + uniqueWords);

        Map<String, Integer> wordCounts = getWordCounts(sampleText);
        System.out.println("Word counts: " + wordCounts);

        Map<String, List<Integer>> wordPositions = getWordPositions(sampleText);
        System.out.println("Word positions: " + wordPositions);

        // Mock document index for testing
        Map<String, Map<String, Integer>> documentIndex = new HashMap<>();
        documentIndex.put("file1.txt", getWordCounts("This is a test document with test words"));
        documentIndex.put("file2.txt", getWordCounts("Another test document with different words"));

        Set<String> searchResult = searchSingleWord("test", documentIndex);
        System.out.println("Files containing 'test': " + searchResult);

        Set<String> searchMultiple = searchMultipleWords(new HashSet<>(Arrays.asList("test", "document")), documentIndex);
        System.out.println("Files containing both 'test' and 'document': " + searchMultiple);

        double tfidf = calculateTFIDF("test", "file1.txt", documentIndex, documentIndex.size());
        System.out.println("TF-IDF score for 'test' in file1.txt: " + tfidf);
    }
}