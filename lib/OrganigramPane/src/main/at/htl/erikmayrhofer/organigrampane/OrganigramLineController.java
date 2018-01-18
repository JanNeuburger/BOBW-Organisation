package at.htl.erikmayrhofer.organigrampane;

import at.htl.florianschwarcz.organisationalstructurelib.Position;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OrganigramLineController extends OrganigramRootBasedController {
    @Override
    protected void populateFromNode(Position p, PosPane parentPane){

        new RuntimeException("Organigramlinecontroller needs getSuper-method in Position, which is not present yet.").printStackTrace();

        PosPane thisPane = createPosPaneFromPosition(parentPane, p);
        if(parentPane == null)
            pane.setRoot(thisPane);
        else
            parentPane.addPos(thisPane);

        if(p.getSubordinates() != null) {
            for (Position sub : p.getSubordinates()) {
                PosPane currPane = createPosPaneFromPosition(thisPane, sub);
                thisPane.addPos(currPane);
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