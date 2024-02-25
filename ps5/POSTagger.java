import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author Jack Chou
 * @purpose This class is used to tag the parts of speech of a given sentence.
 */

public class POSTagger {
    Map<String, Map<String, Double>> observation; // observation.get("word").get("tag") = P(tag|word)
    Map<String, Map<String, Double>> transition; // transition.get("tag1").get("tag2") = P(tag2|tag1)

    /**
     * Constructor for POSTagger
     */
    public POSTagger() {
        this.observation = new HashMap<>();
        this.transition = new HashMap<>();
    }

    /**
     * Reader for the POS Tags File
     * @param pathname
     */
    public static List<String> readPOSTags(String pathname){
        List<String> linesPOS = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathname));
            String line;
            while ((line = reader.readLine()) != null) {
                linesPOS.add(line); // add the line to the list
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return linesPOS;
    }

    /**
     * Reader for the Observation File, also converts words to lowercase
     * @param pathname
     */
    public static List<String> readObservation(String pathname) {
        List<String> linesObs = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathname));
            String line;
            while ((line = reader.readLine()) != null) {
                linesObs.add(line.toLowerCase()); // add the line to the list and convert to lowercase
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return linesObs;
    }

    /**
     * Helper function to update the map for both observation and transition
     */
    public void updateMap(Map<String, Integer> map, String key) {
        // if the key is already in the map
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1); // increment the count
        } else {
            map.put(key, 1); // add the key to the map
        }

    }

    /**
     * Setters and Getters for the Observation and Transition Probability
     * Helpers to create the observation and transition maps
     */
    public void setObservationProb (String tag, String word, double prob) {
        if (observation.containsKey(tag)) { // if the tag is already in the map
            observation.get(tag).put(word, prob); // get the tag and update the probability
        } else {
            Map<String, Double> map = new HashMap<>(); // create a new map
            map.put(word, prob); // add the word and probability to the map
            observation.put(tag, map); // add the tag and map to the observation map
        }
    }

    public void setTransitionProb (String tag1, String tag2, double prob) {
        if (transition.containsKey(tag1)) { // if the tag1 is already in the map
            transition.get(tag1).put(tag2, prob); // get the tag1 and update the probability
        } else {
            Map<String, Double> map = new HashMap<>(); // create a new map
            map.put(tag2, prob); // add the tag2 and probability to the map
            transition.put(tag1, map); // add the tag1 and map to the transition map
        }
    }

    public Double getObservationProb (String tag, String word) {
        if (!observation.containsKey(tag)) { // if the tag is not in the map
            return 0.0; // return 0
        }
        Map <String, Double> entry = observation.get(tag); // get the map for the tag
        if (entry.containsKey(word)) { // if the word is in the map
            return observation.get(tag).get(word); // return the probability
        } else {
            return -10.0 ; // did not find the word in the tag map, return unobserved value
        }
    }

    public Double getTransitionProb (String tag1, String tag2) {
        if (!transition.containsKey(tag1)) { // if the tag1 is not in the map
            return 0.0; // return 0
        }
        Map <String, Double> entry = transition.get(tag1); // get the map for the tag1
        if (entry.containsKey(tag2)) { // if the tag2 is in the map
            return transition.get(tag1).get(tag2); // return the probability
        } else {
            return 0.0; // did not find the tag2 in the tag1 map, return unobserved value
        }
    }

    /**
     * Create observation map
     */
    public void createObservationMap(Map<String, Integer> tagCount , Map<String, Integer> tagWordCount) {
        for (String tagWord : tagWordCount.keySet()) {
            double count = tagWordCount.get(tagWord); // get the count of the tagWord
            String[] split = tagWord.split("_"); // split the tagWord into word and tag
            if (split.length < 2) { // if the length is not 2
                continue; // continue
            }
            String word = split[0]; // get the word
            String tag = split[1]; // get the tag
            if (tagCount.containsKey(tag)) {
                double total = tagCount.get(tag); // get the total count of the tag
                double prob = Math.log10(count / total); // calculate the probability
                setObservationProb(tag, word, prob); // set the observation probability
            }
        }
    }

    /**
     * Create transition map
     */
    public void createTransitionMap(Map<String, Integer> tagCount, Map<String, Integer> tagTransitionCount) {
        for (String tagTransition : tagTransitionCount.keySet()) {
            double count = tagTransitionCount.get(tagTransition); // get the count of the tagTransition
            String[] split = tagTransition.split("_"); // split the tagTransition into tag1 and tag2
            if (split.length < 2) { // if the length is not 2
                continue; // continue
            }
            String tag1 = split[0]; // get the tag1
            String tag2 = split[1]; // get the tag2
            if (tagCount.containsKey(tag1)) {
                double total = tagCount.get(tag1); // get the total count of the tag1
                double prob = Math.log10(count / total); // calculate the probability
                setTransitionProb(tag1, tag2, prob); // set the transition probability
            }
        }
    }

    /**
     * Trains the POS Tagger
     * @param pathName_Sentences
     * @param pathName_Tags
     */
    public void train(String pathName_Sentences, String pathName_Tags) {
        System.out.println("Training the POS Tagger Model with " + pathName_Sentences + " and " + pathName_Tags);

        List<String> lines_Words = readObservation(pathName_Sentences); // list of sentences
        List<String> lines_Tags = readPOSTags(pathName_Tags); // list of tags

        Map<String, Integer> tagCount = new HashMap<>(); // count of each tag appearance
        Map<String, Integer> tagTransitionCount = new HashMap<>(); // count of each tag transition
        Map<String, Integer> wordTagCount = new HashMap<>(); // count of each word with a tag

        for (int i = 0; i < lines_Tags.size(); i++) {

            String line_Tag = lines_Tags.get(i); // get the tags
            String line_Word = lines_Words.get(i); // get the sentences

            List<String> tags = Arrays.asList(line_Tag.split(" ")); // split the tags into a list
            List<String> words = Arrays.asList(line_Word.split(" ")); // split the sentences into a list

            String prevTag = "#";
            tagCount.put(prevTag, 1); // add the start tag to the tag count

            for (int j = 0; j < tags.size(); j++) {

                String currTag = tags.get(j);
                String tagTransition = prevTag + "_" + currTag;

                String word = words.get(j);
                String tagWord = currTag + "_" + word;


                updateMap(tagCount, currTag); // update the start tag count
                updateMap(tagTransitionCount, tagTransition); // update the tag transition count
                updateMap(wordTagCount, tagWord); // update the tag count

                prevTag = currTag; // update the start tag
            }
        }
        createObservationMap(tagCount, wordTagCount); // create the observation map
        createTransitionMap(tagCount, tagTransitionCount); // create the transition map
    }

    /**
     * Viterbi Algorithm
     */
    public List<String> viterbiAlg(List<String> words) {
        Map<Integer, Map<String, String>> backpointer = new HashMap<>(); // backpointer map
        Set<String> currStates = new HashSet<>(); // set of current state
        Map<String, Double> currScores = new HashMap<>(); // map of current score

        String start = "#";
        currStates.add(start); // add the start tag
        currScores.put(start, 0.0); // add the score 0.0

        // iterate through the list of words and calculate the score
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i); // get the word
            Set<String> nextStates = new HashSet<>(); // next state
            Map<String, Double> nextScores = new HashMap<>(); // next score
            for (String currState : currStates) {
                if (!transition.containsKey(currState)) { // if the current state is not in the transition map
                    continue; // continue
                }
                Map<String, Double> transitionEntry = transition.get(currState); // get the map for the current state
                for (String nextState : transitionEntry.keySet()) {
                    nextStates.add(nextState); // add the next state to the all possible next state set
                    double score1 = currScores.get(currState); // get the current score
                    double score2 = getTransitionProb(currState, nextState); // get the transition probability
                    double score3 = getObservationProb(nextState, word); // get the observation probability
                    double score = score1 + score2 + score3; // calculate the score

                    // if the next state is not in the next score map or the score is greater than the next score -> updating the max score
                    if (!nextScores.containsKey(nextState) || score > nextScores.get(nextState)) {
                        nextScores.put(nextState, score); // update the next score
                        //if the backpointer map does not contain the current state
                        if (!backpointer.containsKey(i)) {
                            Map<String, String> path = new HashMap<>(); // create a new map
                            path.put(nextState, currState); // add the next state and current state to the map
                            backpointer.put(i, path); // add the map to the backpointer
                        }
                        else{
                            backpointer.get(i).put(nextState, currState); // add the next state and current state to the backpointer map
                        }
                    }
                }
            }
            currStates = nextStates; // update the current state
            currScores = nextScores; // update the current score
        }

        //To find the highest probability score
        List<String> stateFinalTags = new ArrayList<>(currStates); // list of state names that contains all the possible final state tags
        String maxCurrState = stateFinalTags.get(0); // current state
        for (int i = 1; i < stateFinalTags.size(); i++) {
            // update the current state to the state with the max score
            if (currScores.get(stateFinalTags.get(i)) > currScores.get(maxCurrState)) {
                maxCurrState = stateFinalTags.get(i);
            }
        }

        //Backtracking to find the best path
        Stack<String> backtrack = new Stack<>(); // stack of state names that contains all the possible final state tags
        backtrack.push(maxCurrState); // push the current state with the maxscore to the stack
        // iterate through the words and find the best path from the end because the start state does not have a backpointer
        for (int i = words.size() - 1; i > 0; i--) {
            maxCurrState = backpointer.get(i).get(maxCurrState); // get the max score state
            backtrack.push(maxCurrState); // push the max score state to the stack
        }


        //The best path in forward order
        List<String> bestPath = new ArrayList<>(); // list of the best path
        while (!backtrack.isEmpty()) {
            String state = backtrack.pop(); // remove the last state from the list
            if (!state.equals("#")) {
                bestPath.add(state); // add the state to the best path
            }
        }
        System.out.println(bestPath);
        return bestPath; // return the best path
    }

    /**
     * Accuracy of the POS Tagger
     * @param pathName_Sentences
     * @param pathName_Tags
     */

    public void accuracy(String pathName_Sentences, String pathName_Tags) {
        System.out.println("Calculating the accuracy of the POS Tagger Model with " + pathName_Sentences + " and " + pathName_Tags);
        List<String> lines_Word = readObservation(pathName_Sentences); // list of sentences
        List<String> lines_Tags = readPOSTags(pathName_Tags); // list of tags

        int tWord = 0; // total number of words
        int tCorrect = 0; // number of correct tags

        for (int i = 0; i < lines_Word.size(); i++) {
            String word = lines_Word.get(i); // get the sentence
            String tag = lines_Tags.get(i); // get the tags

            List<String> words = Arrays.asList(word.split(" ")); // split the sentence into words
            List<String> tags = Arrays.asList(tag.split(" ")); // split the tags into tags for words
            List<String> tagged = viterbiAlg(words); // get the tagged words

            tWord += words.size(); // increment the total number of words

            for (int j = 0; j < tags.size(); j++) { // iterate through the tags
                if (tags.get(j).equals(tagged.get(j))) { // if the tag is correct
                    tCorrect++; // increment the number of correct tags
                }
            }
        }
        double accuracy = (double) tCorrect / tWord; // calculate the accuracy
        System.out.println("Total Words: " + tWord); // print the total number of words
        System.out.println("Correct Tags: " + tCorrect); // print the number of correct tags
        System.out.println("Incorrect Tags: " + (tWord - tCorrect)); // print the number of incorrect tags
        System.out.println("Accuracy: " + accuracy * 100 + "%"); // print the accuracy
    }


    //Still gotta fix this
    public static void exampleTest() {
        POSTagger sudi = new POSTagger(); // create a new POS Tagger
        String pathName_Sentences = "ps5_data/example-sentences.txt"; // path to the train sentences
        String pathName_Tags = "ps5_data/example-tags.txt"; // path to the train tags
        sudi.train(pathName_Sentences, pathName_Tags); // train the POS Tagger


    }

    public static void simpleTest() {
        POSTagger sudi = new POSTagger(); // create a new POS Tagger
        String pathName_Sentences = "ps5_data/simple-train-sentences.txt"; // path to the train sentences
        String pathName_Tags = "ps5_data/simple-train-tags.txt"; // path to the train tags
        sudi.train(pathName_Sentences, pathName_Tags); // train the POS Tagger

        String pathName_Sentences_Test = "ps5_data/simple-test-sentences.txt"; // path to the test sentences
        String pathName_Tags_Test = "ps5_data/simple-test-tags.txt"; // path to the test tags
        sudi.accuracy(pathName_Sentences_Test, pathName_Tags_Test); // calculate the accuracy
    }


    /**
     * Run the brown files
     */
    public static void brown() {
        POSTagger sudi = new POSTagger();
        String pathName_Sentences = "ps5_data/brown-train-sentences.txt";
        String pathName_Tags = "ps5_data/brown-train-tags.txt";
        sudi.train(pathName_Sentences, pathName_Tags);

        String pathName_Sentences_Test = "ps5_data/brown-test-sentences.txt";
        String pathName_Tags_Test = "ps5_data/brown-test-tags.txt";
        sudi.accuracy(pathName_Sentences_Test, pathName_Tags_Test);
    }

    public static void main(String[] args) {
        try {
            simpleTest();
//            brown();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
