package Logica;

import java.util.HashSet;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Hilo extends Thread{

    private static boolean solucion = false;
    private static Kakuro kakuro;
    private static int topeHilos;
    private static int hilosEnEjecucion = 1;
    private int[][] copiaTablero;
    private int k;
    private long timeStart;

    public Hilo(int[][] tablero, int k) {
        copiaTablero = tablero;
        this.k = k;
    }

    @Override
    public void run() {
        if (k == kakuro.getTotalCasillas()) {
            kakuro.setTablero(copiaTablero);
            double time = (System.nanoTime() - timeStart)/1000;
            time /= 1000;
            System.out.println(time);
            solucion = true;
        }
        else {
            int[] parOrdenado = kakuro.getCasillas().get(k);
            HashSet<Integer> sucesores = kakuro.obtenerSucesores(parOrdenado[0], parOrdenado[1], copiaTablero);
            for (Integer sucesor : sucesores) {
                copiaTablero[parOrdenado[0]][parOrdenado[1]] = sucesor;
                if (hilosEnEjecucion <= topeHilos){
                    hilosEnEjecucion++;
                    new Hilo(copiaTablero(), k + 1).start();
                } else {
                    resolverKakuroBT(k + 1);
                }
                if (solucion)
                    break;
                copiaTablero[parOrdenado[0]][parOrdenado[1]] = 0;
            }
        }
        hilosEnEjecucion--;

    }

    private void resolverKakuroBT(int k) {
        if (k == kakuro.getTotalCasillas()) {
            kakuro.setTablero(copiaTablero);
            double time = (System.nanoTime() - timeStart)/1000;
            time /= 1000;
            System.out.println(time);
            solucion = true;
        }
        else {
            int[] parOrdenado = kakuro.getCasillas().get(k);
            HashSet<Integer> sucesores = kakuro.obtenerSucesores(parOrdenado[0], parOrdenado[1], copiaTablero);
            for (Integer sucesor : sucesores) {
                copiaTablero[parOrdenado[0]][parOrdenado[1]] = sucesor;
                if (solucion)
                    break;
                copiaTablero[parOrdenado[0]][parOrdenado[1]] = 0;
            }
        }
    }

    static void setKakuro(Kakuro _kakuro) {
        kakuro = _kakuro;
    }

    static void setTopeHilos(int tope) {
        topeHilos = tope;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    private int[][] copiaTablero() {
        int[][] copia = new int[14][14];
        for (int i = 0; i < 14; i++) {
            copia[i] = copiaTablero[i].clone();
        }
        return copia;
    }
}
