package Logica;

/**
 * Created by Pablo Brenes - 2016250460
 * 14 abr 2017.
 */
public class Pista {

    int derecha;
    int abajo;

    public Pista(int derecha, int abajo) {
        this.derecha = derecha;
        this.abajo = abajo;
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
