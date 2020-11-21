package sample;

import com.opencsv.CSVReader;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    Ville _depart = new Ville();
    Ville _arrive = new Ville();

    LocalTime localTime = LocalTime.now();

    ArrayList<TrajetSimple> trajetSimples = new ArrayList<TrajetSimple>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Path path = new Path();
        Group root = new Group(path);
        Scene scene = new Scene(root, 800, 600);


        //Mettre le chemin des fichiers CSV
        final String ville = "/Users/thierryxu/Downloads/INSA/Informatique/Programmation JAVA/TP 3/projet-chemin/src/sample/villes.csv";
        final String bus = "/Users/thierryxu/Downloads/INSA/Informatique/Programmation JAVA/TP 3/projet-chemin/src/sample/bus.csv";
        final String car = "/Users/thierryxu/Downloads/INSA/Informatique/Programmation JAVA/TP 3/projet-chemin/src/sample/car.csv";
        final String train = "/Users/thierryxu/Downloads/INSA/Informatique/Programmation JAVA/TP 3/projet-chemin/src/sample/train.csv";

        try (
                Reader reader = Files.newBufferedReader(Paths.get(ville));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez votre ville de départ : ");
            String depart = scanner.nextLine();
            System.out.print("Entre votre ville d'arrivée : ");
            String arrive = scanner.nextLine();

            while ((nextRecord = csvReader.readNext()) != null) {
                if (depart.equals(nextRecord[0]) || arrive.equals(nextRecord[0])) {

                    if (depart.equals(nextRecord[0])) {
                        _depart = new Ville(nextRecord[0], Integer.parseInt(nextRecord[1]), Integer.parseInt(nextRecord[2]));

                        //Initilisation du texte du depart au dessus des points

                        Text textDepart = new Text(nextRecord[0]);
                        textDepart.setX(Integer.parseInt(nextRecord[1]) * 7 - 8);
                        textDepart.setY(Integer.parseInt(nextRecord[2]) * 7 - 8);

                        root.getChildren().add(textDepart);
                    } else if (arrive.equals(nextRecord[0])) {
                        _arrive = new Ville(nextRecord[0], Integer.parseInt(nextRecord[1]), Integer.parseInt(nextRecord[2]));

                        //Initilisation du texte d'arrive au dessus des points

                        Text textArrive = new Text(nextRecord[0]);
                        textArrive.setY(Integer.parseInt(nextRecord[2]) * 7 - 8);
                        textArrive.setX(Integer.parseInt(nextRecord[1]) * 7 - 8);

                        root.getChildren().add(textArrive);
                    }

                    //Initialisation des "villes" sur l'interface graphique
                    Circle circle = new Circle();
                    circle.setCenterX(Integer.parseInt(nextRecord[1]) * 7);
                    circle.setCenterY(Integer.parseInt(nextRecord[2]) * 7);
                    circle.setRadius(7);

                    root.getChildren().add(circle);
                }
            }
        }

        //Le lien du csv de bus
        Reader readerBus = Files.newBufferedReader(Paths.get(bus));
        CSVReader csvReaderBus = new CSVReader(readerBus);

        //Le lien du csv de bus
        Reader readerCar = Files.newBufferedReader(Paths.get(car));
        CSVReader csvReaderCar = new CSVReader(readerCar);

        //Le lien du csv de train
        Reader readerTrain = Files.newBufferedReader(Paths.get(train));
        CSVReader csvReaderTrain = new CSVReader(readerTrain);

        //Va traverser les 3 fichiers pour voir si le chemin est dans tel fichier
        initializeTrajets(csvReaderBus, root);
        initializeTrajets(csvReaderCar, root);
        initializeTrajets(csvReaderTrain, root);

        System.out.println(trajetSimples);

        //Initialisation de la legende
        Line lineBUS = new Line(20, 500, 100, 500);
        Line lineCAR = new Line(20, 525, 100, 525);
        Line lineTRAIN = new Line(20, 550, 100, 550);

        lineBUS.setStroke(Color.YELLOW);
        lineCAR.setStroke(Color.RED);
        lineTRAIN.setStroke(Color.BLUE);

        Text textBUS = new Text("BUS");
        textBUS.setX(115);
        textBUS.setY(505);

        Text textCAR = new Text("CAR");
        textCAR.setX(115);
        textCAR.setY(530);

        Text textTRAIN = new Text("TRAIN");
        textTRAIN.setX(115);
        textTRAIN.setY(555);

        root.getChildren().add(textBUS);
        root.getChildren().add(textCAR);
        root.getChildren().add(textTRAIN);

        root.getChildren().add(lineBUS);
        root.getChildren().add(lineCAR);
        root.getChildren().add(lineTRAIN);

        primaryStage.setTitle("Projet chemin - XU Thierry");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Méthode qui initialise les trajets en bus / car / train
    public void initializeTrajets(CSVReader csvReader, Group root) throws IOException {
        String[] nextRecord;

        while ((nextRecord = csvReader.readNext()) != null) {
            if (_depart.getNom().equals(nextRecord[0]) && _arrive.getNom().equals(nextRecord[1])) {
                //Initialise les horaires dans les temps et en heure
                String localTime;
                //Ne possède pas de 0 au début quand il n'y a pas 2 chiffres au début, comme 800
                if (nextRecord[3].length() < 4) {
                    localTime = nextRecord[3];
                    localTime = "0" + localTime;
                } else {
                    //Est retourné 1230 par exemple, donc 12h30
                    localTime = nextRecord[3];
                }

                LocalTime localTime1 = LocalTime.of(Integer.parseInt((localTime.charAt(0)) + String.valueOf(localTime.charAt(1))), Integer.parseInt((localTime.charAt(2)) + String.valueOf(localTime.charAt(3))));

                TrajetSimple trajetSimple = new TrajetSimple(_depart, _arrive, localTime1, Moyen.valueOf(nextRecord[2].toUpperCase()));
                addTrajetSimple(trajetSimple);

                Line line = new Line(_depart.getX() * 7, _depart.getY() * 7, _arrive.getX() * 7, _arrive.getY() * 7);
                root.getChildren().add(line);

                //Va prendre en parametre le trajet.simple.moyen pour voir quel est le moyen puis va setter la couleur donné
                switch (trajetSimple.moyen){
                    case BUS:
                        line.setStroke(Color.YELLOW);
                        break;
                    case CAR:
                        line.setStroke(Color.RED);
                        break;
                    case TRAIN:
                        line.setStroke(Color.BLUE);
                        break;
                }
            }
        }
    }

    //Ajoute un chemin possible choisi par le user dans une arraylist trajetsimples
    //Utile pour le trajet compose
    private void addTrajetSimple(TrajetSimple trajetSimple) {
        trajetSimples.add(trajetSimple);
    }

    public static void main(String[] args) {
        launch(args);
    }
}