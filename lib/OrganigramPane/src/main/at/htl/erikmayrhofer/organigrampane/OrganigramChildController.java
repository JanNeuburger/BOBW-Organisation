package at.htl.erikmayrhofer.organigrampane;

import at.htl.florianschwarcz.organisationalstructurelib.Position;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OrganigramChildController extends OrganigramRootBasedController {

    @Override
    protected void populateFromNode(Position p, PosPane parentPane){
        PosPane thisPane = createPosPaneFromPosition(parentPane, p);
        if(parentPane == null)
            pane.setRoot(thisPane);
        else
            parentPane.addPos(thisPane);
        if(p.getSubordinates() != null) {
            for (Position sub : p.getSubordinates()) {
                populateFromNode(sub, thisPane);
            }
        }
        pane.relayout();
    }

    private PosPane createPosPaneFromPosition(PosPane parent, Position p){
        VBox content = new VBox();
        content.getChildren().addAll(
                new Label(p.getJob().getName()),
                new Label(p.getPerson().getFirstName() + " " + p.getPerson().getLastName()));
        return new PosPane(pane,content,p.getJob().getName());
    }
}