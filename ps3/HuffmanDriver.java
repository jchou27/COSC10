import java.io.*;
import java.util.*;

/**
 * @author Jack Chou
 * @Purpose To implement the huffman interface
 */

public class HuffmanDriver implements Huffman {

    @Override
    public Map<Character, Long> countFrequencies(String pathName) throws IOException {
        HashMap<Character, Long> frequencyMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathName))) {
            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                if (frequencyMap.containsKey(character)) {
                    frequencyMap.put(character, frequencyMap.get(character) + 1);
                } else {
                    frequencyMap.put(character, 1L);
                }
            }
        }
    }

    @Override
    public BinaryTree<CodeTreeElement> makeCodeTree(Map<Character, Long> frequencies) {
        return null;
    }

    @Override
    public Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree) {
        return null;
    }

    @Override
    public void compressFile(Map<Character, String> codeMap, String pathName, String compressedPathName) throws IOException {

    }

    @Override
    public void decompressFile(String compressedPathName, String decompressedPathName, BinaryTree<CodeTreeElement> codeTree) throws IOException {

    }
}
