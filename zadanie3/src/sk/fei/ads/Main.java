package sk.fei.ads;

// Java implementation to find if the given
// expression is satisfiable using the
// Kosaraju's Algorithm
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class SatSolver{

    static final int MAX = 100000;

    // Data structures used to implement Kosaraju's
// Algorithm. Please refer
// http://www.geeksforgeeks.org/strongly-connected-components/
    @SuppressWarnings("unchecked")
    static List<List<Integer> > adj = new ArrayList();

    @SuppressWarnings("unchecked")
    static List<List<Integer> > adjInv = new ArrayList();
    static boolean[] visited = new boolean[MAX];
    static boolean[] visitedInv = new boolean[MAX];
    static Stack<Integer> s = new Stack<Integer>();

    static String[] intArray = new String[1000];
    static boolean[] variablesValue = new boolean[100];
    static int numberVariables = 0;
    static int numberClauses = 0;
    // This array will store the SCC that the
// particular node belongs to
    static int[] scc = new int[MAX];

    // counter maintains the number of the SCC
    static int counter = 1;

    // Adds edges to form the original graph void
    static void addEdges(int a, int b)
    {
        adj.get(a).add(b);
    }

    // Add edges to form the inverse graph
    static void addEdgesInverse(int a, int b)
    {
        adjInv.get(b).add(a);
    }

    // For STEP 1 of Kosaraju's Algorithm
    static void dfsFirst(int u)
    {
        if (visited[u])
            return;

        visited[u] = true;

        for(int i = 0; i < adj.get(u).size(); i++)
            dfsFirst(adj.get(u).get(i));

        s.push(u);
    }

    // For STEP 2 of Kosaraju's Algorithm
    static void dfsSecond(int u)
    {
        if (visitedInv[u])
            return;

        visitedInv[u] = true;

        for(int i = 0; i < adjInv.get(u).size(); i++)
            dfsSecond(adjInv.get(u).get(i));

        scc[u] = counter;
    }

    // Function to check 2-Satisfiability
    static void is2Satisfiable(int n, int m,
                               int a[], int b[])
    {

        // Adding edges to the graph
        for(int i = 0; i < m; i++)
        {

            // variable x is mapped to x
            // variable -x is mapped to n+x = n-(-x)

            // for a[i] or b[i], addEdges -a[i] -> b[i]
            // AND -b[i] -> a[i]
            if (a[i] > 0 && b[i] > 0)
            {
                addEdges(a[i] + n, b[i]);
                addEdgesInverse(a[i] + n, b[i]);
                addEdges(b[i] + n, a[i]);
                addEdgesInverse(b[i] + n, a[i]);
            }

            else if (a[i] > 0 && b[i] < 0)
            {
                addEdges(a[i] + n, n - b[i]);
                addEdgesInverse(a[i] + n, n - b[i]);
                addEdges(-b[i], a[i]);
                addEdgesInverse(-b[i], a[i]);
            }

            else if (a[i] < 0 && b[i] > 0)
            {
                addEdges(-a[i], b[i]);
                addEdgesInverse(-a[i], b[i]);
                addEdges(b[i] + n, n - a[i]);
                addEdgesInverse(b[i] + n, n - a[i]);
            }

            else
            {
                addEdges(-a[i], n - b[i]);
                addEdgesInverse(-a[i], n - b[i]);
                addEdges(-b[i], n - a[i]);
                addEdgesInverse(-b[i], n - a[i]);
            }
        }

        // STEP 1 of Kosaraju's Algorithm which
        // traverses the original graph
        for(int i = 1; i <= 2 * n; i++)
            if (!visited[i])
                dfsFirst(i);

        // STEP 2 pf Kosaraju's Algorithm which
        // traverses the inverse graph. After this,
        // array scc[] stores the corresponding value
        while (!s.isEmpty())
        {
            int top = s.peek();
            s.pop();


            if (!visitedInv[top])
            {
                dfsSecond(top);
                counter++;
            }
        }

        for(int i = 1; i <= n; i++)
        {

            // For any 2 variable x and -x lie in
            // same SCC
            if (scc[i] == scc[i + n])
            {
                System.out.println("NESPLNITELNA");
                return;
            }
            variablesValue[i] = scc[i] > scc[i + n];
        }


        // No such variables x and -x exist which lie
        // in same SCC
        System.out.println("SPLNITELNA");
    }

    static void printResult(){
        for(int i = 0; i < numberVariables; i++) {
            if (variablesValue[i]) {
                System.out.println("PRAVDA");

            } else {
                System.out.println("NEPRAVDA");

            }
        }
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
        List l = readFileInList("C:\\work\\xjancikj\\ads\\zadanie3\\input.txt");

        Iterator<String> itr = l.iterator();

        int i = 0;
        while (itr.hasNext()) {
            intArray[i] = itr.next();
            i++;
        }
    }

    // Driver code
    public static void main(String[] args)
    {

        parseFileIntoArray();

        String[] values = intArray[0].split(" ");
        // n is the number of variables
        // 2n is the total number of nodes
        // m is the number of clauses
        numberVariables = Integer.valueOf(values[0]);
        numberClauses = Integer.valueOf(values[1]);
        int a[] = new int[1000], b[] = new int[1000];
        for(int i = 0; i < MAX; i++)
        {
            adj.add(new ArrayList<Integer>());
            adjInv.add(new ArrayList<Integer>());
        }
        for(int j=1; j < numberClauses; j++){
            String[] vars = intArray[j].split(" ");
            int k = 0;
            while(k < vars.length){
                if(k == 0){
                    a[j] = Integer.valueOf(vars[k]);
                }
                if(k == 1) {
                    if (Integer.valueOf(vars[k]) == 0) {
                        return;
                    }
                    b[j] = Integer.valueOf(vars[k]);
                }
                k++;
            }

        }

        // Each clause is of the form a or b
        // for m clauses, we have a[m], b[m]
        // representing a[i] or b[i]

        // Note:
        // 1 <= x <= N for an uncomplemented variable x
        // -N <= x <= -1 for a complemented variable x
        // -x is the complement of a variable x

        // The CNF being handled is:
        // '+' implies 'OR' and '*' implies 'AND'
        // (x1+x2)*(x2â+x3)*(x1â+x2â)*(x3+x4)*(x3â+x5)*
        // (x4â+x5â)*(x3â+x4)
//        int a[] = { 1, -2, -1, 3, -3, -4, -3 };
//        int b[] = { 2, 3, -2, 4, 5, -5, 4 };

        // We have considered the same example
        // for which Implication Graph was made
        is2Satisfiable(numberVariables, numberClauses, a, b);
        printResult();
    }
}

// This code is contributed by jithin
