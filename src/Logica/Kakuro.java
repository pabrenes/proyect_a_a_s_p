package Logica;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Kakuro implements Serializable{

    static int[] numeros = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static int[] numeroMaximo = {9, 17, 24, 30, 35, 39, 42, 44, 45};
    static int[] numeroMinimo = {1, 3, 6, 10, 15, 21, 28, 36, 45};
    Integer[] permitidos={1,2,3,4,5,6,7,8,9};
    private   List<Integer> dominio = Arrays.asList(permitidos);
    private List<Integer> numColumn = new ArrayList<>();
    private  List<Integer> numFilas = new ArrayList<>();
    private  List<Integer> valoresDisponibles = new ArrayList<>();


    private int[][] tablero;
    private Pista[][] pista;
    private int[][] KakuroVacio = {
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2,-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
    };

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


    /*********************************************************/

    public void Imprimir(){
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                System.out.println(KakuroVacio[i][j] + " ");
            }
            System.out.println();
        }
    }


    public List numInComlumnas(int fila,int columna ){
        numColumn.clear();
        int puntero=1;
        while (KakuroVacio[fila-puntero][columna]!=-2) {
            numColumn.add(KakuroVacio[fila-puntero][columna]);
            puntero++;
        }
        puntero=1;
        while (KakuroVacio[fila+puntero][columna]!=-2) {
            numColumn.add(KakuroVacio[fila+puntero][columna]);
            puntero++;
        }
        return numColumn;
    };

    public List numInFilas(int fila,int columna ){
        numFilas.clear();
        int puntero=1;
        while (KakuroVacio[fila][columna-puntero]!=-2) {
            numFilas.add(KakuroVacio[fila][columna-puntero]);
            puntero++;
        }
        puntero=1;
        while (KakuroVacio[fila][columna+puntero]!=-2) {
            numFilas.add(KakuroVacio[fila][columna+puntero]);
            puntero++;
        }
        return numFilas;
    };


    /*
 cuanta la cantidad de casillas usadas en fila
 si es menor a 10 entonces puede ingresar mas filas
 */
    public boolean CantCasilasUsadasFila(int fila ){
        int cantidad=0;
        for (int j = 1; j < 14; j++) {
            if (KakuroVacio[fila][j]!=-2) {
                cantidad++;
            }
        }
        return (cantidad<10);
    };
      /*
    cuanta la cantidad de casillas usadas en columna
    si es menor a 10 entonces puede ingresar mas filas
    */

    public boolean CantCasilasUsadasColumna(int columna ){
        int cantidad=0;
        for (int i = 1; i < 14; i++) {
            if (KakuroVacio[i][columna]!=-2) {
                cantidad++;
            }
        }
        return (cantidad<10);
    };

    public boolean esSolucion(int fila, int columna, int valor){
        List conjuntoColumna=numInComlumnas(fila, columna);
        List conjuntoFila= numInFilas(fila, columna);

        if (CantCasilasUsadasFila(fila) && CantCasilasUsadasColumna(columna)) {

            if (!conjuntoColumna.contains(valor)&& !conjuntoFila.contains(valor))

                return true;
        }
        return false;
    };

    public List ConjuntoPrometedor(int fila, int columna){
        List<Integer> copy = new ArrayList<Integer>(dominio);
        List conjuntoFila=numInFilas(fila, columna);
        List conjuntoColumna=numInComlumnas(fila, columna);
        copy.removeAll(conjuntoColumna);
        copy.removeAll(conjuntoFila);
        return copy;
    };




    public void ProcesarSolucion(int fila, int columna, int valor){
        KakuroVacio[fila][columna]=valor;
    };


    public int[][] LlenarKakuro(){
        Random vaCasilla= new Random();                                         /*0=no va casilla/ 1 si va casilla */
        Random rnd= new Random();                                               /* valor random*/
        int valor;
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < 13; j++) {
                if (vaCasilla.nextInt(20)>2){
                    valor=rnd.nextInt(9)+1;
                    if (esSolucion(i, j, valor)){
                        ProcesarSolucion(i, j, valor);
                    }
                    else{
                        List ConjPrometedor=ConjuntoPrometedor(i,j);

                        if (!ConjPrometedor.isEmpty()) {
                            int nuevoValor= (int) ConjPrometedor.get(0);
                            ProcesarSolucion(i, j, nuevoValor);
                        }

                    }
                }
            }
        }

        return KakuroVacio;
    };


    public void generarCuartetosHorizontales(){
        int posicion=0;
        int suma=0;
        int columna=1;
        for (int i = 1; i < 13; i++) {
            while (columna<14){
                if (KakuroVacio[i][columna]>0 )
                    suma+=KakuroVacio[i][columna];
                else {
                    if (KakuroVacio[i][columna-1]!=-2 ){
                    KakuroVacio[i][posicion] = -1;
                    getPista()[i][posicion].setDerecha(suma);
                    posicion = columna;
                    suma = 0;
                }else posicion=columna;
                }

                columna++;
            }
            getPista()[i][posicion].setDerecha(suma);
            posicion=0;
            columna=1;
        }
    };



    public void generarCuartetosVerticales(){
        int posicion=0;
        int suma=0;
        int fila=1;
        for (int j = 1; j < 14; j++) {
            while (fila<14){

                if (KakuroVacio[fila][j]>0 )
                    suma+=KakuroVacio[fila][j];

                else {
                    if (KakuroVacio[fila-1][j]>0 ){
                        KakuroVacio[posicion][j] = -1;

                        ingresarPista(posicion,j,getPista()[posicion][j].getDerecha(), suma);
                        posicion = fila;
                        suma = 0;
                    }else posicion=fila;
                }

                fila++;
            }
            ingresarPista(posicion,j,getPista()[posicion][j].getDerecha(),suma);
            posicion=0;
            fila=1;
        }
    };




}
