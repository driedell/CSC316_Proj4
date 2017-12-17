import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Proj4 {
    static ArrayList<String>[] myHashTable = new ArrayList[31393];
    static int dictionarySize = 0;
    static int probes = 0;
    static int lookups = 0;
    static int wordsChecked = 0;
    static int misspelledWords = 0;

    public static void main(String[] args) {

        String inputFileName;
        String dictionary;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

//            String dictionary = "C:\\Users\\driedell\\Desktop\\CSC316\\Proj4\\dict.txt";
//            String inputFileName = "C:\\Users\\driedell\\Desktop\\CSC316\\CSC316_Proj1\\original_files\\input1.txt";
//            String inputFileName = "C:\\Users\\driedell\\Desktop\\CSC316\\Proj4\\HP1_test.txt";

            System.out.print("Enter dictionary file: ");
            dictionary = br.readLine();
            if (dictionary.contains("\"")) {
                dictionary = dictionary.replace("\"", "");
            }

            System.out.println("Enter text file: ");
            inputFileName = br.readLine();
            if (inputFileName.contains("\"")) {
                inputFileName = inputFileName.replace("\"", "");
            }

            br = new BufferedReader(new FileReader(dictionary));

            String line;
            while ((line = br.readLine()) != null) {
                dictionarySize++;

                int index = Hash(line);
                ArrayList<String> myBucket;

                if (myHashTable[index] == null) {
                    myBucket = new ArrayList<>();
                    myBucket.add(line);
                    myHashTable[index] = myBucket;
                }
                else {
                    myBucket = myHashTable[index];
                    myBucket.add(line);
                    myHashTable[index] = myBucket;
                }
            }

            br = new BufferedReader(new FileReader(inputFileName));

            line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"")) {
                    line = line.replace("\"", "");
                }


                String[] lineArray = line.split("[^\\w']+");
//                System.out.println(lineArray);

                wordsChecked += lineArray.length;

                for (String s : lineArray) {
                    if (!lookupSuite(s)) {
                        System.out.println(s);
                        misspelledWords++;
                    }
                }
            }
            br.close();

        } catch (Exception e) {

        }

        System.out.println();
        System.out.println("1) Dictionary size: " + dictionarySize);
        System.out.println("2) Word count: " + wordsChecked);
        System.out.println("3) Misspelled words: " + misspelledWords);
        System.out.println("4) Probe count: " + probes + ", Lookup count: " + lookups);
        System.out.println("5) Probes per word: " + (double) probes/wordsChecked);
        System.out.println("6) Probes per lookup: " + (double) probes/lookups);

    }

    public static boolean lookupSuite(String myString) {
        lookups++;
        if (lookup(myString)) {
            return true;
        }
        myString = myString.toLowerCase();
        lookups++;
        if (lookup(myString)) {
            return true;
        }
        if (myString.endsWith("'s")) {
            // removes "'s"
            myString = myString.substring(0, myString.length()-2);
            lookups++;
            if (lookup(myString)) return true;
        }
        lookups++;
        if (myString.endsWith("s") || myString.endsWith("es")) {
            // removes "s"
            myString = myString.substring(0, myString.length()-1);
            lookups++;
            if (lookup(myString)) return true;
            // removes "es"
            myString = myString.substring(0, myString.length()-1);
            lookups++;
            if (lookup(myString)) return true;
        }
        if (myString.endsWith("ed")) {
            // removes "ed"
            myString = myString.substring(0, myString.length()-2);
            lookups++;
            if (lookup(myString)) return true;
            // removes "d"
            myString = myString + "e";
            lookups++;
            if (lookup(myString)) return true;
        }
        if (myString.endsWith("er")) {
            // removes "er"
            myString = myString.substring(0, myString.length()-2);
            lookups++;
            if (lookup(myString)) return true;
            // removes "r"
            myString = myString + "e";
            lookups++;
            if (lookup(myString)) return true;
        }
        if (myString.endsWith("ing")) {
            // removes "ing"
            myString = myString.substring(0, myString.length()-3);
            lookups++;
            if (lookup(myString)) return true;
            // adds "e"
            myString = myString + "e";
            lookups++;
            if (lookup(myString)) return true;

        }
        if (myString.endsWith("ly")) {
            // removes "ly"
            myString = myString.substring(0, myString.length()-2);
            lookups++;
            if (lookup(myString)) return true;
        }

//        System.out.println(myString);
        return false;
    }



    public static boolean lookup(String myString) {
//        System.out.println(myString);
        int index = Hash(myString);

        if (myHashTable[index] == null) {
            return false;
        }
        else {
            for (String s : myHashTable[Hash(myString)]) {
                probes++;
                if (s.equals(myString)) {
                    return true;
                }
            }
            return false;
        }
    }

    // Convert the first 8 chars of the string padded by spaces if necessary into a long,
    // index = abs(long % 31393)
    // 31393 is the closest prime to 1.25*21544
    public static int Hash(String line) {
        line = String.format("%8s", line);
        char[] myCharArray = line.toCharArray();

        long myLong = 0;
        for (int i = 0; i < 8; i++) {
            myLong = (myLong << 8) + ((byte) myCharArray[i]);
        }

        int myIndex = (int) Math.abs(myLong % 31393);
//        System.out.println(myIndex + "," + line);

        return myIndex;
    }
}
