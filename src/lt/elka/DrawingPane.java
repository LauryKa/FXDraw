package lt.elka;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

public class DrawingPane extends Pane {
    Line line;
    Polyline polyline;
    int polylineSize;
    Rectangle rectangle;
    Circle circle;
    Rectangle selectionRectange;
    double startX;
    double startY;
    double endX;
    double endY;
    ObservableList<Node> shapes = this.getChildren();
    ObservableList<Node> selectedShapes = FXCollections.observableArrayList();
    ObservableList<Node> selectionBounds = FXCollections.observableArrayList();

    public DrawingPane() {
        Rectangle clipRectangle = new Rectangle();
        clipRectangle.widthProperty().bind(this.widthProperty());
        clipRectangle.heightProperty().bind(this.heightProperty());
        this.setClip(clipRectangle);
        this.setOnKeyPressed(delauftKeyHandler);
        DrawingPane drawingPane = this;
//        shapes.addListener(new ListChangeListener<Node>() {
//            @Override
//            public void onChanged(Change<? extends Node> c) {
//                drawingPane.getChildren().clear();
//                drawingPane.getChildren().addAll(shapes);
//            }
//        });
    }


    EventHandler<KeyEvent> delauftKeyHandler = event -> {
        if (event.getCode() == KeyCode.ESCAPE) {
            this.setOnMouseClicked(null);
        }
        if (event.getCode() == KeyCode.L) {
            drawLine();
        }
        if (event.getCode() == KeyCode.P) {
            drawPolyline();
        }
        if (event.getCode() == KeyCode.S) {
            select();
        }
    };

    EventHandler<MouseEvent> selectHandler = event -> {

            drawAnyRectangle(event, false, true, Color.RED, Color.YELLOW, null, true);
            System.out.println("clicked");

        if(!selectionBounds.isEmpty())  {
            Rectangle selectionArea = (Rectangle) selectionBounds.get(0);
            for (Node shape : shapes) {
                Shape intersect = Shape.intersect(selectionArea,(Shape)shape);
                if (intersect.getBoundsInLocal().getWidth() != -1)
                    System.out.println(shape.toString());
            }
            selectionBounds.clear();
        }

    };




    EventHandler<MouseEvent> drawLineHandler = event -> {
        if (line == null) {
            line = new Line();
            line.setStartX(event.getX());
            line.setStartY(event.getY());
            line.setEndX(line.getStartX());
            line.setEndY(line.getStartY());

            this.setOnMouseMoved(e -> {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
            });
            this.getChildren().add(line);
            line.setStroke(Color.RED);
            line.getStrokeDashArray().addAll(5.0, 5.0);

        } else {
            line.setStroke(Color.WHITE);
            line.getStrokeDashArray().clear();
            line.setEndX(event.getX());
            line.setEndY(event.getY());
            this.setOnMouseMoved(null);
            System.out.println(line.getTransforms().toString());

            line = null;
        }
    };

    EventHandler<MouseEvent> polyGhost = event -> {
        polyline.getPoints().set(polylineSize - 2, event.getX());
        polyline.getPoints().set(polylineSize - 1, event.getY());
    };

    EventHandler<MouseEvent> drawPolylineHandler = event -> {
        if (polyline == null) {
            polyline = new Polyline();
            polyline.getPoints().addAll(event.getX(), event.getY());
            polyline.getPoints().addAll(event.getX(), event.getY());
            polylineSize = polyline.getPoints().size();

            polyline.setStroke(Color.RED);
            polyline.getStrokeDashArray().addAll(5.0, 5.0);
            this.getChildren().add(polyline);
            this.setOnMouseMoved(polyGhost);

        } else {
            this.setOnMouseMoved(null);
            polyline.getPoints().addAll(event.getX(), event.getY());
            polylineSize = polyline.getPoints().size();
            this.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    if (polyline.getPoints().size() < 4) {
                        this.getChildren().remove(polyline);
                    }
                    polyline.setStroke(Color.WHITE);
                    polyline.getStrokeDashArray().clear();
                    polyline.getPoints().remove(polyline.getPoints().size() - 2);
                    polyline.getPoints().remove(polyline.getPoints().size() - 1);

                    polyline = null;
                    this.setOnKeyPressed(delauftKeyHandler);
                    this.setOnMouseMoved(null);
                }

            });
            this.setOnMouseMoved(polyGhost);
        }
    };

    EventHandler<MouseEvent> drawRectangleHandler = event -> {
        drawAnyRectangle(event, true, true, Color.RED, Color.WHITE, null, true);
    };

    private void drawAnyRectangle(MouseEvent event, boolean addToPane, boolean addToSelectionBoundsList, Paint tempStroke, Paint stroke, Paint fill, boolean dashed) {
        if (line == null && rectangle == null) {
            line = new Line();
            rectangle = new Rectangle();
            line.setStartX(event.getX());
            line.setStartY(event.getY());
            line.setEndX(line.getStartX());
            line.setEndY(line.getStartY());

            this.setOnMouseMoved(e -> {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                rectangleVSlineBounds();

            });
            this.getChildren().add(rectangle);

            rectangle.setStroke(tempStroke);
            if (dashed)
                rectangle.getStrokeDashArray().addAll(5.0, 5.0);
            rectangle.setFill(fill);

        } else {
            this.setOnMouseMoved(null);
            line.setEndX(event.getX());
            line.setEndY(event.getY());
            rectangleVSlineBounds();
            rectangle.setStroke(stroke);
            rectangle.getStrokeDashArray().clear();
            if (!addToPane) {
                this.getChildren().remove(rectangle);
            }
            if (addToSelectionBoundsList) {
                selectionBounds.add(rectangle);
            }

            line = null;
            rectangle = null;
        }
    }

    private void rectangleVSlineBounds() {
        rectangle.setX(line.getBoundsInLocal().getMinX());
        rectangle.setY(line.getBoundsInLocal().getMinY());
        rectangle.setWidth(line.getBoundsInLocal().getMaxX() - line.getBoundsInLocal().getMinX());
        rectangle.setHeight(line.getBoundsInLocal().getMaxY() - line.getBoundsInLocal().getMinY());

    }

    EventHandler<MouseEvent> drawCircleHandler = event -> {
        if (circle == null) {
            circle = new Circle();
            circle.setCenterX(event.getX());
            circle.setCenterY(event.getY());
            circle.setRadius(0);
            circle.setFill(null);
            circle.setStroke(Color.WHITE);
        } else {
            circle.setRadius(Math.sqrt(Math.pow((event.getX() - circle.getCenterX()), 2) + Math.pow((event.getY() - circle.getCenterY()), 2)));
            this.getChildren().add(circle);
            circle = null;
        }
    };

    public void drawLine() {
        this.setOnMouseClicked(drawLineHandler);
    }

    public void drawCircle() {
        this.setOnMouseClicked(drawCircleHandler);
    }

    public void drawPolyline() {
        this.setOnMouseClicked(drawPolylineHandler);
    }

    public void drawRectangle() {
        this.setOnMouseClicked(drawRectangleHandler);
    }

    public void select() {
        this.setOnMouseClicked(selectHandler);
    }


}
