package sample;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Arrays;

import static sample.Ville.getDist;

public class Tests {

    static void testEnumMoyens()
    {
        for(Moyen m: Moyen.values())
            System.out.printf("%s :: cout = %f €/km, vitesse = %f km/h %n", m, m.cout, m.v);
            System.out.println("---");
            System.out.println(Arrays.toString(Moyen.values()));
    }

    /*static void testVilles() {
        for (Ville v1 : Ville) {
            for (Ville v2 : Ville.values()) {
                int d = (int) Ville.getDist(v1, v2);
                if (d > 0)
                    System.out.printf("distance entre %s et %s = %d km%n", v1, v2, d);
            }
            System.out.println("------");
        }

    }*/

    public static void  main(String[] args)
    {

    }
}