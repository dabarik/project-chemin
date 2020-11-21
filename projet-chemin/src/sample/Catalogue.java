package sample;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Catalogue {
    /**table origine, liste de trajets à partir de origine*/
    Map<Ville, List<TrajetSimple>> catalogue;

    Catalogue() { catalogue = new HashMap<>();}

    /**ajout d'un trajet à la bonne place dans la table
     * (fonction non utilisée ici)*/
    void addTrajetSimple(TrajetSimple trajet) {
        catalogue.compute(trajet.depart,
                (v, l) -> {if(l==null) {l = new ArrayList<>();} l.add(trajet); return l;});
    }


    /** trouver tous les chemins directs entre départ et arrivée à partir d'une date
     * plus un certain retard
     *
     * @param depart  ville de depart
     * @param arrivee  ville d'arrivee
     * @param dateDepart date de depart
     * @param delaiMax minutes de retard autorisees
     * @return list of all the direct journeys between start and stop
     */
    List<TrajetSimple> trouveCheminsDirects(Ville depart, Ville arrivee, LocalTime dateDepart, int delaiMax) {
        List<TrajetSimple> cheminsDirects = null;
        List<TrajetSimple> trajets = catalogue.get(depart);
        if (trajets != null) {
            cheminsDirects = new ArrayList<>(List.copyOf(trajets));
            LocalTime dateDepartMax = dateDepart.plusMinutes(delaiMax);
            cheminsDirects.removeIf(t->(t.arrivee != arrivee  || t.dateDepart.isBefore(dateDepart) || t.dateDepart.isAfter(dateDepartMax) ) );
            if (cheminsDirects.isEmpty()) cheminsDirects = null;
        }
        return cheminsDirects ;
    }

    /**crée les trajets de l'énoncé et les ajoute à la table*/
    /*public void creerCatalogue()
    {
        List<TrajetSimple> trajets = new ArrayList<>();
        for(Ville x:Ville.values())
            for(Ville y:Ville.values())
            {
                if (Ville.getDist(x, y)>0)
                {
                    TrajetSimple ts = new TrajetSimple(x, y, LocalTime.of(6, 0), Moyen.BUS);
                    if( (x==Ville.A && y==Ville.F) || (x==Ville.F && y==Ville.A) ) ts.setMoyen(Moyen.TRAIN);
                    if( (x==Ville.A && y==Ville.D) || (x==Ville.D && y==Ville.A) ) ts.setMoyen(Moyen.TRAM);
                    if( (x==Ville.D && y==Ville.F) || (x==Ville.F && y==Ville.D) ) ts.setMoyen(Moyen.TRAM);
                    trajets.add(ts);
                    for(int i=1; i<14;i++) {
                        TrajetSimple tsSuite = ts.clone();
                        tsSuite.dateDepart = ts.dateDepart.plusMinutes(i*30);
                        tsSuite.dateArrivee = ts.dateArrivee.plusMinutes(i*30);
                        trajets.add(tsSuite);
                    }
                }
            }
//génère la map en groupant les trajets par ville de départ
        catalogue = trajets.stream().collect(Collectors.groupingBy(TrajetSimple::getDepart));
    }*/

    /**
     * calcule les chemins directs et indirects possibles entre 2 villes
     * a partir d'une date donne avec un retard et un delai entre voyage autorise
     * @param depart  vile de depart
     * @param arrivee ville d'arrivee
     * @param date date de depart souhaitee
     * @param delai delai maximal autorise avant de partir, ou entre 2 voyages
     * @param voyageEnCours voyage en train d'etre construit
     * @param via liste des villes visitees par le voyage
     * @param results liste de tous les chemins indirects possibles
     * @return true si au moins un chemin a ete trouve
     */
    public boolean trouverCheminIndirect(Ville depart, Ville arrivee, LocalTime date, int delai, List<TrajetSimple> voyageEnCours, List<Ville> via, List<TrajetCompose> results) {
        boolean result=false;
        via.add(depart);
        //recherche des trajets à partir de depart
        List<TrajetSimple> liste = new ArrayList<>(catalogue.get(depart));
        if (liste.isEmpty()) return false;
        //calcul de la date de depart au plus tard
        LocalTime dateDepartMax = date.plusMinutes(delai);
        //retrait des trajets partant trop tot ou trop tard
        liste.removeIf(t->(t.dateDepart.compareTo(date)<0)||t.dateDepart.compareTo(dateDepartMax)>0);
        for (TrajetSimple t : liste) {
            //si on trouve un trajet menant à l'arrivée
            if (t.arrivee == arrivee) {
                //on l'ajoute au voyage en cours
                voyageEnCours.add(t);
                //on cree un nouveau trajet compose reprenant le detail du voyage
                TrajetCompose compo = new TrajetCompose();
                compo.add(List.copyOf(voyageEnCours));
                //on l'ajoute au resultat
                results.add(compo);
                //on retire le dernier trajet pour éventuellement en cherche un autre (plus rapide, moins cher, ...)
                voyageEnCours.remove(voyageEnCours.size() - 1);
            } else {
                //si le trajet ne mène pas à l'arrivee mais donc à un via
                if (!via.contains(t.arrivee)) {
                    //on l'ajoute au voyage en cours
                    voyageEnCours.add(t);
                    //on cherche à partir du via vers l'arrivee
                    trouverCheminIndirect(t.arrivee, arrivee, t.dateArrivee, delai, voyageEnCours, via, results);
                    //on retire les derniers ajouts pour chercher d'autres chemins
                    via.remove(t.arrivee);
                    voyageEnCours.remove(t);
                }
            }
        }
        result = !results.isEmpty();
        return result;
    }

    /*TEST DES CLASSES*/
    /*public static void main(String[] args) {
        Catalogue cata = new Catalogue();
        cata.creerCatalogue();

        ArrayList<TrajetSimple> ts = (ArrayList<TrajetSimple>) cata.trouveCheminsDirects(Ville.A, Ville.E, LocalTime.of(8, 00), 15);
        if(ts!=null)
            for(TrajetSimple t:ts) System.out.println(t);
        else
            System.out.println("aucun trajet trouve");

        ArrayList<TrajetCompose> voyages = new ArrayList<>();
        boolean result = cata.trouverCheminIndirect(Ville.A, Ville.E, LocalTime.of(8,00), 15,  new ArrayList<TrajetSimple>(), new ArrayList<Ville>(), voyages);
        if(result)
            voyages.forEach(System.out::println);
        else
            System.out.println("aucun trajet trouve");

        TrajetCompose plusRapide = Collections.min(voyages, Comparator.comparingInt(TrajetCompose::getDureeTotale));
        System.out.println("plus rapide = " + plusRapide);

        TrajetCompose moinsCouteux = Collections.min(voyages, Comparator.comparingDouble(TrajetCompose::getCoutTotal));
        System.out.println("moins couteux  = " + moinsCouteux);
    }
     */
}