package Logica;

import javax.swing.*;
import java.io.*;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Kakuro implements Serializable{

    static int[] numeroMaximo = {9, 17, 24, 30, 35, 39, 42, 44, 45};
    static int[] numeroMinimo = {1, 3, 6, 10, 15, 21, 28, 36, 45};

    private int[][] tablero;
    private Pista[][] pista;

    public Kakuro() {
        tablero = new int[14][14];
        pista = new Pista[14][14];
    }

    public void ingresarPista (int fila, int columna, int derecha, int abajo) {
        pista[fila][columna] = new Pista(derecha, abajo);
    }

    private int maxParaCasillas(int cantCasillas) {
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

    public void setPista(Pista[][] pista){
        this.pista = pista;
    }

    public void guardarKakuro(String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Archivo no encontrado");
        }
    }

    public void cargarKakuro(String path) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            Object aux = ois.readObject();
            if (aux instanceof Kakuro){
                this.setTablero(((Kakuro) aux).getTablero());
                this.setPista(((Kakuro) aux).getPista());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Archivo no encontrado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Archivo incorrecto o corrupto");
        }
    }

}
