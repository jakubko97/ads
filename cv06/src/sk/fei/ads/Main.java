package sk.fei.ads;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Main {

    static int []pole = new int[1000];

    // https://www.geeksforgeeks.org/optimal-strategy-for-a-game-dp-31/
    // https://www.youtube.com/watch?v=WxpIHvsu1RI&ab_channel=TusharRoy-CodingMadeSimple
    // Returns optimal value possible
    // that a player can collect from
    // an array of coins of size n.
    // Note than n must be even
    static int optimalStrategyOfGame(
            int[] arr, int n)
    {
        // Create a table to store
        // solutions of subproblems
        int [][]table = new int[n][n];

        int gap, i, j , x, y, z, left, right;
        // Fill table using above recursive formula.
        // Note that the tableis filled in diagonal
        // fashion (similar to http:// goo.gl/PQqoS),
        // from diagonal elements to table[0][n-1]
        // which is the result.
        for (gap = 0; gap < n; ++gap) {
            for (i = 0, j = gap; j < n; ++i, ++j) {

                // Here x is value of F(i+2, j),
                // y is F(i+1, j-1) and z is
                // F(i, j-2) in above recursive formula
                x = ((i + 2) <= j) ? table[i + 2][j] : 0;
                y = ((i + 1) <= (j - 1)) ? table[i + 1][j - 1] : 0;
                z = (i <= (j - 2)) ? table[i][j - 2] : 0;

                left = (i + 1 <= j) ? (arr[i + 1] > arr[j] ? x : y) : 0;
                right = (i <= j - 1) ? (arr[i] > arr[j - 1] ? y : z) : 0;

                table[i][j] = Math.max(arr[i] + left, arr[j] + right);
            }
        }

        return table[0][n - 1];
    }

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
        List l = readFileInList("C:\\work\\xjancikj\\ads\\cv06\\ADS2021_cvicenie6data.txt");

        Iterator<String> itr = l.iterator();

        String line = itr.next();

        for (int i = 0; i < line.length(); i++) {
            pole[i] = Character.digit(line.charAt(i), 10);
            //Process char
        }
    }

    // Driver code
    static public void main (String[] args)
    {
        parseFileIntoArray();

        System.out.println(optimalStrategyOfGame(pole, pole.length));

    }
}
