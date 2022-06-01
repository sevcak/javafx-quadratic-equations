import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class QuadraticEquationsApp extends Application {
    TextField tfA, tfB, tfC;
    Label lbX1, lbX2, lbD, lbEquation;
    Graph graph;
    
    public void start(Stage stage) throws Exception{
        //UI build
        BorderPane bpMain = new BorderPane();
        bpMain.getStyleClass().add("bpMain");
        
        //top panel------------------------------------------------
        Label lbHead = new Label("Quadratic Equations");
        lbHead.setMinHeight(60);
        //css classes
        lbHead.getStyleClass().addAll("header", "pane");
        //apply
        bpMain.setTop(lbHead);

        //left panel-----------------------------------------------
        Button btCalculate = new Button("Calculate");
        btCalculate.setOnAction(new ClickCalculate());
        
        tfA = new TextField("1");
        tfB = new TextField("6");
        tfC = new TextField("5");
        lbEquation = new Label("ax\u00B2+bx+c=0");
        
        lbX1 = new Label("");
        lbX2 = new Label("");
        lbD = new Label("");

        GridPane gpLpFields = new GridPane();
        gpLpFields.add(new Label("a:"), 0, 0); gpLpFields.add(tfA, 1, 0);
        gpLpFields.add(new Label("b:"), 0, 1); gpLpFields.add(tfB, 1, 1);
        gpLpFields.add(new Label("c:"), 0, 2); gpLpFields.add(tfC, 1, 2);
        gpLpFields.add(new Label("x\u2081:"), 0, 3); gpLpFields.add(lbX1, 1, 3);
        gpLpFields.add(new Label("x\u2082:"), 0, 4); gpLpFields.add(lbX2, 1, 4);
        gpLpFields.add(new Label("D:"), 0, 5); gpLpFields.add(lbD, 1, 5);
        
        VBox vbLeftPanel = new VBox(gpLpFields, lbEquation, btCalculate);
        
        //css classes
        vbLeftPanel.getStyleClass().addAll("left-panel", "pane");
        
        //apply
        bpMain.setLeft(vbLeftPanel);

        //main panel----------------------------------------------------------
        graph = new Graph();
        
        //css classes
        graph.getStyleClass().addAll("pane", "main-panel");
        
        //apply
        bpMain.setCenter(graph);

        //scene setup----------------------------------------------
        Scene scene = new Scene(bpMain, 800, 400);
        
        
        //apply styles
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Quadratic Equations");
        stage.setMinWidth(520);
        stage.setMinHeight(420);
        stage.show();
        
        graph.drawGrid();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    //throws an alert with specified text
    public void throwAlert(String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.setGraphic(new ImageView(new Image("images/iconWarning01.png")));

        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("images/iconWarning02.png"));
        
        stage.showAndWait();
    }

    //handles clicking on the "Calculate" button, counts equation and draws a graphGrid
    public class ClickCalculate implements EventHandler<ActionEvent>{
        private DecimalFormat df = new DecimalFormat("##.##");
        private double a, b, c;
        private double vysledok[];

        public void handle(ActionEvent event){
            try{ //tries to read values from textfields
                a = Double.parseDouble(tfA.getText());
                b = Double.parseDouble(tfB.getText());
                c = Double.parseDouble(tfC.getText());
            }catch(Exception e){ //if one or more values aren't numbers
                throwAlert("Invalid input!", "Enter numbers in all fields please.");
                return;
            }

            vysledok = QuadraticEquation.qEquation(a, b, c);
            
            if(vysledok == null){
                throwAlert("Error!", "Discriminant is smaller than 0.\nRoots are complex.");
                return;
            }else{
                if(vysledok.length == 1){
                    lbD.setText("");
                    lbX1.setText(""+ df.format(vysledok[0]));
                    lbX2.setText("");
                }else if(vysledok.length == 2){ //if there's one root
                    lbD.setText("" + df.format(vysledok[0]));
                    lbX1.setText("" + df.format(vysledok[1]));
                    lbX2.setText("");
                }else if(vysledok.length == 3){ //if there's two roots
                    lbD.setText("" + df.format(vysledok[0]));
                    lbX1.setText("" + df.format(vysledok[1]));
                    lbX2.setText("" + df.format(vysledok[2]));
                }

                lbEquation.setText(a + "x\u00B2+" + b + "x+" + c + "=0");
            }
            
            //redraws graph
            graph.pathGraph.drawGraph();
        }
    }

    public class CoordinateGridPane extends Pane{
        NumberAxis axisX;
        NumberAxis axisY;
        CoordinateGrid grid;

        //creates a coordinate grid that changes relatively to size of its window
        public CoordinateGridPane(){
            Rectangle paneClip = new Rectangle();
            paneClip.widthProperty().bind(widthProperty());
            paneClip.heightProperty().bind(heightProperty());
            setClip(paneClip);
            
            axisX = new NumberAxis(-10, 10, 1);
            axisX.setSide(Side.BOTTOM);
            axisX.setPrefWidth(1920);
            axisX.layoutYProperty().bind(Bindings.divide(this.heightProperty(), 2));
            axisX.layoutXProperty().bind(Bindings.add(Bindings.subtract(0, Bindings.divide(axisX.prefWidthProperty(), 2)), Bindings.divide(widthProperty(), 2)));
            axisX.setMinorTickVisible(false);
            axisX.getStyleClass().addAll("graph-axis", "horizontal");

            axisY = new NumberAxis(-10, 10, 1);
            axisY.setSide(Side.LEFT);
            axisY.setPrefHeight(1920);
            axisY.layoutXProperty().bind(Bindings.subtract((Bindings.divide(this.widthProperty(), 2)), axisY.widthProperty()));
            axisY.layoutYProperty().bind(Bindings.add(Bindings.subtract(0, Bindings.divide(axisY.prefHeightProperty(), 2)), Bindings.divide(heightProperty(), 2)));
            axisY.setMinorTickVisible(false);
            axisY.getStyleClass().addAll("graph-axis", "vertical");

            grid = new CoordinateGrid();

            getChildren().setAll(grid, axisX, axisY);
        }

        public double findLocationX(double x){
            return (x * axisX.getPrefWidth() / axisX.getUpperBound() / 2) + (getWidth() / 2);
        }

        public double findLocationY(double y){
            return (-y * axisY.getPrefHeight() / axisY.getUpperBound() / 2) + (getHeight() / 2);
        }


        public class CoordinateGrid extends Pane{
            private long steps;
            
            public CoordinateGrid(){
                getStyleClass().add("graph-grid");
            }

            public void drawGrid(){
                getChildren().clear();

                double start;

                steps = 2 * (int)(axisX.getUpperBound());

                for(int i = 1; i < steps; i++){
                    //left side, vertical lines
                    start = findLocationX(0) + (i * axisX.getWidth() / steps);
                    getChildren().add(new Line(start, 0, start, axisY.getHeight()));
                    //right side, vertical lines
                    start = findLocationX(0) - (i * axisX.getWidth() / steps);
                    getChildren().add(new Line(start, 0, start, axisY.getHeight()));

                    //left side, vertical lines
                    start = findLocationY(0) + (i * axisY.getHeight() / steps);
                    getChildren().add(new Line(0, start, axisX.getWidth(), start));
                    //right side, vertical lines
                    start = findLocationY(0) - (i * axisY.getHeight() / steps);
                    getChildren().add(new Line(0, start, axisX.getWidth(), start));
                }
            }
        }

        public void drawGrid(){
            grid.drawGrid();
        }
    }

    public class Graph extends StackPane{
        int facZoom;
        CoordinateGridPane gpaneAxes;
        GraphPath pathGraph;
        
        public Graph(){
            //clips off overflow
            Rectangle paneClip = new Rectangle();
            paneClip.widthProperty().bind(widthProperty());
            paneClip.heightProperty().bind(heightProperty());
            setClip(paneClip);

            facZoom = 100;
            gpaneAxes = new CoordinateGridPane();
            pathGraph = new GraphPath();

            getChildren().addAll(gpaneAxes, pathGraph);

            setOnScroll(new ScrollZoom());

            widthProperty().addListener(new ResizeGraph());
            heightProperty().addListener(new ResizeGraph());
        }

        public double findLocationX(double x, CoordinateGridPane axes){
            return (x * axes.axisX.getPrefWidth() / axes.axisX.getUpperBound() / 2) + (getWidth() / 2);
        }

        public double findLocationY(double y, CoordinateGridPane axes){
            return (-y * axes.axisY.getPrefHeight() / axes.axisY.getUpperBound() / 2) + (getHeight() / 2);
        }

        public void drawGrid(){
            gpaneAxes.drawGrid();
        }

        public class GraphPath extends Pane{
            private Path path;
            
            public GraphPath(){
                path = new Path();

                //adds graph line to view
                getChildren().add(path);

                //adds css class
                path.getStyleClass().add("graph-line");
            }
            
            //draws quadratic graph line
            public void drawGraph(){
                double a = Double.parseDouble(tfA.getText());
                double b = Double.parseDouble(tfB.getText());
                double c = Double.parseDouble(tfC.getText());
    
                double startX = 0, endX = 0; 
                if(a > 0){
                    startX = Math.min(QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getUpperBound(), "x1"), QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getUpperBound(), "x2"));
                    endX = Math.max(QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getUpperBound(), "x1"), QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getUpperBound(), "x2"));;
                }else if(a < 0){
                    startX = Math.min(QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getLowerBound(), "x1"), QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getLowerBound(), "x2"));
                    endX = Math.max(QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getLowerBound(), "x1"), QuadraticEquation.qEquation(a, b, c - gpaneAxes.axisY.getLowerBound(), "x2"));;
                }else if(a == 0){
                    startX = 0 - (getWidth() / 2);
                    endX = getWidth();
                }

                double currentX = startX;

                path.getElements().clear();

                path.getElements().add(
                    new MoveTo(findLocationX(startX, gpaneAxes), findLocationY(QuadraticEquation.findY(a, b, c, startX), gpaneAxes))
                );

                while(currentX <= endX){
                    currentX += 0.0005*facZoom;
                    
                    path.getElements().add(
                    new LineTo(findLocationX(currentX, gpaneAxes), findLocationY(QuadraticEquation.findY(a, b, c, currentX),gpaneAxes))
                    );
                }        
            }
        }

        public class ScrollZoom implements EventHandler<ScrollEvent>{
            public void handle(ScrollEvent event){
                if(event.getDeltaY() > 0 && facZoom > 100){
                    facZoom -= 10;
                }else if(event.getDeltaY() < 0){
                    facZoom += 10;
                }

                gpaneAxes.axisX.setLowerBound(-0.1 * facZoom);
                gpaneAxes.axisX.setUpperBound(0.1 * facZoom);

                gpaneAxes.axisY.setLowerBound(-0.1 * facZoom);
                gpaneAxes.axisY.setUpperBound(0.1 * facZoom);

                graph.pathGraph.drawGraph();
                graph.drawGrid();
            }
        }
        
        public class ResizeGraph implements ChangeListener<Number>{
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){
                graph.pathGraph.drawGraph();
                graph.drawGrid();
            }
        }
    }
}