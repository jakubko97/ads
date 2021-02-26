package sk.fei.ads;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DynamicProgramming {

    static Map<Integer,Integer> penalty = new HashMap<>();
    static int[] intArray = new int[1000];

    public static List<String> readFileInList(String fileName)
    {

        List<String> lines = Collections.emptyList();
        try
        {
            lines =
                    Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }

    public static void findMinPenaltyInRange(int from, int to){
        for(int kmPerDay=from; kmPerDay <= to; kmPerDay++){
            int loc_penalty=0;
            int breakKm = 0;
            int breakIdx = 0;
            int breakKmIdx = 0;
            for (int j=0; j < intArray.length; j++) {
                int km = intArray[j];
                int a = 0;
                int idx = 0;
                int diff = (km-breakKm);
                if(diff > kmPerDay){
                    int minValue = (intArray[breakIdx] - breakKm) - kmPerDay;
                    for (int k=breakIdx; k <= j; k++) {
//                    System.out.println("hodnota k : " + intArray[k] + "break km : " + breakKm);
                        a = (intArray[k] - breakKm) - kmPerDay;
                        if(Math.abs(a) < Math.abs(minValue)){
                            minValue = Math.abs(a);
                            breakKmIdx = intArray[k];
                            idx = k;
                        }
                    }
                    breakKm = breakKmIdx;
                    breakIdx = idx;
                    loc_penalty += Math.pow(minValue, 2);

                }
            }
            penalty.put(kmPerDay,loc_penalty);
        }
        int min = Collections.min(penalty.values());
        System.out.println("Minimum value is: " + min);
    }

    public static void findMinPen(){
        int kmPerDay = 400;
        double value = 0;
        int[] bestpath = new int[intArray.length];
        int[] stop = new int[intArray.length];

            for (int j=0; j < intArray.length; j++) {
                bestpath[j] = (int) (Math.pow((kmPerDay - intArray[j]), 2));
                System.out.println(bestpath[j]);
                stop[j] = 0;
//                System.out.println("At city: " + j);
                for (int k=0; k < j; k++) {
//                  System.out.println("hodnota k : " + intArray[k] + "break km : " + breakKm);
                    value = bestpath[k] + Math.pow(kmPerDay - (intArray[j] - intArray[k]),2);
//                    System.out.println((intArray[j] - intArray[k]));
                    if(value < bestpath[j]){
                        bestpath[j] = (int) value;
                        stop[j] = k;
                    }
                }
        }

        // Print the output
        System.out.println(Arrays.toString(bestpath));
        System.out.println("Minimal Penalty :"+bestpath[intArray.length - 1]);
        String finalPath = "";
        int index = stop.length-1;
        while(index>=0){
            finalPath = (index+1)+" "+finalPath;
            index = stop[index]-1;
        }
        System.out.println("Stop at: "+finalPath);
    }

    public static int recursive(int[] bestpath, int n, int i, int j){
        int kmPerDay = 400;
        int value = 0;

        if (i < n) {
            if (j < n) {
                // inner loop when j < n
                value = bestpath[j] + (int) Math.pow(kmPerDay - (intArray[i] - intArray[j]),2);
                bestpath[j] = value + recursive(bestpath,n, i, j+1); // increment inner counter only!
            } else { // when j has reached n...
                bestpath[j] = recursive(bestpath,n, i+1, 0); // increment outer counter, reset inner
            }
        }
        return bestpath[j];
    }

    public static void parseFileIntoArray(){
        List l = readFileInList("C:\\work\\xjancikj\\ads\\cv02\\ADS2021_cvicenie2data.txt");

        Iterator<String> itr = l.iterator();

        int i = 0;
        while (itr.hasNext()) {
            intArray[i] = Integer.valueOf(itr.next());
            i++;
        }
    }
    public static void main(String[] args)
    {

        parseFileIntoArray();
//        findMinPenaltyInRange(400,500);
        findMinPen();

//        int lenght = 1000;
//        int[] bestpath = new int[lenght];
//        recursive(bestpath, lenght,0 , 0);

    }
}
