import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuadraticEquationsApp extends Application {
    TextField tfA, tfB, tfC;
    Label lbX1, lbX2, lbD, lbEquation;
    
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
        CoordinateGridPane graphGrid = new CoordinateGridPane();
        
        //css classes
        graphGrid.getStyleClass().addAll("pane", "main-panel");
        
        //apply
        bpMain.setCenter(graphGrid);
        

        //scene setup----------------------------------------------
        Scene scene = new Scene(bpMain, 800, 400);
        
        //apply styles
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Quadratic Equations");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

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

                if(a == 0.0 ){ //a can't be 0 in order to calculate
                    throwAlert("Invalid input!", "A value can't be 0.");
                    return;
                }
            }catch(Exception e){ //if one or more values aren't numbers
                throwAlert("Invalid input!", "Enter numbers in all fields please.");
                return;
            }

            vysledok = QuadraticEquation.qEquation(a, b, c);
            
            if(vysledok == null){
                throwAlert("Error!", "Discriminant is smaller than 0.\nRoots are complex.");
                return;
            }else{
                if(vysledok.length == 2){ //if there's one root
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
        }
    }

    public class CoordinateGridPane extends Pane{
        private NumberAxis axisX;
        private NumberAxis axisY;

        //creates a coordinate grid with a specific, unchangeable size
        public CoordinateGridPane(int width, int height){
            setPrefSize(width, height);
            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            axisX = new NumberAxis(-10, 10, 1);
            axisX.setSide(Side.BOTTOM);
            axisX.setPrefWidth(width);
            axisX.setLayoutY(height / 2);
            axisX.setMinorTickVisible(false);
            
            axisY = new NumberAxis(-10, 10, 1);
            axisY.setSide(Side.LEFT);
            axisY.setPrefHeight(height);
            axisY.layoutXProperty().bind(Bindings.subtract((width / 2) + 1, axisY.widthProperty()));
            axisY.setMinorTickVisible(false);

            getChildren().setAll(axisX, axisY);
        }

        //creates a coordinate grid that changes relatively to its size
        public CoordinateGridPane(){
            prefWidthProperty().bind(this.widthProperty());
            prefHeightProperty().bind(this.heightProperty());
            
            axisX = new NumberAxis(-10, 10, 1);
            axisX.setSide(Side.BOTTOM);
            axisX.prefWidthProperty().bind(this.widthProperty());
            axisX.layoutYProperty().bind(Bindings.divide(this.heightProperty(), 2));
            
            axisY = new NumberAxis(-10, 10, 1);
            axisY.setSide(Side.LEFT);
            axisY.prefHeightProperty().bind(this.heightProperty());
            axisY.layoutXProperty().bind(Bindings.subtract((Bindings.divide(this.widthProperty(), 2)), axisY.widthProperty()));

            getChildren().setAll(axisX, axisY);
        }
    }
}