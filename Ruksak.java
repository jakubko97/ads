import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Ruksak {

    static int[][] polozky = new int[1000][4];

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

    public static void parseFileIntoArray(){
        List l = readFileInList("C:\\work\\xjancikj\\git\\ADS2021_cvicenie4data.txt");

        Iterator<String> itr = l.iterator();

        int i = 0;
        String[] line;
        while (itr.hasNext()) {
            line = itr.next().split(",");;
            for(int j=0; j< 4; j++){
                polozky[i][j] = Integer.valueOf(line[j]);
            }
            i++;
        }
    }

    // val[] is for storing maximum profit for each weight
// wt[] is for storing weights
// n number of item
// W maximum capacity of bag
// dp[W+1] to store final result
    static int KnapSack(int values[][], int n, int W)
    {
        // array to store final result
        int[][] results = new int[values.length + 1][W + 1]; // 2

        //initially profit with 0 to W KnapSack capacity is 0

        // iterate through all items
        for(int i=1; i <= n; ++i) {

            //top-bottom
            for (int j = W; j >= 1; --j) {
                results[i][j] = results[i - 1][j]; // 5
                int candidate;
                int candidate2;

                if (values[i - 1][1] <= j){
                    candidate = results[i - 1][j - values[i - 1][1]] + values[i - 1][0];
                    if(candidate >= results[i][j]){
                        results[i][j] = candidate;
                    }
                }
                if (values[i - 1][3] <= j) {
                    candidate2 = results[i - 1][j - values[i - 1][3]] + values[i - 1][2];
                    if(candidate2 >= results[i][j]){
                        results[i][j] = candidate2;
                    }
                }
//                results[i][j] = Math.max(candidate, candidate2);
//                System.out.print(results[i][j] + " ");
            }
            System.out.println(" ");
            System.out.println(results[i][W]);
        }
        return results[values.length][W];
    }


    public static void main(String[] args)
    {
        parseFileIntoArray();

//        for(int m=0; m<polozky.length; m++){
//            for(int n=0; n<polozky[m].length; n++){
//                System.out.print(polozky[m][n]);
//            }
//            System.out.println();
//        }
        int W = 2000, n = polozky.length;
        System.out.println(KnapSack(polozky, n, W));

    }
}
