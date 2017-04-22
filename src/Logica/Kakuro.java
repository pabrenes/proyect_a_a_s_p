package Logica;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

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
        System.out.println("------------------------");
        for (int value : values) {
            System.out.println(value);
        }
        System.out.println("------------------------");
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
        System.out.println(min);
        for (int i = 0; i < min; i++) {                                                                                 //Solo puedo colocar números menores al mínimo (ves static array numeros)
            //if (!(min - numeros[i] == numeros[i]))     REVISAR, EL MIN TIENE YA -1, POR LO QUE NO ES EL VALOR REAL    //Si colocar un valor hará que deba colocar el mismo después, ignorelo
                values.add(numeros[i]);                                                                                 //Agrega el valor
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
