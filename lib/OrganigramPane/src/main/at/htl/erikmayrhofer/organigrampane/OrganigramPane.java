package at.htl.erikmayrhofer.organigrampane;

import at.htl.florianschwarcz.organisationalstructurelib.Position;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

import java.util.ArrayList;


public class OrganigramPane extends ScrollPane {



    private OrganigramController controller;

    private PosPane head;

    //TODO can this be omitted
    private Pane dragPane;

    private DoubleProperty centerPos;

    private EventHandler<PositionClickEvent> posClickHandler;


    public OrganigramPane(OrganigramController controller){
        this();

        setController(controller);
    }

    public OrganigramPane() {

        StackPane centerPane = new StackPane();
        centerPane.setPrefSize(StackPane.USE_COMPUTED_SIZE,StackPane.USE_COMPUTED_SIZE);
        centerPane.setCenterShape(true);

        dragPane = new Pane();
        dragPane.setPrefSize(Pane.USE_COMPUTED_SIZE,Pane.USE_COMPUTED_SIZE);

        centerPane.getChildren().add(dragPane);
        setContent(centerPane);
        setPannable(true);

        centerPos = new SimpleDoubleProperty(0);
    }

    public void setController(OrganigramController controller) {
        this.controller = controller;
        controller.setOrganigramPane(this);
    }

    public OrganigramController getController() {
        return controller;
    }

    public void invalidate(){
        Platform.runLater(this::relayout);
    }

    void relayout(){
        ArrayList<TreeLayer> spaces = new ArrayList<>();


        TreeLayer firstLayer = new TreeLayer();
        TreeLayerGroup headLayerGroup = new TreeLayerGroup(firstLayer);
        TreeLayerPos headPos = new TreeLayerPos(head, firstLayer);
        headLayerGroup.add(headPos);
        firstLayer.add(headLayerGroup);
        spaces.add(firstLayer);

        while(true){
            TreeLayer currLayer = new TreeLayer();

            for(TreeLayerGroup g : spaces.get(spaces.size()-1)){
                for(TreeLayerPos p : g){
                    TreeLayerGroup currGroup = new TreeLayerGroup(currLayer);
                    currGroup.addAll(p.getPos().getChildPos());
                    if(currGroup.size() > 0) {
                        p.setChildren(currGroup);
                        currLayer.add(currGroup);
                    }
                }
            }
            if(currLayer.size() == 0)
                break;
            spaces.add(currLayer);
        }

        int lastY = 0;
        for(TreeLayer l : spaces){
            double maxHeight = 0;
            for(TreeLayerGroup g : l){
                for(TreeLayerPos p : g){
                    maxHeight = Math.max(maxHeight,PosPane.POS_PANE_HEIGHT+2*PosPane.POS_PANE_MARGIN_Y);
                }
            }
            l.setHeight((int)maxHeight);
            l.setStartY(lastY);
            lastY = l.getEndY();
        }

        headPos.bubbleDown();

        //PosPane set Pos
        for(TreeLayer l : spaces){
            for(TreeLayerGroup g : l){
                for(TreeLayerPos p : g){
                    p.getPos().setLayoutX(p.getStartX());
                    p.getPos().setLayoutY(p.getY());
                }
            }
        }
    }

    void stagePos(PosPane pos){
        dragPane.getChildren().addAll(pos);
        pos.setOnMouseClicked(mouseEvent -> {
            if(posClickHandler != null){
                if(mouseEvent.getClickCount() == 2)
                    posClickHandler.handle(new PositionClickEvent(mouseEvent, pos.getPosition()));
            }
        });
    }

    void setRoot(PosPane rootPane){
        if(this.head != null) throw new IllegalStateException("Root is already set. call reset first");
        //rootPane.layoutXProperty().bind(offsetPosProperty());
        this.head = rootPane;
        dragPane.getChildren().add(rootPane);
    }

    void reset(){
        this.head = null;
        dragPane.getChildren().clear();
    }

    void stageLine(Line l){
        dragPane.getChildren().addAll(l);
    }

    private void setRoot(Node child, Position pos){
        setRoot(new PosPane(this,child,"Root", pos));
    }

    private double getOffsetPos() {
        return centerPos.get();
    }

    private DoubleProperty offsetPosProperty() {
        return centerPos;
    }

    private void setOffsetPos(double centerPos) {
        this.centerPos.set(centerPos);
    }

    public void setOnPositionDoubleClicked(EventHandler<PositionClickEvent> eventhandler){
        this.posClickHandler = eventhandler;
    }
}

