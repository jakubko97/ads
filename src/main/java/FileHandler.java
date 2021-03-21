import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**Usage from different class, if it already wasnt obvious enough
 *
 * FileHandler fileHandler = new FileHandler();
 * fileHandler.loadFile("dictionary.txt");
 * File file = fileHandler.getFile();
 *
 * or by invoking the handleFile() method :)
 *
 * because we dont actually need the instance of the class in runtime I just
 * added static signature to the methods that needed it
 *
 * Map<String, Integer> dictionary = FileHandler.handleFile("dictionary2.txt");
 */

public class FileHandler {
    private static File file;

    public FileHandler(){ }

    public static void loadFile(String fileName){
        FileHandler fileHandler = new FileHandler();
        file = fileHandler.getFileFromResources(fileName);
    }

    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {
        //returns the runtime class of an object
        ClassLoader classLoader = getClass().getClassLoader();  //kvoli tomuto musi byt ta metoda private, v podstate potrebuje sameho seba

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }


    public static Map<String, Integer> parseFileToList(File file) throws IOException {
        if (file == null) return null;
        //TreeMap will automatically put entries sorted by keys
        //which are the words in our case
        Map<String, Integer> dictionary = new TreeMap<>();
        String delimiter = " ";

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                //parsedFile.add(line);   //substitute by sout if u wanna print it
                dictionary.put(line.split(delimiter)[1], Integer.parseInt(line.split(delimiter)[0]));
            }
        }
        return dictionary;
    }

    public static Map<String, Integer> handleFile(String fileName) throws IOException {
        loadFile(fileName);
        return parseFileToList(file);
    }
}
