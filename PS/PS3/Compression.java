import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 *
 * Compress and decompress files
 * Dartmouth CS 10, Winter 2025
 *
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 */

public class Compression implements Huffman {

    /**
     * Read file provided in pathName and count how many times each character appears
     *
     * @param pathName - path to a file to read
     * @return - Map with a character as a key and the number of times the character appears in the file as value
     * @throws IOException
     */
    @Override
    public Map<Character, Long> countFrequencies(String pathName) throws IOException {
        Map<Character, Long> frequencies = new HashMap<>(); //initialize a new hash map that stores characters and longs

        try (BufferedReader input = new BufferedReader(new FileReader(pathName))) { //attempts to open a file to read using a BufferedReader
            StringBuilder str = new StringBuilder(); //initialize a string builder to store the contents of the file
            String line;
            boolean firstLine = true;

            //append each line of the file to the string builder:
            while ((line = input.readLine()) != null) {
                if (!firstLine){
                    str.append("\n");
                }
                str.append(line);
                firstLine=false;
            }

            char[] characters = str.toString().toCharArray(); //store the contents of str in a character array

            //iterate over every character in the character array and update each character's frequencies in the HashMap
            for (char character : characters) {
                if (frequencies.containsKey(character)) {
                    frequencies.put(character, frequencies.get(character) + 1); //update value of existing key
                } else {
                    frequencies.put(character, 1L); //create a new key value pair, with the key equal to the character, and the value equal to 1
                }
            }
            return frequencies;

            //catches and rethrows any exceptions (FileNotFound, AccessDenied, etc.):
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Construct a code tree from a map of frequency counts. Note: this code should handle the special
     * cases of empty files or files with a single character.
     *
     * @param frequencies a map of Characters with their frequency counts from countFrequencies
     * @return the code tree.
     */
    @Override
    public BinaryTree<CodeTreeElement> makeCodeTree(Map<Character, Long> frequencies) {
        PriorityQueue<BinaryTree<CodeTreeElement>> pq = new PriorityQueue<>(new TreeComparator()); //creates a priority queue that will store binary tree nodes and uses TreeComparator for ordering

        //converts each character-value paid from the hash map into a code tree element and adds it to the priority queue
        for (Map.Entry<Character, Long> entry : frequencies.entrySet()) {
            CodeTreeElement ct = new CodeTreeElement(entry.getValue(), entry.getKey());
            pq.add(new BinaryTree<>(ct));
        }

        while (pq.size() > 1) { //will keep running until the PQ has only one element
            //find the two elements with the smallest frequencies in the PQ AND remove them from PQ:
            BinaryTree<CodeTreeElement> t1 = pq.poll();
            BinaryTree<CodeTreeElement> t2 = pq.poll();

            //merge the two smallest trees elements into a new binary tree. smaller one on the left, larger one on the right:
            BinaryTree<CodeTreeElement> tree = new BinaryTree<>(new CodeTreeElement(t1.getData().getFrequency() + t2.getData().getFrequency(), null));
            tree.setLeft(t1);
            tree.setRight(t2);

            pq.add(tree); // add the merged tree to our priority queue
        }

        BinaryTree<CodeTreeElement> res = pq.poll(); //create a new binary tree that will hold the only remaining element from the priority queue
        return res;
    }

    /**
     * Computes the code for all characters in the tree and enters them
     * into a map where the key is a character and the value is the code of 1's and 0's representing
     * that character.
     *
     * @param codeTree the tree for encoding characters produced by makeCodeTree
     * @return the map from characters to codes
     */
    @Override
    public Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree) {
        Map<Character, String> res = new HashMap<>(); //initialize a map that will hold characters and strings
        computeCodesHelper(codeTree, "", res);
        return res;
    }

    /**
     * helper for computeCodes (accumulator method)
     *
     * @param node   - node of binary tree to go to
     * @param prefix - bits to write to map
     * @param map    - map to write to
     */
    private void computeCodesHelper(BinaryTree<CodeTreeElement> node, String prefix, Map<Character, String> map) {

        //If only one character, just append 0
        if (node.isLeaf() && prefix.isEmpty()){
            map.put(node.getData().getChar(),"0");
            return;
        }

        CodeTreeElement element = node.getData(); //finds the data stored in the node

        if (element.getChar() != null) {
            map.put(element.getChar(), prefix); //store the node in the map
            return;
        }

        //recursively traverse the tree, assigning 0 for left and 1 for right:
        computeCodesHelper(node.getLeft(), prefix + "0", map);
        computeCodesHelper(node.getRight(), prefix + "1", map);
    }

    /**
     * Compress the file pathName and store compressed representation in compressedPathName.
     *
     * @param codeMap            - Map of characters to codes produced by computeCodes
     * @param pathName           - File to compress
     * @param compressedPathName - Store the compressed data in this file
     * @throws IOException
     */
    @Override
    public void compressFile(Map<Character, String> codeMap, String pathName, String compressedPathName) throws IOException {
        BufferedReader input = null; //initialize file reader
        BufferedBitWriter bitOutput = null; //initialize bit writer

        try {
            //open input and output files:
            input = new BufferedReader(new FileReader(pathName));
            bitOutput = new BufferedBitWriter(compressedPathName);
            boolean firstLine = true;

            String line = ""; //string that will store the contents of each line

            while ((line = input.readLine()) != null) { //loop through each line

                if (firstLine){
                    firstLine = false;
                } else {
                    line = "\n" + line;
                }

                char[] characters = line.toCharArray(); //convert the contents of this line to an array of character

                //convert all the characters in each line into binary code and write it as bits:
                for (char c : characters) {
                    String value = codeMap.get(c); //
                    for (int i = 0; i < value.length(); i++) {
                        char bitToWrite = value.charAt(i);
                        bitOutput.writeBit(bitToWrite == '1');
                    }
                }
            }
        } catch (IOException e) { //catches and rethrows any IOException
            throw new IOException(e);
        } catch (NullPointerException n){ //catches and rethrows NullPointerExceptions
            throw new NullPointerException();
        }
        finally {
            //close all files regardless of whether an exception occurred:
            if (input != null) input.close();
            if (bitOutput != null) bitOutput.close();
        }

    }

    /**
     * Decompress file compressedPathName and store plain text in decompressedPathName.
     *
     * @param compressedPathName   - file created by compressFile
     * @param decompressedPathName - store the decompressed text in this file, contents should match the original file before compressFile
     * @param codeTree             - Tree mapping compressed data to characters
     * @throws IOException
     */
    @Override
    public void decompressFile(String compressedPathName, String decompressedPathName, BinaryTree<CodeTreeElement> codeTree) throws IOException {
        BufferedBitReader bitInput = null; //initialize bit reader
        BufferedWriter output = null; //initialize file writer

        try {
            //open input and output files:
            bitInput = new BufferedBitReader(compressedPathName);
            output = new BufferedWriter(new FileWriter(decompressedPathName));
            BinaryTree<CodeTreeElement> node = codeTree;


            //Handles special case of only one character
            if (node.isLeaf()){
                for (int i = 0; i < node.getData().getFrequency(); i++){
                    output.write(node.getData().getChar());
                }

                return;
            }

            while (bitInput.hasNext()) { //loop through each bit
                boolean bit = bitInput.readBit();

                //traverse through the code tree based on bit values:
                if (bit) {
                    node = node.getRight();
                } else {
                    node = node.getLeft();
                }

                // if the node is a leaf, writes the decoded characters into the output file
                if (node.isLeaf()) {
                    output.write(node.getData().getChar());
                    node = codeTree;
                }
            }
        } catch (IOException e) { //catches and rethrows any exceptions
            throw new IOException(e);
        } catch (NullPointerException n){
            throw new NullPointerException();
        }
        finally {
            //close all files regardless of whether an exception occurred:
            if (bitInput != null) bitInput.close();
            if (output != null) output.close();
        }

    }

    private static class TreeComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) { //takes two objects
            //finds the frequencies of both objects:
            long f1 = ((BinaryTree<CodeTreeElement>) o1).getData().getFrequency();
            long f2 = ((BinaryTree<CodeTreeElement>) o2).getData().getFrequency();

