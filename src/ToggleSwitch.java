import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ToggleSwitch extends Parent{
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);

    public BooleanProperty switchedOnProperty(){
        return switchedOn;
    }

    public ToggleSwitch(double width){
        Rectangle background = new Rectangle(width, width/2);
        background.setArcWidth(width/2);
        background.setArcHeight(width/2);

        Circle trigger = new Circle(width/4);
        trigger.setCenterX(width/4);
        trigger.setCenterY(width/4);
        

        getStyleClass().add("toggle-switch");
        background.getStyleClass().add("background");
        trigger.getStyleClass().add("trigger");

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState.booleanValue();

            if(isOn){
                trigger.setTranslateX(width/2);
                background.getStyleClass().add("on");
            }else{
                trigger.setTranslateX(0);
                background.getStyleClass().remove("on");
            }
        });

        setOnMouseClicked(event -> {
            switchedOn.set(!switchedOn.get());
        });
    }
}
