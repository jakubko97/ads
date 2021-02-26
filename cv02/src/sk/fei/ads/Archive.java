package cv02.src.sk.fei.ads;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Archive {

    static Map<Integer,Integer> penalty = new HashMap<>();
    static int[] intArray = new int[1000];

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
}
