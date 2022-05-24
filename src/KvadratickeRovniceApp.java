import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KvadratickeRovniceApp extends Application {
    TextField tfA, tfB, tfC;
    Label lbX1, lbX2, lbD;
    
    public void start(Stage stage) throws Exception{
        

        //UI build
        BorderPane bpMain = new BorderPane();
        bpMain.getStyleClass().add("bpMain");
        
        //top panel------------------------------------------------
        Label lbHead = new Label("Kvadratické Rovnice");
        //lbHead.setMaxWidth(Double.MAX_VALUE);
        lbHead.setMinHeight(60);
        //css classes
        lbHead.getStyleClass().add("header");
        lbHead.getStyleClass().add("pane");
        //apply
        bpMain.setTop(lbHead);

        //left panel-----------------------------------------------
        Button btVypocet = new Button("Výsledok");
        btVypocet.setOnAction(new KlikVysledok());
        
        tfA = new TextField("1");
        tfB = new TextField("6");
        tfC = new TextField("5");
        Label lbRovnica = new Label("ax\u00B2+bx+c=0");
        
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
        
        VBox vbLeftPanel = new VBox(gpLpFields, lbRovnica, btVypocet);
        
        //css classes
        vbLeftPanel.getStyleClass().add("left-panel");
        vbLeftPanel.getStyleClass().add("pane");
        
        //apply
        bpMain.setLeft(vbLeftPanel);

        //scene setup----------------------------------------------
        Scene scene = new Scene(bpMain, 800, 400);
        
        //apply styles
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Kvadratické Rovnice");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public class KlikVysledok implements EventHandler{
        public void handle(Event event){
            double a = Double.parseDouble(tfA.getText());
            double b = Double.parseDouble(tfB.getText());
            double c = Double.parseDouble(tfC.getText());

            double vysledok[] = KvadratickaRovnica.kvRovnica(a, b, c);

            if(vysledok.length == 2){ //ak je jeden koren
                lbD.setText("" + vysledok[0]);
                lbX1.setText("" + vysledok[1]);
                lbX2.setText("");
            }else if(vysledok.length == 3){
                lbD.setText("" + vysledok[0]);
                lbX1.setText("" + vysledok[1]);
                lbX2.setText("" + vysledok[2]);
            }
        }
    }
}