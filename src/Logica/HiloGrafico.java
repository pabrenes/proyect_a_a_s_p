package Logica;

import Interfaz.Controlador;

/**
 * Created by Pablo Brenes 2016250460
 * 24 abr 2017.
 */
public class HiloGrafico extends Thread{

    Controlador control;

    public HiloGrafico(Controlador control) {
        this.control = control;
    }

    @Override
    public void run() {
        while (true) {
            if (HiloSolucionador.isSolucion()){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
