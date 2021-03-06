package bobworga.Controller;

import at.htl.erikmayrhofer.organigrampane.*;
import at.htl.erikmayrhofer.organisationalstructurecomponents.job.JobInfo;
import at.htl.erikmayrhofer.organisationalstructurecomponents.position.PositionInfo;
import at.htl.florianschwarcz.organisationalstructurelib.Position;
import bobworga.model.Repository;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Positions extends BorderPane{

    @FXML
    private OrganigramPane organigramPane;

    public Positions(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tabs/Positions.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        organigramPane.setController(new OrganigramChildController(position -> {
            VBox box = new VBox();
            box.getChildren().add(new PositionInfo(position));

            Button button = new Button("Add Child");
            button.setOnAction(actionEvent -> {
                position.addSubordinate(new Position());
                Repository.getInstance().getHierarchy().invalidate();
            });
            box.getChildren().add(button);
            Button removeButton = new Button("X");
            removeButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Position");
                alert.setHeaderText("Are you sure to remove this position?");
                alert.setContentText("Removing a position is destructive, it cannot be restored.");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    Repository.getInstance().deletePosition(position);
                }
            });
            box.getChildren().add(removeButton);
            return box;
        }));
        organigramPane.getController().setHierarchy(Repository.getInstance().getHierarchy());
        organigramPane.setOnPositionDoubleClicked(positionClickEvent -> {
            Stage stage = new Stage();
            EditPositionDialog dialog = new EditPositionDialog(positionClickEvent.getPosition());
            Scene scene = new Scene(dialog);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Edit Position");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(getScene().getWindow());
            stage.show();
            dialog.setOnFinished(actionEvent -> stage.close());
        });
    }
}
