package Interfaz;

import Logica.HiloGrafico;
import Logica.HiloSolucionador;
import Logica.Kakuro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by   Pablo Brenes    - 2016250460
 *              Jeison Esquivel - 2013018886
 * 14 abr 2017.
 */

public class Controlador implements Initializable {

    @FXML
    Pane panel;
    @FXML
    GridPane tablero;
    @FXML
    TextArea log;
    @FXML
    Button generarForks;
    @FXML
    Button resolverForks;
    @FXML
    Button generarThreads;
    @FXML
    Button resolverThreads;
    @FXML
    Button generar;
    @FXML
    Button resolver;
    @FXML
    Button cargar;
    @FXML
    Button guardar;

    private Kakuro kakuro;
    private ArrayList<Group> lines;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){

        lines = new ArrayList<>();
        kakuro = new Kakuro();

        construirTablero();

        log.setEditable(false);

        generarForks.setDisable(true);
        generarForks.setOnAction(event -> System.out.println("Generando con forks..."));

        resolverForks.setDisable(true);
        resolverForks.setOnAction(event -> System.out.println("Resolviendo con forks..."));

        generarThreads.setDisable(true);
        generarThreads.setOnAction(event -> System.out.println("Generando con hilos..."));

        resolverThreads.setOnAction(event -> resolverThread());

        generar.setOnAction(event -> generar());

        resolver.setOnAction(event -> resolver());

        cargar.setOnAction(event -> loadKakuro());

        guardar.setOnAction(event -> saveKakuro());

    }

    private void resolverThread() {
        int cantidadHilos = inputDialog();
        if (cantidadHilos != 0){
            HiloGrafico thread = new HiloGrafico();
            thread.start();
            kakuro.resolverKakuroParalelo(cantidadHilos);
            try {
                thread.join();
                construirTablero();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    // todo ARCHIVOS DE LOG PARA DEBUGEAR MEJOR DATOS PARA PYTHON
    private void generar() {
        kakuro = new Kakuro();
        kakuro.setTablero(kakuro.LlenarKakuro());
        construirTablero();
    }

    private void resolver() {
        kakuro.resolverKakuro();
        construirTablero();
        double time = kakuro.getTotalTime()/1000;
        time /= 1000;
        //tiempo.setText("Tiempo total: \n" + time + " milisegundos");
        System.out.println(time);
        System.gc();
    }

    private void saveKakuro() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Kakuro");
        configureFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            kakuro.guardarKakuro(file.getPath());
        }
    }

    private void loadKakuro() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cargar Kakuro");
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            kakuro.cargarKakuro(file.getPath());
            construirTablero();
        }
    }

    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")  + "/Desktop"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Kakuros", "*.kak")
        );
    }

    public void construirTablero() {
        tablero.getChildren().clear();
        clearLines();
        lines = new ArrayList<>();
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
        double inicioX = 20.0 + 6.0 + (42.0 * columna);
        double inicioY = 20.0 + 6.0 + (42.0 * fila);
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

    private int inputDialog() {
        boolean flag = true;

        while (flag) {
            try {
                TextInputDialog inputDialog = new TextInputDialog();
                inputDialog.setTitle("Configurar cantidad de hilos");
                inputDialog.setHeaderText("Ingrese la cantidad de hilos:");
                Optional<String> valor = inputDialog.showAndWait();
                if (valor.isPresent()) {
                    int dato = Integer.parseInt(valor.get());
                    if (dato < 1){
                        continue;
                    }
                    return dato;
                } else {
                    flag = false;
                }
            } catch (NumberFormatException e) {
                flag = true;
            }
        }
        return 0;
    }

}