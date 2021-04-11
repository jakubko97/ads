//ADS zadanie 2, Knapsack 0/1
//Natalia Klementova, 87080

//run cez cmd (treba odkomentovat 2 riadky - 1. riadok v readFile() a 2. riadok v saveToFile() a zakomentovat tie pod nimi)
//prejdeme do priecinka, kde mame zdrojovy kod a instrukcie: cd C:\Users\natik\Documents\ADS\zadanie2\src
//compile-> javac Knapsack.java
//run-> java Knapsack predmety2.txt out.txt

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knapsack {
    private static int maximumAllowedItems;
    private static int maximumAllowedWeight;
    private static int maximumAllowedFragileItems;
    private static List<Item> listOfItems = new ArrayList<>();  //indexing from 0 so we have to access the elements like i-1


    public static List<Item> sortListByValue(List<Item> list, int n){
        Item tmp;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getValue() > list.get(j+1).getValue()) {
                    tmp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, tmp);
                    swapped = true;
                }
            }
            // IF no two elements were
            // swapped by inner loop, then break
            if (swapped == false)
                break;
        }
        return list;
    }

    public static List<Item> sortListByFragility(List<Item> list, int n){
        Item tmp;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if(list.get(j).getWeight() == list.get(j+1).getWeight()){
                    if (list.get(j).getFragility() > list.get(j+1).getFragility()) {
                        tmp = list.get(j);
                        list.set(j, list.get(j+1));
                        list.set(j+1, tmp);
                        swapped = true;
                    }
                }
            }
            // IF no two elements were
            // swapped by inner loop, then break
            if (swapped == false)
                break;
        }
        return list;
    }

    public static List<Item> reverseList(List<Item> list){
        List<Item> reversedList = new ArrayList<>();
        for(int i = list.size()-1; i >= 0; i--){
            reversedList.add(list.get(i));
        }
        return reversedList;
    }


    public static List<Item> findChosenItems(int[][][] T, List<Item> items){
        List<Item> chosenItems = new ArrayList<>();
        int j = maximumAllowedWeight, k = maximumAllowedFragileItems;

        for(int i = maximumAllowedItems; i > 0; i--){
            if(T[i][j][k] == T[i-1][j][k]){
                continue;
            }
            chosenItems.add(items.get(i-1));
            j = j - items.get(i-1).getWeight();
            k = k - items.get(i-1).getFragility();
        }
        return chosenItems;
    }


    private static int[][][] fillTheMatrix(int maxItems, int maxWeight, int maxFragileItems, List<Item> items) {
        int[][][] T = new int[maxItems+1][maxWeight+1][maxFragileItems+1];

        for(int i = 0; i <= maxItems; i++){                    //items/rows
            for(int j = 0; j <= maxWeight; j++){               //columns "from front"
                for(int k = 0; k <= maxFragileItems; k++){     //columns "from side"
                    if(i == 0 || j == 0 || k == 0){
                        T[i][j][k] = 0;
                    }//todo ta 2. cast podmienky je vzdy true
                    else if((items.get(i-1).getWeight() > j) || (items.get(i-1).getFragility() > k)){
                        T[i][j][k] = T[i-1][j][k];
                    }
                    else{
                        T[i][j][k] = Math.max(
                                items.get(i-1).getValue() + T[i-1][j-items.get(i-1).getWeight()][k-items.get(i-1).getFragility()],
                                T[i-1][j][k]);
                    }
                }
            }
        }
        return T;
    }


    public static void saveToFile(String[] args, List<Item> items, int maxValue){
        //String fileForSaving = args[1];     //if we use cmd
        String fileForSaving = "out.txt";

        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileForSaving))) {
            br.write(maxValue + "\n");
            br.write(items.size() + "\n");
            for(Item i : items){
                br.write(i.getId() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***We assume the file is in correct form so there is no exception handling.
     * @param file is a list of sentences where every word has its own id
     */
    public static void parseFile(List<List<String>> file){
        int id, value, weight, fragility;
        maximumAllowedItems = Integer.parseInt(file.get(0).get(0));
        maximumAllowedWeight = Integer.parseInt(file.get(1).get(0));
        maximumAllowedFragileItems = Integer.parseInt(file.get(2).get(0));
        for(int i = 3; i < file.size(); i++){
            id = Integer.parseInt(file.get(i).get(0));
            value = Integer.parseInt((file.get(i).get(1)));
            weight = Integer.parseInt(file.get(i).get(2));
            fragility = Integer.parseInt(file.get(i).get(3));
            listOfItems.add(new Item(id, value, weight, fragility));
        }
    }


    public static List<List<String>> readFile(String[] args) throws IOException {
        List<List<String>> listWithSentences = new ArrayList<>();
        //try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {     //if we use cmd
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\work\\xjancikj\\ads\\Zadanie2\\src\\predmety1.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> splitLine = Arrays.asList(line.split("\\s+"));     //split according to spaces
                listWithSentences.add(splitLine);
            }
        }
        return listWithSentences;
    }


    public static void main(String[] args) throws IOException {
        List<List<String>> file = readFile(args);
        parseFile(file);
        //len skusanie ci by vysli lepsie vysledky
//        listOfItems = sortListByValue(listOfItems, listOfItems.size());
//        listOfItems = sortListByFragility(listOfItems, listOfItems.size());
//        for(Item i : listOfItems) {
//            System.out.println(i);
//        }

        int T[][][] = fillTheMatrix(maximumAllowedItems, maximumAllowedWeight, maximumAllowedFragileItems, listOfItems);
        List<Item> chosenItems = findChosenItems(T, listOfItems);
        chosenItems = reverseList(chosenItems);
        saveToFile(args, chosenItems, T[maximumAllowedItems][maximumAllowedWeight][maximumAllowedFragileItems]);
    }
}
