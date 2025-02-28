import java.util.*;
import java.io.*;

/**
 * @Author Mithun Rameshkumar
 * @Author Ashok Vasan
 *
 * The Viterbi class implements Viterbi decoding algorithm for Hidden Markov Models
 * It also builds a graph using transition and observation probabilities
 */
public class Viterbi {
    Map<String, Map<String, Double>> transitions; //Maps each state to possible next states with transition probabilities
    Map<String, Map<String, Double>> observations; //Maps each state to words and their observation probabilities

    public Viterbi(){
        transitions = new HashMap<>();
        observations = new HashMap<>();
    }

    /**
     * runs the Viterbi algorithm to determine the most likely sequence of tags for a given sentence
     *
     * @param sentence
     * @return A list of tags with the most probable sequence
     */
    public List<String> decodeViterbi(String sentence){
        String[] words = sentence.toLowerCase().split("\\s+");
        Map<String, Double> currScores = new HashMap<>();
        Set<String> currStates = new HashSet<>();
        currStates.add("#");
        currScores.put("#", 0.0);

        //Backtrace map to track the best previous state for each state at each word position
        Map<String, Map<Integer, String>> backTrace = new HashMap<>();

        //Compute probabilities for each state at each word position
        for (int i = 0; i < words.length; i ++){
            Set<String> nextStates = new HashSet<>();
            Map<String, Double> nextScores = new HashMap<>();

            for (String currState: currStates){
                if (transitions.containsKey(currState)) { //ensure current state has valid transitions
                    for (String nextState : transitions.get(currState).keySet()) {
                        nextStates.add(nextState);
                        Double currStateScore = currScores.get(currState);
                        Double transitionScore = transitions.get(currState).get(nextState);
                        Double observationScore = -100.0; //Default penalty for unseen words

                        //If word exists in observation update score
                        if (observations.containsKey(nextState) && observations.get(nextState).containsKey(words[i])) {
                            observationScore = observations.get(nextState).get(words[i]);
                        }

                        Double nextScore = currStateScore + transitionScore + observationScore;

                        // Update score if this path is the best found so far
                        if (!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)) {
                            nextScores.put(nextState, nextScore);

                            //initialize backtrace if not already present
                            if (!backTrace.containsKey(nextState)) {
                                Map<Integer, String> temp = new HashMap<>();
                                backTrace.put(nextState, temp);

                            }
                            backTrace.get(nextState).put(i, currState); // store the best previous state leading to the nextState and index i
                        }
                    }
                }
            }
            // Move to the next set for the next iteration
            currStates = nextStates;
            currScores = nextScores;
        }

        //Select the best final state with the highest probability
        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>((a,b) -> Double.compare(b.getValue(), a.getValue()));
        pq.addAll(currScores.entrySet());
        String bestFinalState = pq.poll().getKey();

        //Construct the best tag sequence from the end to the start
        String[] resultConstructor = new String[words.length];
        resultConstructor[words.length-1] = bestFinalState;

        for (int i = words.length - 1; i > 0; i --){
            resultConstructor[i-1] = backTrace.get(resultConstructor[i]).get(i);
        }

        // Convert array to a list and return sequence
        return new ArrayList<>(List.of(resultConstructor));
    }

    /**
     * Reads tags from file and populates transition probabilities
     * Counts are then converted to log probabilities
     * @param tagFile The file containing tag sequences
     * @throws IOException if an error occurs while reading the file
     */
    public void populateTransitions(String tagFile) throws Exception{
        try (BufferedReader reader = new BufferedReader(new FileReader(tagFile))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] tags = line.split("\\s+");
                String prevState = "#"; //Start state

                for (int i = 0; i < tags.length; i++){
                    String currState = tags[i];

                    //Update transition counts.
                    if (!transitions.containsKey(prevState)){
                        Map<String, Double> initialInsert = new HashMap<>();
                        initialInsert.put(currState, 1.0);
                        transitions.put(prevState, initialInsert);
                    } else if (!transitions.get(prevState).containsKey(currState)){
                        transitions.get(prevState).put(currState, 1.0);
                    } else{
                        transitions.get(prevState).put(currState, transitions.get(prevState).get(currState) + 1.0);
                    }

                    prevState = currState;
                }
            }

            //Convert counts into log probabilities
            for (Map<String, Double> map: transitions.values()){
                Double size = 0.0;
                for (Double num: map.values()){
                    size += num;
                }

                for (String key: map.keySet()){
                    Double score = Math.log(map.get(key)/size);
                    map.put(key, score);
                }
            }

        } catch (IOException e){
            throw new IOException(e);
        }
    }

    /**
     * Reads sentences and their corresponding tags from files and populates observation probabilities
     * Counts are converted into log probabilities for numerical stability.
     *
     * @param sentenceFile - The file containing sentences
     * @param tagFile - The file containing correspoinding tags
     * @throws IOException - if an error occurs while reading the files
     */

    public void populateObservations(String sentenceFile, String tagFile) throws Exception{
        try(BufferedReader sentenceReader = new BufferedReader(new FileReader(sentenceFile)); BufferedReader tagReader = new BufferedReader(new FileReader(tagFile))){
            String sentenceLine, tagLine;
            while ((sentenceLine = sentenceReader.readLine()) != null &&  (tagLine = tagReader.readLine()) != null){
                String[] word = sentenceLine.toLowerCase().split(" ");
                String[] tag = tagLine.split(" ");

                for (int i = 0; i <tag.length; i ++){
                    String currWord = word[i];
                    String currTag = tag[i];

                    // Update observation counts
                    if (!observations.containsKey(currTag)){
                        Map<String, Double> initialInsert = new HashMap<>();
                        initialInsert.put(currWord, 1.0);
                        observations.put(currTag, initialInsert);
                    } else if (!observations.get(currTag).containsKey(currWord)){
                        observations.get(currTag).put(currWord,1.0);
                    } else {
                        observations.get(currTag).put(currWord, observations.get(currTag).get(currWord) + 1.0);
                    }
                }
            }

            // Convert counts into log probabilities
            for (Map<String, Double> map: observations.values()){
                Double size = 0.0;
                for (Double num: map.values()){
                    size += num;
                }

                for (String key: map.keySet()){
                    Double score = Math.log(map.get(key)/size);
                    map.put(key, score);
                }
            }
        } catch (IOException e){
            throw new IOException(e);
        }
    }

}