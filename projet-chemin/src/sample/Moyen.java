package sample;

public enum Moyen {
    BUS(0.1, 40), CAR(0.3, 50), TRAIN(1, 70);
    /**cout en euro au km*/
    double cout;
    /**vitesse en km/h*/
    double v;
    Moyen(double _cout, double _v){cout = _cout; v = _v;}
}