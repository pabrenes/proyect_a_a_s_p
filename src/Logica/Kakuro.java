package Logica;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */
//up
public class Kakuro implements Serializable{

    static int[] numeros = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static int[] numeroMaximo = {9, 17, 24, 30, 35, 39, 42, 44, 45};
    static int[] numeroMinimo = {1, 3, 6, 10, 15, 21, 28, 36, 45};

    private int[][] tablero;
    private Pista[][] pista;

    public Kakuro() {
        tablero = new int[14][14];
        pista = new Pista[14][14];
    }

    //De momento se usa para probar las posibles soluciones a un casilla del kakuro
    public void resolverKakuro(int fila, int columna) {
        ArrayList<Integer> values = obtenerSucesores(fila, columna);

    }

    private boolean promete() {
        boolean laVaraPromete = false;
        return true;
    }

    private ArrayList<Integer> obtenerSucesores(int fila, int columna) {
        ArrayList<Integer> values = new ArrayList<>();                                                                  //Valores totales para los posibles sucesores
        int[] pistas = getPistas(fila, columna);                                                                        //Obtengo la posición de las pistas en el tablero
        int derecha, abajo, min;                                                                                        //Pista horizontal y vertical de la casilla y el minimo de estas
        derecha = pista[fila][pistas[0]].getDerecha();                                                                  //Obtengo la pista horizontal
        abajo = pista[pistas[1]][columna].getAbajo();                                                                   //Obtengo la pista vertical
        ArrayList<Integer>[] runs = getRuns(fila, ++pistas[0], ++pistas[1], columna);                                   //Obtengo los datos de las runs
        if (runs[0].remove(runs[0].size() - 1) == 1) {                                                            //Determino si no era la ultima casilla
            derecha--;                                                                                                  //Si no es la ultima no puedo colocar el valor de la pista
        }
        derecha -= runs[0].remove(runs[0].size() - 1);                                                            //Determino la nueva pista horizontal
        if (runs[1].remove(runs[1].size() - 1) == 0) {                                                            //Determino si era la ultima casilla
            abajo--;                                                                                                    //Si no es la ultima no puedo colocar el valor de la pista
        }
        abajo -= runs[1].remove(runs[1].size() - 1);                                                              //Determino la nueva pista vertical
        min = Math.min(derecha, abajo);                                                                                 //El valor de la mista está dado por el menor
        if (min > 9){
            min = 9;                                                                                                    //Si es mayor a 9 baje a 9
            for (int i = min - 1; i > -1; i--) {                                                                        //Solo puedo colocar números menores al mínimo (ves static array numeros)
                values.add(numeros[i]);                                                                                 //Agrega el valor
            }
        }
        else {
            for (int i = 0; i < min; i++) {                                                                             //Solo puedo colocar números menores al mínimo (ves static array numeros)
                values.add(numeros[i]);                                                                                 //Agrega el valor
            }
        }
        values.removeAll(runs[0]);                                                                                      //Elimino repetidos horizontalmente
        values.removeAll(runs[1]);                                                                                      //Elimino repetidos verticalmente
        return values;
    }

    private ArrayList<Integer>[] getRuns(int filaD, int columnaD, int filaA, int columnaA) {
        ArrayList<Integer>[] runs = new ArrayList[2];                                                                   //Contenedor para los valores en las runs
        int sumaD = 0, sumaA = 0;                                                                                       //Control para la suma
        runs[0] = new ArrayList<>();
        runs[1] = new ArrayList<>();
        //Se determinan todos los valores en la run horizontal para una casilla, además de la suma de estos valores
        while (columnaD < 14 && tablero[filaD][columnaD] > 0) {                                                         //Mientras no me salga del kakuro y sea un valor válido
            runs[0].add(tablero[filaD][columnaD]);                                                                      //Agrege valor al arreglo
            sumaD += tablero[filaD][columnaD++];                                                                        //Sume el valor
        }
        runs[0].add(sumaD);                                                                                             //Se agrega la suma al arreglo.
        int bool = (columnaA + 1 < 14 && tablero[filaD][columnaA + 1] > -1) ? 1 : 0;                                    //Se determina si no es la ultima casilla de la run
        runs[0].add(bool);
        //Se determinan todos los valores en la run vertical para una casilla, además de la suma de estos valores
        while (filaA < 14 && tablero[filaA][columnaA] > 0) {                                                            //Mientras no me salga del kakuro y sea un valor válido
            runs[1].add(tablero[filaA][columnaA]);                                                                      //Agrege valor al arreglo
            sumaA += tablero[filaA++][columnaA];                                                                        //Sume el valor
        }
        runs[1].add(sumaA);                                                                                             //Se agrega la suma al arreglo.
        bool = (filaD + 1 < 14 && tablero[filaD + 1][columnaA] > -1) ? 1 : 0;                                           //Se determina si no es la ultima casilla de la run
        runs[1].add(bool);                                                                                              //Agrego el valor de la sentencia anterior
        return runs;
    }

    public void permutaciones (int casillas, int pista) {
        if (casillas != 1) {
            int[] vector = new int[casillas];
            Arrays.fill(vector, 0);
            permutacionesBT(0, vector, 0, pista, casillas);
        }
        else{
            System.out.println(pista);
        }
    }

    private void permutacionesBT(int k, int[] casillas, int suma, int pista, int tamano) {
        if (k == tamano) {
            for (int dato : casillas) {
                System.out.print("" + dato + "\t");
            }
            System.out.print('\n');
            return;
        }
        else {
            //Se exploran los valores prometedores para solución
            for (int i = 1; i < 10; i++) {
                if (!esta(casillas, i, tamano)) {
                    if (k != 0) {
                        if (casillas[k-1] < i) {
                            if (k == tamano - 1) { //Es decir, el último
                                if (suma + i == pista) {
                                    casillas[k] = i;
                                    permutacionesBT(k + 1, casillas, suma + i, pista, tamano);
                                    casillas[k] = 0;
                                }
                            } else {
                                if (suma + i < pista) {
                                    casillas[k] = i;
                                    permutacionesBT(k + 1, casillas, suma + i, pista, tamano);
                                    casillas[k] = 0;
                                }
                            }
                        }
                    }
                    else {
                        if (i < pista) {
                            casillas[k] = i;
                            permutacionesBT(k+1, casillas, suma + i, pista, tamano);
                            casillas[k] = 0;
                        }
                    }
                }
            }
        }
    }

    private boolean esta(int[] vector, int valor, int tamano) {
        int idx = 0;
        while (idx < tamano && vector[idx] < valor) {
            idx++;
        }
        return (idx < tamano && vector[idx] == valor);
    }

    private int[] getPistas(int fila, int columna) {
        int[] pista = new int[2];                                                                                       //Contenedor de las pistas
        for (int i = fila - 1; i > -1; i--) {                                                                           //Determino la pista vertical de la casilla
            if (tablero[i][columna] == -1) {                                                                            //La pista está donde haya un -1 más próximo
                pista[1] = i;                                                                                           //Guardo el índice (solo la fila, ya tengo la columna)
                break;                                                                                                  //No me interesa terminar los ciclos
            }
        }
        for (int i = columna - 1; i > -1; i--) {                                                                        //Determino la pista horizontal
            if (tablero[fila][i] == -1) {                                                                               //La pista está donde haya un -1 más próximo
                pista[0] = i;                                                                                           //Guardo el índice (solo la columna, ya tengo la fila)
                break;                                                                                                  //No me interesa terminar los ciclos
            }
        }
        return pista;
    }

    public void ingresarPista(int fila, int columna, int derecha, int abajo) {
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