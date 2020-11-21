package sample;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TrajetCompose {
    /**
     * liste des trajets composant le trajet composé
     */
    ArrayList<TrajetSimple> trajets;
    /**
     * origine
     */
    Ville depart;
    /**
     * destination
     */
    Ville arrivee;
    /**
     * durée totale du trajet en minutes
     */
    int dureeTotale;
    /**
     * date de départ
     */
    LocalTime dateDepart;
    /**
     * date d'arrivée
     */
    LocalTime dateArrivee;
    /**
     * coût total
     */
    double coutTotal;

    public TrajetCompose() {
        trajets = new ArrayList<>();
    }

    /**
     * ajoute un voyage à la fin de la liste de voyages si ce voyage en est une continuation
     * (date de départ> date d'arrivée précédente et depart=arrivee précédente)
     *
     * @param nouveau voyage à ajouter
     */
    public boolean add(TrajetSimple nouveau) {
        boolean ajout = true;
        int nbTrajets = trajets.size();
        if (nbTrajets == 0) trajets.add(nouveau);
        else {
            TrajetSimple dernier = trajets.get(nbTrajets - 1);
            if (dernier.arrivee == nouveau.depart && dernier.dateArrivee.isBefore(nouveau.dateDepart))
                trajets.add(nouveau);
            else ajout = false;
        }
        if (ajout) calcule();
        return ajout;
    }

    /**
     * ajout d'une liste de trajets simple
     * (attention, pas de vérification de la continuité des trajets
     */
    public void add(List<TrajetSimple> listeTrajets) {
        trajets.addAll(listeTrajets);
        calcule();
    }

    /**
     * calcul du coût total, de la durée totale
     */
    private void calcule() {
        int nbTrajets = trajets.size();
        TrajetSimple premier = trajets.get(0);
        depart = premier.depart;
        TrajetSimple dernier = trajets.get(nbTrajets - 1);
        arrivee = dernier.arrivee;
        dureeTotale = (int) ChronoUnit.MINUTES.between(premier.dateDepart, dernier.dateArrivee);
        coutTotal = trajets.stream().mapToDouble(TrajetSimple::getCout).sum();
    }

    /**
     * retourne la duree totale
     */
    public int getDureeTotale() {
        return dureeTotale;
    }

    /**
     * retourne le cout total
     */
    public double getCoutTotal() {
        return coutTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("trajet compose, ");
        sb.append("duree=").append(dureeTotale);
        sb.append(" mn, cout total =").append(coutTotal).append(" \n");
        String tirets = "---";
        trajets.forEach(t -> sb.append(tirets).append(t).append("\n"));
        return sb.toString();
    }
}