            return Long.compare(f1, f2); //compare the two longs and return -1, 1, or 0 depending on their values
        }

    }


    public static void main(String[] args) throws IOException {
        Compression comp = new Compression();
        BinaryTree<CodeTreeElement> codeTree = null;
        String pathName = "Inputs/OneCharacter.txt";
        String compressedPathName = pathName.substring(0, pathName.length() - 4) + "_compressed" + pathName.substring(pathName.length() - 4);
        String decompressedPathName = pathName.substring(0, pathName.length() - 4) + "_decompressed" + pathName.substring(pathName.length() - 4);


        //tries to compress file
        try {
            Map frequencies = comp.countFrequencies(pathName);
            codeTree = comp.makeCodeTree(frequencies);
            Map<Character, String> codeMap = comp.computeCodes(codeTree);
            comp.compressFile(codeMap, pathName, compressedPathName);
        } catch (IOException e) {
            System.err.println(pathName + " is not a valid file!");
        } catch (NullPointerException n){
            System.err.println(pathName + " is Empty!");
        }


        //tries to decompress file
        try {
            comp.decompressFile(compressedPathName, decompressedPathName, codeTree);
        } catch (IOException e) {
            System.err.println(compressedPathName + " is not a valid file!");
        } catch (NullPointerException n){
            System.err.println(compressedPathName + " is Empty!");
        }
    }
}
