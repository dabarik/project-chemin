package sample;

import java.time.LocalTime;

public class TrajetSimple implements  Cloneable {
    /** origine */
    Ville depart;
    /** destination */
    Ville arrivee;
    /** moyen de transport*/
    Moyen moyen;
    /** duress du voyage en minutes */
    int duree;
    /** longueur du parcours en km */
    double distance;
    /** date de depart */
    LocalTime dateDepart;
    /** date d'arrivee */
    LocalTime dateArrivee;
    /** cout */
    double cout;

    TrajetSimple() { }

    /**constructeur où les arrivée, depart, moyen sont en chaine de caractères
     * et où la date de départ est une chaîne de type hhmm*/

    /**constructeur de TrajetSimple*/
    public TrajetSimple(Ville _depart, Ville _arrivee,  LocalTime _dateDepart, Moyen _moyen) {
        depart = _depart;
        arrivee = _arrivee;
        distance  = Ville.getDist(depart,  arrivee);
        moyen = _moyen;
        dateDepart = _dateDepart;
        calcule();
    }

    /**calcule la durée, la date d'arrivée, et le coût en fonction des villes
     * et du moyen de transport (si distance==-1 (car aucun trajet direct possible
     * entre les villes), cout=duree=-1 et date d'arrivee = null))*/
    private void calcule() {
        duree = (distance==-1?-1:(int) (60d*distance / moyen.v));
        dateArrivee = (distance==-1?null:dateDepart.plusMinutes((int)duree));
        cout = (distance==-1?-1:distance*moyen.cout);
    }

    /**change le moyen de transport utilisé*/
    public void setMoyen(Moyen _moyen)
    { moyen = _moyen; calcule(); }
    /**retourne le cout du voyage*/
    public double getCout() { return cout; }
    /**retourne la ville de depart*/
    public Ville getDepart() { return depart; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("trajet de ");
        sb.append(depart.getNom()).append(" à ").append(arrivee.getNom());
        sb.append(" par ").append(moyen);
        sb.append("\n Le depart est à : ").append(dateDepart);
        sb.append("\n L'arrivee est à : ").append(dateArrivee);
        sb.append("\n Le cout est de :  ").append(cout);
        sb.append("\n distance est de :  ").append(distance);
        sb.append("\n La duree du trajet est de : ").append(duree);
        return sb.toString();
    }

    @Override
    protected TrajetSimple clone() {
        TrajetSimple clone=null;
        try { clone = (TrajetSimple) super.clone(); }
        catch (CloneNotSupportedException e) { e.printStackTrace();}
        return (TrajetSimple)clone;
    }
}