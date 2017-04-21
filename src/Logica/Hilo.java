package Logica;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Hilo extends Thread{

    private static boolean solucion = false;
    private static Kakuro kakuro;
    private static int topeHilos;
    private static int hilosEnEjecucion = 0;
    private boolean pause;
    private int[][] copiaTablero;

    public Hilo(int[][] tablero) {
        copiaTablero = tablero;
        pause = false;
    }

    @Override
    public void run() {
        while (true) {
            if (solucion)
                break;

        }
    }

    public static void setKakuro(Kakuro _kakuro) {
        kakuro = _kakuro;
    }

    public static void setTopeHilos(int tope) {
        topeHilos = tope;
    }

}
