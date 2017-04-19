package Logica;

/**
 * Created by Pablo Brenes on 18 abr 2017.
 */
public class Hilo extends Thread{

    boolean pause;
    int[] arreglo;

    public Hilo(int tamanoArreglo) {
        arreglo = new int[tamanoArreglo];
        pause = false;
    }

    @Override
    public void run() {

    }

}
