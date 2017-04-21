package Logica;

/**
 * Created by Pablo Brenes ft Jeison E. Samudio
 * 13 abr 2017.
 */

public class Kakuro {

    private int[][] tablero;
    private Pista[][] pista;

    static int[] numeroMaximo = {9, 17, 24, 30, 35, 39, 42, 44, 45};
    static int[] numeroMinimo = {1, 3, 6, 10, 15, 21, 28, 36, 45};

    public Kakuro() {
        tablero = new int[14][14];
        pista = new Pista[14][14];
    }

    public void ingresarPista (int fila, int columna, int derecha, int abajo) {
        pista[fila][columna] = new Pista(derecha, abajo);
    }

    private int maxParaCasillas (int cantCasillas) {
        int total = 0;
        return total;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public Pista[][] getPista() {
        return pista;
    }

    public void setTablero(int[][] tablero) {
        this.tablero = tablero;
    }
}
