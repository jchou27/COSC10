import java.util.*;


/**
 * @Author Jack Chou
 * @Purpose To check that the implementation works on the US Constitution file
 */

public class USConstitutionHuffman extends HuffmanImplementation {
    public static void main(String[] args) throws Exception {
        // Create a new HuffmanImplementation object
        HuffmanImplementation huffman = new HuffmanImplementation();
        // Create a map to store the frequency of each character
        Map<Character, Long> frequencyMap = new HashMap<>();
        // Read the file and count the frequency of each character
        frequencyMap = huffman.countFrequencies("ps3/USConstitution.txt");
        // Create a code tree from the frequency map
        BinaryTree<CodeTreeElement> codeTree = huffman.makeCodeTree(frequencyMap);
        // Print the code tree
        System.out.println(codeTree);
        // Compute the codes for each character in the tree
        Map<Character, String> codeMap = huffman.computeCodes(codeTree);
        // Print the code map
        for (Map.Entry<Character, String> entry : codeMap.entrySet()) {
            System.out.println("Character: " + entry.getKey() + " Code: " + entry.getValue());
        }
        // Compression
        huffman.compressFile(codeMap, "ps3/USConstitution.txt", "ps3/USConstitutionCompressed.txt");
        // Decompression
        huffman.decompressFile("ps3/USConstitutionCompressed.txt", "ps3/USConstitutionDecompressed.txt", codeTree);

    }
}
