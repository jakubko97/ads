import java.io.*;
import java.util.*;

public class OptimalBinarySearchTree {
    private static Key[][] roots;               //we dont use the 0th row and column, saving the keys right away so I can access more info
    private static double[][] probabilitySums;  //w, not using the 0th row
    private static double[][] expectedCosts;    //e, -||-
    private static int numberOfComparisons;

    /**
     * @param roots of our optimal binary search tree :)) duh
     * @param start is the first element of the "subarray", initial value needs to be 1, cuz
     *              there is nothing on the 0th row
     * @param end is the last element of the "subarray"
     *            these  are basically helper variables in order to be able to
     *            find at what subarray we are currently - whether it is [0][n] or [1][3]...
     * */
    public static String findString(Key[][] roots, int start, int end, String stringToFind){
        //so it doesnt throw NullPointerException when accessing the key
        if(start > end || end < 1){
            //System.out.println("illegal index");
            return null;
        }
        if(roots[start][end] == null){
            System.out.println("we should not have gotten here");
            numberOfComparisons++;
            return null;
        }
        String rootString = roots[start][end].getKeyString();
        int rootId = roots[start][end].getId();

        //we found the key
        if(rootString == null || rootString.length() == 0 || rootString.equals(stringToFind)){
            numberOfComparisons++;
            return rootString;
        }//left subtree
        if(stringToFind.compareTo(rootString) < 0 ){        //1st string is smaller than 2nd
            numberOfComparisons++;
            return findString(roots, start, rootId-1, stringToFind);
        }//right subtree
        numberOfComparisons++;
        return findString(roots, rootId+1, end, stringToFind);
    }

    public static int pocetPorovnani(String stringToFind){
        numberOfComparisons = 0;                        //initialize
        if(stringToFind.length() > 0){
            //on the last but one column is the root of the whole tree
            String foundKey = findString(roots, 1, roots.length-1, stringToFind);
            if (foundKey == null){
                System.out.println("Word '" + stringToFind + "' was not found.");
                System.out.println("Dummy key is located on level " + (numberOfComparisons+1) + ".");
            }else{
                System.out.println("Word '" + stringToFind + "' was found.");
                System.out.println("Key is located on level " + numberOfComparisons + ".");
            }
        }
        return numberOfComparisons;
    }

    /**Probability table is a list containing keys:
     * pi - probability of searching a key, we shouldnt access p0 at all, because that value just doesnt exist
     * qi - probability of searching a dummy key
     * (sum of words with frequencies that are <= 50000 between keys(words) with f > 50000)/
     * (sum of frequencies of all words in dictionary)
     *
     * n = 151+1 because there are that many words with frequency > 50000 in dictionary.txt + the 0th element on the start
     * the 0th element is the dummy key for words before 'a'
     */
    private static List<Key> getProbabilityTable(Map<String, Integer> dictionary) throws IOException {
        List<Key> probabilityTable = new ArrayList<>();
        //sums all values (aka frequencies) from dictionary
        int frequenciesOfAllWords = dictionary.values().stream().reduce(0, Integer::sum);
        int frequenciesForDummyKey = 0;
        int keyCount = 0;
        Stack<Double> p_tmp = new Stack<>();        //helper variable so we can save p_i and q_(i-1) at the same time
        Stack<String> word_tmp = new Stack<>();

        p_tmp.push(0.0);                      //actually non existing element! p0 doesnt exist!
        word_tmp.push("");
        for(Map.Entry<String, Integer> wordElement: dictionary.entrySet()){ //would be sweet to remember this
            Integer frequency = wordElement.getValue();
            if(frequency <= 50000){                 //tu hadam mozu byt nezrovnalosti, kvoli tym <=/<???
                frequenciesForDummyKey += frequency;
            }else{                                  //so we have found a key and now we compute its probabilities
                double p = (double)frequency/frequenciesOfAllWords;                 //p_(i+1)
                double q = (double)frequenciesForDummyKey/frequenciesOfAllWords;    //q_i
                frequenciesForDummyKey = 0;         //we have to null it in order to use it for the next key
                probabilityTable.add(new Key(keyCount++, p_tmp.pop(), q, word_tmp.pop()));
                p_tmp.push(p);                      //here we are saving p for the next key
                word_tmp.push(wordElement.getKey());
            }
        }
        //saving the frequencies which are behind the last key
        probabilityTable.add(new Key(keyCount++, p_tmp.pop(), (double)frequenciesForDummyKey/frequenciesOfAllWords, word_tmp.pop()));  //we have to do this for the last element bc it is never saved
        return probabilityTable;
    }

    //testing method, you can say that it is almost one
    //0.9999999999999998 ^^
    //and with dictionary2.txt it is 1 ^^
    public static void checkIfProbabilitiesEqualOne(List<Key> probabilityTable){
        double finalSum = 0;
        for(Key key : probabilityTable){
            finalSum += key.getProbabilityOfDummyKey();
            finalSum += key.getProbabilityOfKey();
        }
        System.out.println(finalSum);
    }

    //bear in mind that the 0th row of the matrices is empty
    public static Key[][] createOptimalBinarySearchTree(List<Key> probabilityTable){
        int n = probabilityTable.size()-1;      //number of words with frequency > 50000 - 1 bc of the extra 0th element
        roots = new Key[n+1][n+1];              //we dont use the 0th row and column, saving the keys right away so I can access more info
        probabilitySums = new double[n+2][n+1]; //w, not using the 0th row
        expectedCosts = new double[n+2][n+1];   //e, -||-

        for(int i = 1; i <= n+1; i++){          //we are filling up the tables from the 1st row
            probabilitySums[i][i-1] = probabilityTable.get(i-1).getProbabilityOfDummyKey();
            expectedCosts[i][i-1] = probabilityTable.get(i-1).getProbabilityOfDummyKey();
        }

        for(int l = 1; l <= n; l++){
            for(int i = 1; i <= n-l+1; i++){
                int j = i + l -1;
                expectedCosts[i][j] = Integer.MAX_VALUE;
                probabilitySums[i][j] = probabilitySums[i][j-1] + probabilityTable.get(j).getProbabilityOfKey() + probabilityTable.get(j).getProbabilityOfDummyKey();
                for(int r = i; r <= j; r++){
                    double t = expectedCosts[i][r-1] + expectedCosts[r+1][j] + probabilitySums[i][j];
                    if(t < expectedCosts[i][j]){
                        expectedCosts[i][j] = t;
                        roots[i][j] = probabilityTable.get(r);
                    }
                }
            }
        }
        return roots;
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> dictionary = FileHandler.handleFile("dictionary.txt");
        List<Key> probabilityTable = getProbabilityTable(dictionary);
        checkIfProbabilitiesEqualOne(probabilityTable);

        createOptimalBinarySearchTree(probabilityTable);
        System.out.println("Root of the tree is  " + roots[1][roots.length-1].getKeyString());

        List<String> stringsToFind = Arrays.asList("which", "where", "have", "been", "of", "the", "world", "even", "i");
        for(String stringToFind: stringsToFind){
            System.out.println("Number of comparisons for word '" + stringToFind + "' is " + pocetPorovnani(stringToFind) +".\n");
        }
    }
}
