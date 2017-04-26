package Logica;

import java.util.HashSet;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class HiloSolucionador extends Thread{

    private static boolean solucion = false;
    private static Kakuro kakuro;
    private static int topeHilos;
    private static int hilosEnEjecucion = 1;
    private int[][] copiaTablero;
    private int k;
    private static long timeStart;

    HiloSolucionador(int[][] tablero, int k) {
        copiaTablero = tablero;
        this.k = k;
    }

    @Override
    public void run() {
        resolverKakuroBT(k);
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
                if (hilosEnEjecucion < topeHilos){
                    hilosEnEjecucion++;
                    new HiloSolucionador(copiaTablero(), k + 1).start();
                } else {
                    resolverKakuroBT(k + 1);
                }
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

    void setTimeStart(long startTime) {
        timeStart = startTime;
    }

    public static void clearHilo() {
        solucion = false;
        kakuro = null;
        topeHilos = 0;
        hilosEnEjecucion = 1;
    }

    private int[][] copiaTablero() {
        int[][] copia = new int[14][14];
        for (int i = 0; i < 14; i++) {
            copia[i] = copiaTablero[i].clone();
        }
        return copia;
    }

    public static boolean isSolucion() {
        return solucion;
    }
}
