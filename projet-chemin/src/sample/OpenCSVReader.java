package sample;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenCSVReader {
    private static final String train = "/Users/thierryxu/Downloads/INSA/Informatique/Programmation JAVA/TP 3/projet-chemin/src/sample/train.csv";

    public static void main(String[] args) throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(train));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println("origine : " + nextRecord[0]);
                System.out.println("destination : " + nextRecord[1]);
                System.out.println("means : " + nextRecord[2]);
            }
        }
    }
}