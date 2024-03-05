import java.io.*;
import java.util.*;

/**
 * @author Jack Chou
 * @Purpose To implement the huffman interface
 */

public class HuffmanImplementation implements Huffman{
    /**
     * Read file provided in pathName and count how many times each character appears
     * @param pathName - path to a file to read
     * @return
     * @throws IOException
     */
    @Override
    public Map<Character, Long> countFrequencies(String pathName) throws IOException {
        // Create a map to store the frequency of each character
        HashMap<Character, Long> frequencyMap = new HashMap<>();
        // Read the file and count the frequency of each character
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathName)); // Read the file
            // Read the file character by character
            while (reader.ready()) {
                Character currChar = (char) reader.read();
                // If char in the map, increment the frequency
                if (frequencyMap.containsKey(currChar)) {
                    frequencyMap.put(currChar, frequencyMap.get(currChar) + 1);
                } else { // If char not in map, add it to the map with a frequency of 1
                    frequencyMap.put(currChar, 1L); // Put the character in the map with a frequency of 1
                }
            }
            reader.close();
            return frequencyMap;
        } catch (IOException e) { // If file not found, throw an exception
            throw new IOException(e);
        }
    }

    /**
     * Construct a code tree from a map of frequency counts. Note: this code should handle the special
     * cases of empty files or files with a single character.
     * @param frequencies a map of Characters with their frequency counts from countFrequencies
     * @return
     */
    @Override
    public BinaryTree<CodeTreeElement> makeCodeTree(Map<Character, Long> frequencies) {
        // Create new priority queue with a comparator that compares the frequency of the trees
        PriorityQueue<BinaryTree<CodeTreeElement>> queue = new PriorityQueue<>(Comparator.comparingLong(tree -> tree.getData().getFrequency()));
        // if the map is empty, return null
        if (frequencies.isEmpty()) {
            return null;
        }
        if (frequencies.size() == 1) { // if the map has only one entry, return a tree with that entry
            Map.Entry<Character, Long> entry = frequencies.entrySet().iterator().next();
            CodeTreeElement treeElement = new CodeTreeElement(entry.getValue(), entry.getKey());
            BinaryTree<CodeTreeElement> left = new BinaryTree<>(treeElement);
            BinaryTree<CodeTreeElement> right = new BinaryTree<>(treeElement);
            return new BinaryTree<>(new CodeTreeElement(entry.getValue(), null), left, right);
        }
        // Add all the trees to the priority queue
        for (Map.Entry<Character, Long> entry : frequencies.entrySet()) {
            queue.add(new BinaryTree<>(new CodeTreeElement(entry.getValue(), entry.getKey())));
        }
        while (queue.size() > 1) {
            BinaryTree<CodeTreeElement> left = queue.remove(); // Get left tree with the lowest frequency
            BinaryTree<CodeTreeElement> right = queue.remove(); // Get right tree with the lowest frequency
            // Create a new CodeTreeElement that combines the left tree and right tree's frequencies, c is null bcuz it's an internal node
            CodeTreeElement newElement = new CodeTreeElement(left.getData().getFrequency() + right.getData().getFrequency(), null);
            // Create a new tree with the new element as the root and the left and right trees as children
            BinaryTree<CodeTreeElement> newTree = new BinaryTree<>(newElement, left, right);
            // Add the new tree to the queue with the sum of the two trees' frequencies
            queue.add(newTree);
        }
        return queue.poll(); // Return the last tree in the queue
    }

    /**
    * Compute the code for all characters in the tree and enters them into a hash map where the key is a character
    * and the value is the code of 1's and 0's representing that character.
    * @param codeTree
    * @return
    */
    @Override
    public Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree) {
        // Create a hash map to store the codes and its corresponding letter
        Map<Character, String> codeMap = new HashMap<>();
        // If the tree is empty, return an empty map
        if (codeTree == null) {
            return codeMap;
        }
        // If the tree has only one node, return a map with the character and the code "0"
        if (codeTree.isLeaf()) {
            codeMap.put(codeTree.getData().getChar(), "0"); // root node has a code of "0"
            return codeMap;
        }
        // If the tree has more than one node, call the helper method to compute the codes
        computeCodesHelper(codeTree, "", codeMap);
        return codeMap;
    }

    /**
     * Helper method to compute the codes for all characters in the tree by recursively traversing the tree in preorder
     * @param tree
     * @param code
     * @param codeMap
     */
    private void computeCodesHelper(BinaryTree<CodeTreeElement> tree, String code, Map<Character, String> codeMap) {
        if (tree.isLeaf()) { // If the tree is a leaf, add the character and its code to the hash map
            codeMap.put(tree.getData().getChar(), code);
        } else { // If the tree is an inner node, recursively call the helper method on the left and right children
            computeCodesHelper(tree.getLeft(), code + "0", codeMap); // left = 0
            computeCodesHelper(tree.getRight(), code + "1", codeMap); // right = 1
        }
    }

    /**
     * Compress the file pathName and store compressed representation in compressedPathName.
     * @param codeMap - Map of characters to codes produced by computeCodes
     * @param pathName - File to compress
     * @param compressedPathName - Store the compressed data in this file
     * @throws IOException
     */
    @Override
    public void compressFile(Map<Character, String> codeMap, String pathName, String compressedPathName) throws IOException {
        try {
            BufferedReader input = new BufferedReader(new FileReader(pathName)); // Read the file
            BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName); // Write the bit compressed file
            if (codeMap != null) { // If the code map is not empty
                while (input.ready()) { // Loop that reads the file character by character until end of file
                    char c = (char) input.read(); // Read a character from the file
                    String code = codeMap.get(c); // get the bit codes for the character
                    for (int i = 0; i < code.length(); i++) { // Loops through the bit codes and writes the bits to the file
                        if (code.charAt(i) == '0') { // If the bit is 0, write a false bit
                            bitOutput.writeBit(false);
                        } else { // If the bit is 1, write a true bit
                            bitOutput.writeBit(true);
                        }
                    }
                }
                input.close();
                bitOutput.close();
            }

        } catch (IOException e) { // Exception: If no file was given
            throw new IOException(e);
        }
    }

    @Override
    public void decompressFile(String compressedPathName, String decompressedPathName, BinaryTree<CodeTreeElement> codeTree) throws IOException {
        try {
            BufferedBitReader bitInput = new BufferedBitReader(compressedPathName); // Read the compressed bit file
            BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName)); // Write the decompressed file
            if (codeTree != null) { // If the code tree is not empty
                BinaryTree<CodeTreeElement> current = codeTree; // Set the current node to the root of the code tree
                while (bitInput.hasNext()) { //while there are still bits to read
                    boolean bit = bitInput.readBit(); // Read a bit from the file
                    if (bit) { // If the bit is 1, move to the right child
                        current = current.getRight();
                    } else { // If the bit is 0, move to the left child
                        current = current.getLeft();
                    }
                    if (current.isLeaf()) { // If the current node is a leaf, write the character to the file
                        CodeTreeElement element = current.getData();  // Retrieve the data (Char, Frequency) from the leaf node
                        char c = element.getChar(); // Gets the Char from the CodeTreeElement
                        output.write(c); // Writes the char into the Decompressed file
                        current = codeTree; // Reset the current node to the root of the code tree
                    }
                }
            }
            bitInput.close();
            output.close();
        } catch (IOException e) { // Exception: If no file was given
            throw new IOException(e);
        }
    }
}


