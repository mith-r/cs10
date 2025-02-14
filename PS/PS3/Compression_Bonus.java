import java.io.*;
import java.util.*;

/**
 *
 * Compress and decompress files
 * Dartmouth CS 10, Winter 2025
 *
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 */

public class Compression_Bonus {

    /**
     * Read file provided in pathName and count how many times each character appears
     *
     * @param pathName - path to a file to read
     * @return - Map with a character as a key and the number of times the character appears in the file as value
     * @throws IOException
     */

    public static Map<Character, Long> countFrequencies(String pathName) throws IOException {
        Map<Character, Long> frequencies = new HashMap<>(); //initialize a new hash map that stores characters and longs

        try (BufferedReader input = new BufferedReader(new FileReader(pathName))) { //attempts to open a file to read using a BufferedReader
            StringBuilder str = new StringBuilder(); //initialize a string builder to store the contents of the file
            String line;
            boolean firstLine = true;

            //append each line of the file to the string builder:
            while ((line = input.readLine()) != null) {
                if (!firstLine) {
                    str.append("\n");
                }
                str.append(line);
                firstLine = false;
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

    public static BinaryTree<CodeTreeElement> makeCodeTree(Map<Character, Long> frequencies) {
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

    public static Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree) {
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
    private static void computeCodesHelper(BinaryTree<CodeTreeElement> node, String prefix, Map<Character, String> map) {

        //If only one character, just append 0
        if (node.isLeaf() && prefix.isEmpty()) {
            map.put(node.getData().getChar(), "0");
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
     * converts binary tree to a string representation
     * @param treeMap - treeMap of huffman encoding
     * @returns string version of tree
     */

    public static String treeToString(BinaryTree<CodeTreeElement> treeMap) {
        StringBuilder res = new StringBuilder();
        treeToStringHelper(treeMap, res); // calls helper function
        res.append("!!"); //two exclamation mark ends file
        return res.toString();

    }

    /**
     * helper function to recursively create string
     * @param node - node of tree
     * @param string - stringbuilder to append to
     */
    private static void treeToStringHelper(BinaryTree<CodeTreeElement> node, StringBuilder string) {
        if (node == null) {
            return;
        }
        // if node has character append 'C' to mark character and the character else '*' marks empty node
        if (node.getData().getChar() == null) {
            string.append("*");
        } else {
            string.append("C").append(node.getData().getChar());
        }

        treeToStringHelper(node.getLeft(), string); //recursion
        treeToStringHelper(node.getRight(), string); //recursion
    }

    /**
     * Compress the file pathName and store compressed representation in compressedPathName.
     *
     * @param codeMap            - Map of characters to codes produced by computeCodes
     * @param pathName           - File to compress
     * @param compressedPathName - Store the compressed data in this file
     * @throws IOException
     */

    public static void compressFile(Map<Character, String> codeMap, BinaryTree<CodeTreeElement> codeTree, String pathName, String compressedPathName) throws IOException {
        BufferedReader input = null; //initialize file reader
        BufferedBitWriter bitOutput = null; //initialize bit writer

        try {
            //open input and output files:
            input = new BufferedReader(new FileReader(pathName));
            bitOutput = new BufferedBitWriter(compressedPathName);

            String treeString = treeToString(codeTree); //String representation of huffman tree

            char[] mapChar = treeString.toCharArray(); //convert string to character array

            //for every character convert to binary and write bits to compressed file
            for (char c : mapChar) {
                String binary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
                char[] binaryChar = binary.toCharArray();
                for (char b : binaryChar) {
                    bitOutput.writeBit(b == '1');
                }
            }


            boolean firstLine = true;

            String line = ""; //string that will store the contents of each line

            while ((line = input.readLine()) != null) { //loop through each line

                if (firstLine) {
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
        } catch (NullPointerException n) { //catches and rethrows NullPointerExceptions
            throw new NullPointerException();
        } finally {
            //close all files regardless of whether an exception occurred:
            if (input != null) input.close();
            if (bitOutput != null) bitOutput.close();
        }

    }

    /**
     * returns Huffman tree from string representation
     * @param stringArray arraylist that stores each binary string/character
     * @return Huffman tree
     */

    public static BinaryTree<CodeTreeElement> stringToTree(ArrayList<Character> stringArray){
        int index = 0;
        BinaryTree<CodeTreeElement> codeTree = new BinaryTree<CodeTreeElement>(new CodeTreeElement(null,null));
        stringToTreeHelper(stringArray, codeTree, index); //helper function
        return codeTree;
    }

    /**
     * recursive function to recreate huffman tree
     * @param stringArray - arraylist that stores each binary string/character
     * @param codeTree - reconstructed code tree
     * @param index - index of arraylist
     * @return - recursively modifying index
     */

    private static int stringToTreeHelper(ArrayList<Character> stringArray, BinaryTree<CodeTreeElement> codeTree, int index){
        //base case
        if (index >= stringArray.size() || codeTree == null) {
            return index;
        }

        char current = stringArray.get(index);
        index++; //increase index in case of 'C'

        //if '*', then parent node else its a character leaf node
        if (current == '*') {
            codeTree.setData(new CodeTreeElement(0L, null));

            //sets trees to avoid null pointer
            codeTree.setLeft(new BinaryTree<>(new CodeTreeElement(null, null)));
            codeTree.setRight(new BinaryTree<>(new CodeTreeElement(null, null)));

            index = stringToTreeHelper(stringArray, codeTree.getLeft(), index);
            index = stringToTreeHelper(stringArray, codeTree.getRight(), index);
        } else if (current == 'C') {

            char letter = stringArray.get(index);
            codeTree.setData(new CodeTreeElement(0L, letter));
            index++;
        }
        return index;
    }

    /**
     * Decompress file compressedPathName and store plain text in decompressedPathName.
     *
     * @param compressedPathName   - file created by compressFile
     * @param decompressedPathName - store the decompressed text in this file, contents should match the original file before compressFile
     * @throws IOException
     */
    public static void decompressFile(String compressedPathName, String decompressedPathName) throws IOException {
        BufferedBitReader bitInput = null; //initialize bit reader
        BufferedWriter output = null; //initialize file writer

        try {
            //open input and output files:
            bitInput = new BufferedBitReader(compressedPathName);
            output = new BufferedWriter(new FileWriter(decompressedPathName));

            ArrayList<Character> stringArray = new ArrayList<>(); //stores ArrayList of bytes (characters)

            boolean breakOut = false; //flag to break out

            while (true) {
                StringBuilder bits = new StringBuilder();

                //repeats in loops of 8 to decode bits
                for (int i = 0; i < 8; i++) {
                    if (bitInput.hasNext()) {
                        boolean bit = bitInput.readBit();
                        if (bit) {
                            bits.append('1');
                        } else {
                            bits.append('0');
                        }
                    }
                }

                String stringBits = bits.toString();
                int charCode = Integer.parseInt(stringBits, 2);
                char character = (char) charCode;

                //break out if !! twice

                if (character == '!') {
                    if (breakOut) {
                        break;
                    } else {
                        breakOut = true;
                        continue; //Continue to see if next one is '!' before adding
                    }
                }

                //this only happens if first one was '!' and second wasn't so add '!' to treeString and reset flag
                if (breakOut) {
                    stringArray.add('!');
                    breakOut = false; // Reset flag
                }

                stringArray.add(character); // Add valid characters
            }

            BinaryTree<CodeTreeElement> tree = stringToTree(stringArray);
            BinaryTree<CodeTreeElement> node = tree;


            //Handles special case of only one character
            if (node.isLeaf()) {
                while (bitInput.hasNext()) {
                    bitInput.readBit();
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
                if (node.getData().getChar() != null) {
                    output.write(node.getData().getChar());
                    node = tree;
                }
            }
        } catch (IOException e) { //catches and rethrows any exceptions
            throw new IOException(e);
        } catch (NullPointerException n) {
            throw new NullPointerException();
        } finally {
            //close all files regardless of whether an exception occurred:
            if (bitInput != null) bitInput.close();
            if (output != null) output.close();
        }

    }

    /**
     * easier for user to use by condensing all functions into one
     * @param pathName - file to compress
     */

    public static void compress(String pathName)  {
        try {
            String compressedPathName = pathName.substring(0, pathName.length() - 4) + "_compressed" + pathName.substring(pathName.length() - 4);
            Map frequencies = countFrequencies(pathName);
            BinaryTree<CodeTreeElement> codeTree = makeCodeTree(frequencies);
            Map<Character, String> codeMap = computeCodes(codeTree);
            compressFile(codeMap, codeTree, pathName, compressedPathName);
            System.out.println("Compression Successful!");
        } catch (IOException e){
            System.err.println(pathName + " is not a valid file!");
        } catch (NullPointerException n){
            System.err.println(pathName + " is Empty!");
        }
    }

    /**
     * easier for user to use by condensing all functions into one
     * @param compressedPathName name of compressed file
     */

    public static void decompress(String compressedPathName) {
        try{
            String decompressedPathName = compressedPathName.substring(0,compressedPathName.length() - 14) + "de" + compressedPathName.substring(compressedPathName.length()-14);
            decompressFile(compressedPathName, decompressedPathName);
            System.out.println("Decompression Successful!");
        } catch (IOException e) {
            System.err.println(compressedPathName + " is not a valid file!");
        } catch (NullPointerException n){
            System.err.println(compressedPathName + " is Empty!");
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
        compress("Inputs/OneCharacter.txt"); //test compression
        decompress("Inputs/OneCharacter_compressed.txt"); //test decompression
    }
}
