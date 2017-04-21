package Logica;

/**
 * Created by Pablo Brenes - 2016250460
 * 14 abr 2017.
 */
public class Pista {

    int derecha;
    int abajo;

    public Pista() {
        derecha = 0;
        abajo = 0;
    }

    public String getStringDerecha() {
        if (derecha != 0)
            return "" + derecha;
        return "";
    }

    public String getStringAbajo() {
        if (abajo != 0)
            return "" + abajo;
        return "";
    }

}
