import java.io.*;
import java.util.*;

/**
 * Viterbi algorithm testing
 * Dartmouth CS 10, Winter 2025
 *
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 */

public class codeTesting {

    /**
     * file-based testing with tagged training and test datasets
     * @param file filename for the training and test datasets
     */
    public static void fileBasedTesting(String file) throws Exception {
        String sentenceTrainingFile = "texts/" + file + "-train-sentences.txt";
        String tagTrainingFile = "texts/" + file + "-train-tags.txt";
        String sentenceTestingFile = "texts/" + file + "-test-sentences.txt";
        String tagTestingFile = "texts/" + file + "-test-tags.txt";

        try (BufferedReader sentenceReader = new BufferedReader(new FileReader(sentenceTestingFile));
             BufferedReader tagReader = new BufferedReader(new FileReader(tagTestingFile))) {

            System.out.println("Testing " + file + "...");

            // initialize Viterbi test and populate transition and observation probabilities
            Viterbi test = new Viterbi();
            test.populateTransitions(tagTrainingFile);
            test.populateObservations(sentenceTrainingFile, tagTrainingFile);

            int total = 0;  // number of words processed
            int correct = 0; // correctly predicted tags

            String sentenceLine, tagLine;

            // read each line and tag lines from the files
            while (((sentenceLine = sentenceReader.readLine()) != null) &&
                    ((tagLine = tagReader.readLine()) != null)) {

                // get predicted tags using the Viterbi algorithm
                List<String> returned = test.decodeViterbi(sentenceLine);
                String[] expected = tagLine.split("\\s+"); // split expected tags by spaces

                // compare predicted tags with expected tags
                for (int i = 0; i < returned.size(); i++) {
                    total++;
                    if (returned.get(i).equals(expected[i])) {
                        correct++;
                    }
                }
            }

            // calculate accuracy percentage:
            double accuracy = (double) correct / (double) total;
            double percent = Math.round(accuracy * 100 * 100) / 100.0;

            System.out.println(percent + "% Accurate (" + correct  + "/" + total + ")");

        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    //interactive console-based testing where users can input sentences and the system predicts part-of-speech tags using the Viterbi algorithm
    public static void consoleBasedTesting() throws Exception {
        Viterbi test = new Viterbi();
        test.populateObservations("texts/brown-train-sentences.txt", "texts/brown-train-tags.txt");
        test.populateTransitions("texts/brown-train-tags.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Input Sentence: ");
        String input = scanner.nextLine(); // read user input

        // predict parts of speech for the input
        List<String> listTags = test.decodeViterbi(input);
        String[] words = input.split("\\s+"); // split input into words

        // create and print tagged sentence
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            result.append(words[i]).append("/").append(listTags.get(i)).append(" ");
        }

        System.out.println(result);

        scanner.close();
    }

    // test using hardcoded graph from the recitation
    public static void hardCodedGraph() {
        System.out.println("Testing Hardcoded Graph...");
        Viterbi training = new Viterbi();

        // define transition probabilities
        training.transitions.put("#", Map.of("NP", Math.log(1.0 / 2.0), "N", Math.log(1.0 / 2.0)));
        training.transitions.put("NP", Map.of("V", Math.log(1.0 / 2.0), "CNJ", Math.log(1.0 / 2.0)));
        training.transitions.put("N", Map.of("V", Math.log(1.0 / 2.0), "CNJ", Math.log(1.0 / 2.0)));
        training.transitions.put("CNJ", Map.of("NP", Math.log(1.0 / 3.0), "N", Math.log(1.0 / 3.0), "V", Math.log(1.0 / 3.0)));
        training.transitions.put("V", Map.of("N", Math.log(1.0 / 3.0), "NP", Math.log(1.0 / 3.0), "CNJ", Math.log(1.0 / 3.0)));

        // define observation probabilities
        training.observations.put("N", Map.of("cat", Math.log(1.0 / 3.0), "dog", Math.log(1.0 / 3.0), "watch", Math.log(1.0 / 3.0)));
        training.observations.put("V", Map.of("get", Math.log(1.0 / 3.0), "chase", Math.log(1.0 / 3.0), "watch", Math.log(1.0 / 3.0)));
        training.observations.put("CNJ", Map.of("and", Math.log(1.0)));
        training.observations.put("NP", Map.of("chase", Math.log(1.0)));

        // run test sentences through the hardcoded graph
        System.out.println("Test Sentence 1: chase and get and dog and cat");
        System.out.println("Expected output: [NP, CNJ, V, CNJ, N, CNJ, N]");
        System.out.println("Actual output:   " + training.decodeViterbi("chase and get and dog and cat"));

        System.out.println("\nTest Sentence 2: dog and cat watch and chase");
        System.out.println("Expected output: [N, CNJ, N, V, CNJ, NP]");
        System.out.println("Actual output:   " + training.decodeViterbi("dog and cat watch and chase"));
    }

    //executes all the test cases.
    public static void main(String[] args) throws Exception {
        // run hardcoded graph test
        hardCodedGraph();
        System.out.println();

        // perform  testing on simple and brown different datasets
//        fileBasedTesting("simple");
//        System.out.println();
//        fileBasedTesting("brown");
//        System.out.println();

        // run console-based testing
        System.out.println("Console Based Testing...");

            consoleBasedTesting();
    }
}