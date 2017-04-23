package Interfaz;

import Logica.Kakuro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Controlador implements Initializable {

    //Atributos para los objetos graficos
    @FXML
    Pane panel;
    @FXML
    GridPane tablero;
    @FXML
    Button guardar;
    @FXML
    Button cargar;
    @FXML
    Button resolver;

    //Atributos para clase
    private Kakuro kakuro;
    private ArrayList<Group> lines;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){

        lines = new ArrayList<>();
        kakuro = new Kakuro();

        setKakuroPrueba();

        kakuro.guardarKakuro("Kakuro.dat");

        kakuro = new Kakuro();

        kakuro.cargarKakuro("Kakuro.dat");

        construirTablero();

        kakuro.permutaciones(4, 14);

        guardar.setOnAction(event -> System.out.println("Guardando..."));

        cargar.setOnAction(event -> System.out.println("Cargando..."));

        resolver.setOnAction(event -> System.out.println("Resolviendo..."));

    }

    private void construirTablero() {
        tablero.getChildren().clear();
        clearLines();
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++){
                tablero.add(construirCasilla(i, j), j, i);
            }
        }
        addLines();
    }

    private VBox construirCasilla(int fila, int columna) {
        VBox contenedor = new VBox();
        int valor = kakuro.getTablero()[fila][columna];
        switch (valor) {
            case -2:
                Label cajaNegra = new Label();
                cajaNegra.setAlignment(Pos.CENTER_RIGHT);
                cajaNegra.setStyle("-fx-background-color:#595959");
                cajaNegra.setPrefSize(42.0, 42.0);
                contenedor.getChildren().addAll(cajaNegra);
                break;
            case -1:
                Label casillaSup = new Label(kakuro.getPista()[fila][columna].getStringDerecha() + "  ");
                casillaSup.setAlignment(Pos.CENTER_RIGHT);
                casillaSup.setTextFill(Color.web("#FFFFFF"));
                casillaSup.setStyle("-fx-background-color:#595959");
                casillaSup.setPrefSize(42.0, 21.0);
                casillaSup.setWrapText(true);
                Label casillaInf = new Label("  " + kakuro.getPista()[fila][columna].getStringAbajo());
                casillaInf.setAlignment(Pos.CENTER_LEFT);
                casillaInf.setTextFill(Color.web("#FFFFFF"));
                casillaInf.setStyle("-fx-background-color:#595959");
                casillaInf.setPrefSize(42.0, 21.0);
                casillaSup.setWrapText(true);
                contenedor.getChildren().addAll(casillaSup, casillaInf);
                setLine(fila, columna);
                break;
            default:
                String numero = "";
                if (kakuro.getTablero()[fila][columna] != 0){
                    numero += String.valueOf(kakuro.getTablero()[fila][columna]);
                }
                Label casilla = new Label(numero);
                casilla.setAlignment(Pos.CENTER);
                casilla.setPrefSize(42.0, 42.0);
                casilla.setFont(new Font("Cambria Math", 20));
                contenedor.getChildren().addAll(casilla);
                contenedor.setStyle("-fx-border-color:#000000");
                break;
        }
        return contenedor;
    }

    private void setLine(int fila, int columna) {
        double inicioX = 10.0 + 6.0 + (42.0 * columna);
        double inicioY = 10.0 + 6.0 + (42.0 * fila);
        if (columna < 8)
            inicioX -= columna;
        else
            inicioX -= 8;
        if (fila >= 7)
            inicioY -= 1;
        if (fila > 7)
            inicioY -= fila%8 + 1;
        Group line = createLine(inicioX, inicioY);
        lines.add(line);
    }

    private Group createLine(double inicioX, double inicioY) {
        Line linea = new Line();
        linea.setStartX(inicioX);
        linea.setStartY(inicioY);
        linea.setEndX(inicioX + 30);
        linea.setEndY(inicioY + 30);
        linea.setStrokeWidth(1.0);
        return new Group(linea);
    }

    private void clearLines() {
        for (Group linea : lines) {
            panel.getChildren().remove(linea);
        }
    }

    private void addLines() {
        for (Group linea : lines) {
            panel.getChildren().add(linea);
        }
    }

    private void setKakuroPrueba() {

        int[][] tablero = {
                {-2, -1, -1, -2, -2, -1, -1, -2, -2, -2, -1, -1, -2, -2},
                {-1,  0,  0, -2, -1,  0,  0, -2, -2, -1,  0,  0, -2, -2},
                {-1,  0,  0, -1,  0,  0,  0, -1, -1,  0,  0,  0, -1, -2},
                {-2, -1,  0,  0,  0, -1,  0,  0, -1,  0,  0,  0,  0, -2},
                {-2, -2, -1,  0,  0,  0, -1,  0,  0, -2, -1,  0,  0, -2},
                {-2, -1, -1,  0,  0,  0, -1,  0,  0, -1,  0,  0, -1, -2},
                {-1,  0,  0,  0, -2, -1,  0,  0, -2, -1,  0,  0,  0, -2},
                {-1,  0,  0,  0, -2, -1,  0,  0, -1, -1,  0,  0,  0, -2},
                {-2, -1,  0,  0, -1,  0,  0, -1,  0,  0,  0, -2, -2, -2},
                {-1,  0,  0, -1, -1,  0,  0, -1,  0,  0,  0, -1, -2, -2},
                {-1,  0,  0,  0,  0, -1,  0,  0, -1,  0,  0,  0, -1, -2},
                {-2, -1,  0,  0,  0, -2, -1,  0,  0,  0, -1,  0,  0, -2},
                {-2, -1,  0,  0, -2, -2, -1,  0,  0, -2, -1,  0,  0, -2},
                {-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2}
        };

        tablero = kakuro.LlenarKakuro();

        kakuro.setTablero(tablero);

        /*int [][] pistas = {
                {0, 1   , 0, 4},
                {0, 2   , 0, 7},
                {0, 5   , 0, 16},
                {0, 6   , 0, 11},
                {0, 10  , 0, 14},
                {0, 11  , 0, 37},
                {1, 0   , 4, 0},
                {1, 4   , 9, 12},
                {1, 9   , 3, 12},
                {2, 0   , 3, 0},
                {2, 3   , 22, 33},
                {2, 7   , 0, 30},
                {2, 8   , 23, 0},
                {2, 12  , 0, 9},
                {3, 1   , 9, 0},
                {3, 5   , 6, 17},
                {3, 8   , 11, 13},
                {4, 2   , 20, 0},
                {4, 6   , 16, 0},
                {4, 10  , 16, 36},
                {5, 1   , 0, 8},
                {5, 2   , 13, 41},
                {5, 6   , 8, 33},
                {5, 9   , 16, 0},
                {5, 12  , 0, 16},
                {6, 0   , 9, 0},
                {6, 5   , 17, 0},
                {6, 9   , 14, 0},
                {7, 0   , 17, 0},
                {7, 5   , 10, 6},
                {7, 8   , 0, 13},
                {7, 9   , 24, 26},
                {8, 1   , 13, 13},
                {8, 4   , 7, 0},
                {8, 7   , 13, 0},
                {9, 0   , 15, 0},
                {9, 3   , 0, 20},
                {9, 4   , 11, 4},
                {9, 7   , 20, 23},
                {9, 11  , 0, 11},
                {10, 0  , 26, 0},
                {10, 5  , 17, 0},
                {10, 8  , 19, 5},
                {10, 12 , 0, 6},
                {11, 1  , 9, 0},
                {11, 6  , 17, 0},
                {11, 10 , 11, 0},
                {12, 1  , 16, 0},
                {12, 6  , 8, 0},
                {12, 10 , 4, 0},
        };
        for (int[] cuarteto : pistas) {
            kakuro.ingresarPista(cuarteto[0], cuarteto[1], cuarteto[2], cuarteto[3]);
        }

        */
    }

    /*·········································································*/


}