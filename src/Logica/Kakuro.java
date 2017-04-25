package Logica;

import java.io.*;
import java.util.*;
import java.util.ArrayList;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison  Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Kakuro implements Serializable {

    private int[][] tablero;
    private Pista[][] pista;
    private ArrayList<int[]> casillas;
    private int totalCasillas;
    private boolean solucion;
    private long totalTime, timeEnd, timeStart;
    private ArrayList<Long> tiempos;
    private ArrayList<Integer> casillasColocadas;
    /**
    Pablo de aquí para arriba declara sus variables
        ---------------+---------------
                 ___ /^^[___              _
               /|^+----+   |#___________//
             ( -+ |____|   _______-----+/
              ==_________--'            \
                ~_|___|__~
    Jeison de aquí para abajo declara sus variables
    */
    Integer[] permitidos={1,2,3,4,5,6,7,8,9};
    private List<Integer> dominio = Arrays.asList(permitidos);
    private List<Integer> numColumn = new ArrayList<>();
    private  List<Integer> numFilas = new ArrayList<>();
    private  List<Integer> valoresDisponibles = new ArrayList<>();
    private int[][] KakuroVacio = {
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
        {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
        {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
    };

    /**
     * Builder para la clase kakuro
     * Únicamente inicializa el espacio para las variables
     */
    public Kakuro() {
        tablero = new int[14][14];
        pista = new Pista[14][14];
    }

    /**
     * Función que inicia la recursión del BT para generar una solución a un kakuro
     * Toma también el tiempo resultado de encontrar una solución
     * También inicia variables requeridas para el BT
     */
    public void resolverKakuro() {
        clearKakuro();
        timeStart = System.nanoTime();
        casillas = obtenerCasillas();                                                                                   //Obtener casillas recorrer el kakuro encontrando las casillas a rellenar
        totalCasillas = casillas.size();                                                                                //El tamaño indica cuando encontré mi solución
        tiempos = new ArrayList<>();
        casillasColocadas = new ArrayList<>();
        solucion = false;
        resolverKakuroBT(0);
        totalTime = timeEnd - timeStart;
        /*
        for (int i = 0; i < tiempos.size(); i++) {
            double time = (tiempos.get(i) - timeStart)/100;
            time /= 100;
            time /= 100;
            System.out.println("Colocadas " + casillasColocadas.get(i) + " casillas a los: " + time + " milisegundos");
        }
        */
        System.out.print("[");
        for (int k : casillasColocadas ) {
            System.out.print(k + ", ");
        }
        System.out.print("]");
        System.out.println();
        System.out.print("[");
        for (Long time : tiempos ) {
            double mls = (time - timeStart) / 100;
            mls /= 100;
            mls /= 100;
            System.out.print(mls + ", ");
        }
        System.out.print("]");
        System.out.println();
    }

    /**
     * Función encargada de crear el primer hilo para crear una solución paralelizada por hilos
     */
    public void resolverKakuroParalelo(int topeHilos) {
        clearKakuro();
        HiloSolucionador.clearHilo();
        casillas = obtenerCasillas();                                                                                   //Obtener casillas recorrer el kakuro encontrando las casillas a rellenar
        totalCasillas = casillas.size();                                                                                //El tamaño indica cuando encontré mi solución
        HiloSolucionador hilo = new HiloSolucionador(tablero, 0);
        HiloSolucionador.setKakuro(this);
        HiloSolucionador.setTopeHilos(topeHilos);
        hilo.setTimeStart(System.nanoTime());
        hilo.start();
    }

    /**
     * Función que resuelve un kakuro con metodología BT
     * La Función de poda se encuentra en otro método, aquí solo se llama para obtener los posibles sucesores
     * @param k int para avanzar a la siguiente solución prometedora
     */
    private void resolverKakuroBT(int k) {
        if (k == totalCasillas) {                                                                                       //Solución encontrada
            solucion = true;
            timeEnd = System.nanoTime();
        }
        else {                                                                                                          //Aún no hay solución
            int[] parOrdenado = casillas.get(k);                                                                        //Obtengo los pares ordenados a trabajar según el k
            HashSet<Integer> sucesores = obtenerSucesores(parOrdenado[0], parOrdenado[1], tablero);                     //Obtengo los sucesores para esa casilla
            //Una desición erronea tomada anteriormente genera que no encuentre valores posibles para que el kakuro obtenga una solución
            //Sucesores puede ser generada de manera vacía, lo que indica que atrás hubo un valor no correcto
            //Por lo que simplemente no intenta realizar nuevas conmparaciones y regresa para enmendar el error
            for (Integer sucesor : sucesores) {                                                                         //Para cada sucesor explore los siguientes sucessores
                tablero[parOrdenado[0]][parOrdenado[1]] = sucesor;                                                      //Agregue el posible valor al tablero
                casillasColocadas.add(k + 1);
                tiempos.add(System.nanoTime());
                resolverKakuroBT(k + 1);                                                                             //Explore según el posible valor anterior
                if (solucion)                                                                                           //Si ya se encontró solución, corte
                    break;
                tablero[parOrdenado[0]][parOrdenado[1]] = 0;                                                            //Desaga el cambio en caso de no valor
            }
        }
    }

    /**
     * Función que retorna un ArrayList con todos los pares ordenados para cada casilla
     * @return retorna un ArrayList de int[2]
     */
    private ArrayList<int[]> obtenerCasillas() {
        ArrayList<int[]> paresOrdenados = new ArrayList<>();                                                            //Guardaré todas las posiciones [i, j] donde haya una casilla por llenar
        int[] parOrdenado;                                                                                              //Variable temporal para almacenar un par ordenado
        for (int i = 1; i < 14; i++) {
            for (int j = 1; j < 14; j++) {
                if (tablero[i][j] > -1) {                                                                               //Si se cumple que estoy en una casilla por llenar
                    parOrdenado = new int[2];                                                                           //Creo un nuevo array de 2
                    parOrdenado[0] = i;                                                                                 //Guardo el i
                    parOrdenado[1] = j;                                                                                 //Guardo el j
                    paresOrdenados.add(parOrdenado);                                                                    //Guardo el par ordenado
                }
            }
        }
        return paresOrdenados;
    }

    /**
     * Función encargada de obtener los posibles valores para colocar en una casilla del kakuro en base a toda la
     * información que interfiera en esta casilla
     * @param fila Fila donde se encuentra la casilla
     * @param columna Columna donde se encuentra la columna
     * @return se retorna un SetHash con los valores que pueden ser colocados en esa casilla
     */
    HashSet<Integer> obtenerSucesores(int fila, int columna, int[][] tablero) {
        HashSet<Integer> values;

        int[] pistas = getPistas(fila, columna, tablero);                                                                        //Obtengo la posición de las pistas en el tablero
        int pHoriz, pVerti;                                                                                             //Pista horizontal y vertical de la casilla
        int totalD, totalA;                                                                                             //Total de casillas en la run horizontal y vertical

        pHoriz = pista[fila][pistas[0]].getDerecha();                                                                   //Obtengo la pista horizontal
        pVerti = pista[pistas[1]][columna].getAbajo();                                                                  //Obtengo la pista vertical

        ArrayList<Integer>[] runs = getRuns(fila, pistas[0] + 1, pistas[1] + 1, columna, tablero);               //Obtengo los datos de las runs (ver doc de la función)

        //Se usa la cantidad de casillas para calcular permutaciones disponibles para cierta pista en esa cantidad de casillas
        //Este dato es variable, cuando se avanza a una casilla dependiente de una run, se trata como si fuera otra run totalmente distinta
        //Por ejemplo considere una run de 4 casillas, si coloqué un valor en en la primera, entonces la segunda se trata como si solo fuera de 3 casillas.
        totalD = runs[0].remove(runs[0].size() - 1);                                                             //Determino la nueva pista horizontal
        totalA = runs[1].remove(runs[1].size() - 1);                                                             //Determino la nueva pista horizontal

        //Para la poda se crean "Nuevas pistas" para hacer el árbol de soluciones más pequeño.
        //La nueva pista consiste en tomar la pista de la run original y restarle la suma de los valores ya puestos en la run
        //Por ende toda casilla termina con una pista totalmente diferente a cualquier otra casilla
        pHoriz -= runs[0].remove(runs[0].size() - 1);                                                             //Determino la nueva pista horizontal
        pVerti -= runs[1].remove(runs[1].size() - 1);                                                             //Determino la nueva pista vertical

        //Esta función crea los valores con los que se puede formar una pista con cierta cantidad de casillas
        //Puede notar que la cantidad de casillas se manipula, para recrear el ejemplo de que el total de casillas es variable antes mencionado
        values = new HashSet<>(permutaciones((totalD - (columna - pistas[0])) + 1, pHoriz));                    //Obtengo los posibles valores para formar una pista en tantas casillas
        values.retainAll(permutaciones((totalA - (fila - pistas[1])) + 1, pVerti));                             //Obtengo los posibles valores para formar una pista en tantas casillas

        //Entre los datos que se obtienen de las runs son los valores ya colocados en la fila y columna, se remueven todos de la posible solución
        //Ya que no pueden repetirse
        values.removeAll(runs[0]);                                                                                      //Elimino repetidos horizontalmente
        values.removeAll(runs[1]);                                                                                      //Elimino repetidos verticalmente

        return values;
    }

    /**
     * Función que determina los datos de dos runs para una casilla dada, retorna un arreglo de dos espacios de arreglos
     * dinámicos con la siguiente información:
     *      • Todos los valores de las casillas en orden de la pista de la run deseada hasta el final de la run
     *      • La suma de todos los valores anteriormente dado
     *      • La cantidad de casillas que fueron leídas (tamaño de la run)
     *  Es importante recordar:
     *      • Siempre el último elemento del array es la cantidad de casillas
     *      • Siempre el penúltimo elemento del array es la suma de todos los valores en la run
     * @param filaD int que corresponde a la fila en la cual se encuentra la pista horizontal para la run
     * @param columnaD int que corresponde a la columna en la cual se encuentra la pista horizontal para la run
     * @param filaA int que corresponde a la fila en la cual se encuentra la pista vertical para la run
     * @param columnaA int que corresponde a la columna en la cual se encuentra la pista vertical para la run
     * @return Se retorna un arreglo de tamaño dos con dos arreglos de tamaño dinámico (especificados antes)
     */
    private ArrayList<Integer>[] getRuns(int filaD, int columnaD, int filaA, int columnaA, int[][] tablero) {
        ArrayList<Integer>[] runs = new ArrayList[2];                                                                   //Contenedor para los valores en las runs
        int sumaD = 0, sumaA = 0, totalD = 0, totalA = 0;                                                               //Control para la suma y casillas para la run
        runs[0] = new ArrayList<>();
        runs[1] = new ArrayList<>();
        //Se determinan todos los valores en la run horizontal para una casilla, además de la suma de estos valores
        while (columnaD < 14 && tablero[filaD][columnaD] > -1) {                                                        //Mientras no me salga del kakuro y sea un valor de la run
            runs[0].add(tablero[filaD][columnaD]);                                                                      //Agrege valor al set
            sumaD += tablero[filaD][columnaD++];                                                                        //Sume el valor
            totalD++;                                                                                                   //Hay un valor más
        }
        runs[0].add(sumaD);                                                                                             //Se agrega la suma actual
        runs[0].add(totalD);                                                                                            //Se agrega la cantidad de casillas
        //Se determinan todos los valores en la run vertical para una casilla, además de la suma de estos valores
        while (filaA < 14 && tablero[filaA][columnaA] > -1) {                                                           //Mientras no me salga del kakuro y sea un valor de la run
            runs[1].add(tablero[filaA][columnaA]);                                                                      //Agrege valor al set
            sumaA += tablero[filaA++][columnaA];                                                                        //Sume el valor
            totalA++;
        }
        runs[1].add(sumaA);                                                                                             //Se agrega la suma actual
        runs[1].add(totalA);                                                                                            //Se agrega la cantidad de casillas
        return runs;
    }

    /**
     * Función que dado dos ints, una fila y una columna correspondientes a una casilla del kakuro, retornará otro par
     * de tal manera que la fila de parámetro más la columna que se retorna formen la pista horizontal para la casilla
     * de igual manera en base a la columna retorna la fila de manera que se construya la pista vertical para la casilla
     * @param fila Fila en la que se encuentra la casilla (empieza en 0)
     * @param columna Columna donde se encuentra la casilla (empieza en 0)
     * @return se retrona un vector de dos ints [columna, fila], para formar dos pares nuevos con los parámetros.
     */
    private int[] getPistas(int fila, int columna, int[][] tablero) {

        int[] pista = new int[4];                                                                                       //Contenedor de las pistas

        for (int i = columna - 1; i > -1; i--) {                                                                        //Determino la pista horizontal
            if (tablero[fila][i] == -1) {                                                                               //La pista está donde haya un -1 más próximo
                pista[0] = i;                                                                                           //Guardo el índice (solo la columna, ya tengo la fila)
                break;                                                                                                  //No me interesa terminar los ciclos
            }
        }

        for (int i = fila - 1; i > -1; i--) {                                                                           //Determino la pista vertical de la casilla
            if (tablero[i][columna] == -1) {                                                                            //La pista está donde haya un -1 más próximo
                pista[1] = i;                                                                                           //Guardo el índice (solo la fila, ya tengo la columna)
                break;                                                                                                  //No me interesa terminar los ciclos
            }
        }

        return pista;
    }

    /**
     *  Función encargada de llamar a una función recursiva con valores iniciales, además bloquea el caso base y lo
     * retorna directo desde está función (Se evita una llamada recursiva innecesaria).
     *  Se inicializa un vector vacio con el tamaño para que el algoritmo real de permutaciones dado por Backtracking
     * pueda trabajar.
     * @param casillas Tamaño total de casillas que se utilizan para formar la pista dada
     * @param pista La suma que debe formar los numeros puestos en cada casilla sin repetirse
     */
    private HashSet<Integer> permutaciones(int casillas, int pista) {
        HashSet<Integer> valores = new HashSet<>();
        if (casillas != 1) {
            int[] vector = new int[casillas];
            Arrays.fill(vector, 0);
            permutacionesBT(0, vector, 0, pista, casillas, valores);
        } else {
            valores.add(pista);
        }
        return valores;
    }

    /**
     * Función para encontrar permutaciones para una pista dada y la cantidad de casillas implementada con backtracking
     * Trabaja sobre un mismo vector, reasignando valores para evitar guardar estados posterirores al vector
     * Determina todas las posibles soluciones sin contar equivalentes por ejemplo (3-2-1 = 1-2-3 = 2-1-3)
     * @param k Valor indicativo de en cual índice del vector estoy trabajando, además de ser determinante de solución
     * @param casillas Vector sobre cual el algoritmo trabaja tiene el tamaño de las casillas requeridas
     * @param suma La suma que se lleva en el momento con los valores para no estar recorriendo el vector en cada caso
     * @param pista Valor que deben sumar en total todas las casillas
     * @param tamano Se guarda el tamaño del vector para no recalcularlo
     * @param valores HashSet para guardar los valores disponibles para crear una pista (hashSet para evitar duplicados)
     */
    private void permutacionesBT(int k, int[] casillas, int suma, int pista, int tamano, HashSet<Integer> valores) {
        if (k == tamano) {                                                                                              //Si el k es igual al tamano (el idx del array se salio) ya es solución
            for (int dato : casillas) {                                                                                 //Se almacenan los valores
                valores.add(dato);                                                                                      //Mi poda garantiza que en este punto es solución SIEMPRE
            }
        } else {
            //Se exploran los valores prometedores para solución (Se poda)
            for (int i = 1; i < 10; i++) {
                if (!esta(casillas, i, tamano)) {                                                                       //El valor a fuerza no puede estar ya colocado en el vector
                    //A partir de aquí se divide en dos casos, es la casilla inicial o no lo es
                    if (k != 0) {                                                                                       //Si no es la casilla inicial proceda
                        //Aquí se toma la desición de siempre tener un orden ascedente para evitar generar mismas combinaciones permutadas de diferente manera SIEMPRE debe cumplirse
                        if (casillas[k - 1] < i) {
                            //Aquí redivido en dos posibles casos, que sea la última casilla o no lo sea
                            if (k == tamano - 1) {                                                                      //Es la última casilla
                                //Aquí otra anotación importante es que en la misma poda se decide si es solución para ser aceptado el i elegido debe sumar exactamente la suma requerida
                                if (suma + i == pista) {                                                                //Solo si el valor ya completa una solución
                                    casillas[k] = i;                                                                    //Se asigna el valor
                                    permutacionesBT(k + 1, casillas, suma + i, pista, tamano, valores);                //El algoritmo ahora trabajo con el nuevo valor propuesto
                                    casillas[k] = 0;                                                                    //Cuando regrese y quiera probar otro, se limpia (se evita guardar los estados)
                                }
                            } else {                                                                                    //No era la casilla final
                                if (suma + i < pista) {                                                                 //Sirve cualquier valor mientras no sobrepase la suma requerida
                                    casillas[k] = i;                                                                    //Se asigna el valor
                                    permutacionesBT(k + 1, casillas, suma + i, pista, tamano, valores);                //El algoritmo ahora trabajo con el nuevo valor propuesto
                                    casillas[k] = 0;                                                                    //Cuando regrese y quiera probar otro, se limpia (se evita guardar los estados)
                                }
                            }
                        }
                    } else {                                                                                            //Es la casilla inicial
                        if (i < pista) {                                                                                //Si es la primera casilla solo necesito que el i no sobrepase la suma requerida
                            casillas[k] = i;                                                                            //Se asigna el valor
                            permutacionesBT(k + 1, casillas, suma + i, pista, tamano, valores);                        //El algoritmo ahora trabajo con el nuevo valor propuesto
                            casillas[k] = 0;                                                                            //Cuando regrese y quiera probar otro, se limpia (se evita guardar los estados)
                        }
                    }
                }
            }
        }
    }

    /**
     * Función que determina si un valor se encuentra en arreglo ordenado ascendemente
     * @param vector Vector en el que se desea buscar (debe estar ordenado de menor a mayor)
     * @param valor Valor que se va a buscar dentro del arreglo
     * @param tamano Tamaño del vector para evitar recorrer el arreglo una vez solo para esto
     * @return Se retorna un valor booleano que indica si el valor estaba o no (Si estaba = true, no estada = false)
     */
    private boolean esta(int[] vector, int valor, int tamano) {
        int idx = 0;                                                                                                    //Necesito un índice para moverme sobre el arreglo
        while (idx < tamano && vector[idx] < valor) {                                                                   //Mientras el índice no se desborde y el valor actual no sea mayor al que busco
            idx++;                                                                                                      //Desplazece
        } //Al arreglo estar ordenado me garantiza que si encuentro un valor mayor, exista la posibilidad de que mi indice actual apunte al valor, sino nunca existio
        return (idx < tamano && vector[idx] == valor); //Se retorna directamente si el valor quedó en mi último índice o no
    }

    /**
     * Dado un cuarteto ingresa una pista al tablero de pistas
     * @param fila La fila correspondiente a la pista a ingresar
     * @param columna La columna correspondiente a la pista a ingresar
     * @param derecha el determinante de la fila horizontal
     * @param abajo el determinante de la fila vertical
     */
    public void ingresarPista(int fila, int columna, int derecha, int abajo) {
        pista[fila][columna] = new Pista(derecha, abajo);
    }

    /**
     * Obtener el tablero lógico
     * @return se retorna el tablero lógico del kakuro
     */
    public int[][] getTablero() {
        return tablero;
    }

    /**
     * Obtener el tablero de pistas
     * @return se retorna el tablero de pistas
     */
    public Pista[][] getPista() {
        return pista;
    }

    /**
     * Dado un tablero int[][] lo asigna como tablero lógico
     * @param tablero int[][] tablero para asignar
     */
    public void setTablero(int[][] tablero) {
        this.tablero = tablero;
    }

    /**
     * Dado un tablero de pistas Pista[][] lo asigna como el actual
     * @param pista Pista[][] que será asignado como el nuevo tablero de pistas
     */
    public void setPista(Pista[][] pista) {
        this.pista = pista;
    }

    /**
     * Función que guarda un .dat con el kakuro en un parámetro dado
     * @param path String con el path completo para guardar un kakuro
     */
    public void guardarKakuro(String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            clearKakuro();
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dado un path carga el .dat con un kakuro y lo asigna a esta instancia del kakuro
     * @param path String como el path completo de donde se debe cargar un kakuro
     */
    public void cargarKakuro(String path) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            Object aux = ois.readObject();
            if (aux instanceof Kakuro) {
                this.setTablero(((Kakuro) aux).getTablero());
                this.setPista(((Kakuro) aux).getPista());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtener el tiempo de ejecución
     * @return retorna un long con el tiempo en milisegundos total de ejecución
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * Función para obtener las casillas a llenar
     * @return retorna el arreglo con las casillas a llenar
     */
    ArrayList<int[]> getCasillas() {
        return casillas;
    }

    /**
     * Función para obtener el total de casillas
     * @return retorna el int con el tamaño total de casillas a llenar
     */
    int getTotalCasillas() {
        return totalCasillas;
    }

    /**
     * Método que limpia el tablero y reinicia valores relacionados para la solución del kakuro
     */
    public void clearKakuro() {
        clearTablero();
        casillas = new ArrayList<>();
        totalCasillas = 0;
        solucion = false;
        totalTime = timeEnd = timeStart = 0;
        tiempos = new ArrayList<>();
        casillasColocadas = new ArrayList<>();
    }

    /**
     * Método que coloca en 0 de nuevo las casillas por resolver
     */
    private void clearTablero() {
        for (int i = 1; i < 14; i++) {
            for (int j = 1; j < 14; j++) {
                if (tablero[i][j] > -1)
                    tablero[i][j] = 0;
            }
        }
    }


    /**
    //                          *
    //                         * *
    //                        *   *
    //                       *     *
    //                      *       *
    //                     *         *
    //                    *****   *****
    //                        *   *
    //                        *   *
    //                        *   *
    //                        *   *
    //                        *   *
    //                        *   *
    //                        *****
    //----------------------------------------------------------//
    //                    PABLO  WORKSPACE                      //
    //----------------------------------------------------------//
    //               .ed$$$$$eec.
    //           .e$$$$$$$$$$$$$$$$$$ééééé$$$$$c
    //          d$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$c
    //        .$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$b.
    //       $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ $b
    //      d$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$F
    //     .$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //     $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //    .$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$**$ ^$$$$
    //    4 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*"     4$$F
    //    4 '$$$$$$$$$$$$$$$$$$$$$$$$$$$"        4$$
    //    4  $$$$$$$$$$$$$$$$$$$$$$$$$$$        .$$%
    //    d   $$$$$          $$$$$*$$$$$$c   ..e$$"
    //    -   4$$$$          ^$$$$  *$$$$$F  ^"""
    //        4$$$$          4$$$$ z$$$$$"
    //        4$$$$          4$$$$ ^$$$P
    //        ^$$$$b         '$$$$e  "F
    //----------------------------------------------------------//
    //                    JEISON WORKSPACE                      //
    //----------------------------------------------------------//
    //                        *****
    //                        *   *
    //                        *   *
    //                        *   *
    //                        *   *
    //                        *   *
    //                        *   *
    //                    *****   *****
    //                     *         *
    //                      *       *
    //                       *     *
    //                        *   *
    //                         * *
    //                          *
    */

    public void Imprimir() {
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                System.out.println(KakuroVacio[i][j] + " ");
            }
            System.out.println();
        }
    }


    public List numInComlumnas(int fila, int columna) {
        numColumn.clear();
        int puntero = 1;
        while (KakuroVacio[fila - puntero][columna] != -2) {
            numColumn.add(KakuroVacio[fila - puntero][columna]);
            puntero++;
        }
        puntero = 1;
        while (KakuroVacio[fila + puntero][columna] != -2) {
            numColumn.add(KakuroVacio[fila + puntero][columna]);
            puntero++;
        }
        return numColumn;
    }

    ;

    public List numInFilas(int fila, int columna) {
        numFilas.clear();
        int puntero = 1;
        while (KakuroVacio[fila][columna - puntero] != -2) {
            numFilas.add(KakuroVacio[fila][columna - puntero]);
            puntero++;
        }
        puntero = 1;
        while (KakuroVacio[fila][columna + puntero] != -2) {
            numFilas.add(KakuroVacio[fila][columna + puntero]);
            puntero++;
        }
        return numFilas;
    }

    ;


    /*
 cuanta la cantidad de casillas usadas en fila
 si es menor a 10 entonces puede ingresar mas filas
 */
    public boolean CantCasilasUsadasFila(int fila) {
        int cantidad = 0;
        for (int j = 1; j < 14; j++) {
            if (KakuroVacio[fila][j] != -2) {
                cantidad++;
            }
        }
        return (cantidad < 10);
    }

    ;
      /*
    cuanta la cantidad de casillas usadas en columna
    si es menor a 10 entonces puede ingresar mas filas
    */

    public boolean CantCasilasUsadasColumna(int columna) {
        int cantidad = 0;
        for (int i = 1; i < 14; i++) {
            if (KakuroVacio[i][columna] != -2) {
                cantidad++;
            }
        }
        return (cantidad < 10);
    }

    ;

    public boolean esSolucion(int fila, int columna, int valor) {
        List conjuntoColumna = numInComlumnas(fila, columna);
        List conjuntoFila = numInFilas(fila, columna);

        if (CantCasilasUsadasFila(fila) && CantCasilasUsadasColumna(columna)) {

            if (!conjuntoColumna.contains(valor) && !conjuntoFila.contains(valor))

                return true;
        }
        return false;
    }

    ;

    public List ConjuntoPrometedor(int fila, int columna) {
        List<Integer> copy = new ArrayList<Integer>(dominio);
        List conjuntoFila = numInFilas(fila, columna);
        List conjuntoColumna = numInComlumnas(fila, columna);
        copy.removeAll(conjuntoColumna);
        copy.removeAll(conjuntoFila);
        return copy;
    }

    ;


    public void ProcesarSolucion(int fila, int columna, int valor) {
        KakuroVacio[fila][columna] = valor;
    }

    ;


    public int[][] LlenarKakuro() {
        Random vaCasilla = new Random();                                         /*0=no va casilla/ 1 si va casilla */
        Random rnd = new Random();                                               /* valor random*/
        int valor;
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < 13; j++) {
                if (vaCasilla.nextInt(20) > 2) {
                    valor = rnd.nextInt(9) + 1;
                    if (esSolucion(i, j, valor)) {
                        ProcesarSolucion(i, j, valor);
                    } else {
                        List ConjPrometedor = ConjuntoPrometedor(i, j);

                        if (!ConjPrometedor.isEmpty()) {
                            int nuevoValor = (int) ConjPrometedor.get(0);
                            ProcesarSolucion(i, j, nuevoValor);
                        }

                    }
                }
            }
        }

        return KakuroVacio;
    }

    ;


    public void generarCuartetosHorizontales() {
        int posicion = 0;
        int suma = 0;
        int columna = 1;
        for (int i = 1; i < 13; i++) {
            while (columna < 14) {
                if (KakuroVacio[i][columna] > 0)
                    suma += KakuroVacio[i][columna];
                else {
                    if (KakuroVacio[i][columna - 1] != -2) {
                        KakuroVacio[i][posicion] = -1;

                        posicion = columna;
                        suma = 0;
                    } else posicion = columna;
                }

                columna++;
            }

            posicion = 0;
            columna = 1;
        }
    }

    ;


    public void generarCuartetosVerticales() {
        int posicion = 0;
        int suma = 0;
        int fila = 1;
        for (int j = 1; j < 14; j++) {
            while (fila < 14) {

                if (KakuroVacio[fila][j] > 0)
                    suma += KakuroVacio[fila][j];

                else {
                    if (KakuroVacio[fila - 1][j] > 0) {
                        KakuroVacio[posicion][j] = -1;

                        ingresarPista(posicion, j, getPista()[posicion][j].getDerecha(), suma);
                        posicion = fila;
                        suma = 0;
                    } else posicion = fila;
                }

                fila++;
            }
            ingresarPista(posicion, j, getPista()[posicion][j].getDerecha(), suma);
            posicion = 0;
            fila = 1;
        }
    }



};