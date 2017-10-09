package lt.elka;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Main extends Application {
    Stage window;
    BorderPane layout;
    VBox controlLeft;
    HBox infoBox;
    VBox propertiesBox;
    DrawingPane drawingPane;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        primaryStage.setTitle("FXDraw");

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        fileMenu.getItems().add(new MenuItem("New Project..."));
        fileMenu.getItems().add(new MenuItem("New Module..."));
        fileMenu.getItems().add(new MenuItem("Import Project..."));
        editMenu.getItems().add(new MenuItem("Select"));
        editMenu.getItems().add(new MenuItem("Copy"));
        editMenu.getItems().add(new MenuItem("Paste"));

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,editMenu);

        Button drawLine = new Button("Line");

        Button drawPolyline = new Button("Polyline");
        Button drawRectangle = new Button("Rectangle");
        Button drawCircle = new Button("Circle");
        Button select = new Button("Select");
        Button move = new Button("Move");
        Button delete = new Button("Delete");


        controlLeft = new VBox();
        controlLeft.getChildren().addAll(drawLine,drawPolyline,drawRectangle,drawCircle,select,move,delete);
        controlLeft.setSpacing(10);
        controlLeft.setPadding(new Insets(10,10,10,10));


        infoBox = new HBox();
        infoBox.getChildren().addAll(new Label("This is info box"));
        infoBox.setPadding(new Insets(10,10,10,10));

        propertiesBox = new VBox();
        propertiesBox.getChildren().addAll(new Label("This is properties"));
        propertiesBox.setPadding(new Insets(10,10,10,10));

        drawingPane = new DrawingPane();
        drawingPane.getStyleClass().add("drawing-pane");

        drawLine.setOnAction(event ->{
            drawingPane.drawLine();
        });

        drawCircle.setOnAction(event ->{
            drawingPane.drawCircle();
        });
        drawPolyline.setOnAction(event ->{
            drawingPane.drawPolyline();
        });

        drawRectangle.setOnAction(event -> {
            drawingPane.drawRectangle();
        });


        layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setLeft(controlLeft);
        layout.setBottom(infoBox);
        layout.setRight(propertiesBox);
        layout.setCenter(drawingPane);

        Scene scene = new Scene(layout, 800,800);
        scene.getStylesheets().add("Style.css");
        window.setScene(scene);

        scene.onKeyPressedProperty().bind(drawingPane.onKeyPressedProperty());
        window.show();

    }
}
