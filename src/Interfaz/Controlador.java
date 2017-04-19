package Interfaz;

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
 * Created by Pablo Brenes - 2016250460
 * 11 abr 2017.
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
    int[][] tableroLogico = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 7, 7, 8, 9, 2, 6, 5, 1, 1, 1},
            {0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0},
            {0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){

        construirTablero();

        guardar.setOnAction(event -> {
           System.out.println("Guardando...");
        });

        cargar.setOnAction(event -> {
            System.out.println("Cargando...");
        });

        resolver.setOnAction(event -> {
            System.out.println("Resolviendo...");
        });

    }

    private void construirTablero() {
        tablero.getChildren().clear();
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++){
                tablero.add(construirCasilla(i, j), j, i);
            }
        }
    }

    private VBox construirCasilla(int fila, int columna) {
        VBox contenedor = new VBox();
        contenedor.setStyle("-fx-border-color:#000000");
        if (tableroLogico[fila][columna] == 0) {
            Label casillaSup = new Label(String.valueOf(fila) + "  ");
            casillaSup.setAlignment(Pos.CENTER_RIGHT);
            casillaSup.setTextFill(Color.web("#FFFFFF"));
            casillaSup.setStyle("-fx-background-color:#434343");
            casillaSup.setPrefSize(42.0, 21.0);
            casillaSup.setWrapText(true);
            Label casillaInf = new Label("  " + String.valueOf(columna));
            casillaInf.setAlignment(Pos.CENTER_LEFT);
            casillaInf.setTextFill(Color.web("#FFFFFF"));
            casillaInf.setStyle("-fx-background-color:#434343");
            casillaInf.setPrefSize(42.0, 21.0);
            casillaSup.setWrapText(true);
            contenedor.getChildren().addAll(casillaSup, casillaInf);
            return contenedor;
        }
        else {
            Label casilla = new Label(String.valueOf(tableroLogico[fila][columna]));
            casilla.setAlignment(Pos.CENTER);
            casilla.setPrefSize(42.0, 42.0);
            casilla.setFont(new Font("Cambria Math", 20));
            contenedor.getChildren().addAll(casilla);
            return contenedor;
        }
    }

    private void setLine (int fila, int columna) {
        Line line = createLine(13, 8);
        tablero.add(line, columna, fila);
    }

    private Line createLine (double inicioX, double inicioY) {
        Line linea = new Line();
        linea.setStartX(inicioX);
        linea.setStartY(inicioY);
        linea.setEndX(inicioX + 30);
        linea.setEndY(inicioY + 35);
        linea.setStrokeWidth(1.0);
        return linea;
    }
}