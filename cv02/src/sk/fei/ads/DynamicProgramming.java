package cv02.src.sk.fei.ads;

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



    public static void findMinPen(){
        int kmPerDay = 400;
        double value = 0;
        int[] bestpath = new int[intArray.length];
        int[] stop = new int[intArray.length];

            for (int j=0; j < intArray.length; j++) {
                bestpath[j] = (int) (Math.pow((kmPerDay - intArray[j]), 2));
                System.out.println(bestpath[j]);
                stop[j] = 0;
                for (int k=0; k < j; k++) {
                    value = bestpath[k] + Math.pow(kmPerDay - (intArray[j] - intArray[k]),2);
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
        findMinPen();
    }
}
