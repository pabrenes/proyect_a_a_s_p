package Logica;

import java.io.Serializable;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Pista implements Serializable {

    private int derecha;
    private int abajo;

    public Pista(int derecha, int abajo) {
        this.derecha = derecha;
        this.abajo = abajo;
    }

    public int getDerecha() {
        return derecha;
    }

    public int getAbajo() {
        return abajo;
    }

    public void setAbajo(int abajo) {
        this.abajo = abajo;
    }

    public void setDerecha(int derecha) {
        this.derecha = derecha;
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
