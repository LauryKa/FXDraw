package lt.elka;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class DrawingPane extends Pane {
    Line line;
    Polyline polyline;
    Rectangle rectangle;
    Circle circle;

    public DrawingPane() {
        Rectangle clipRectangle = new Rectangle();
        clipRectangle.widthProperty().bind(this.widthProperty());
        clipRectangle.heightProperty().bind(this.heightProperty());
        this.setClip(clipRectangle);
    }

    EventHandler<MouseEvent> drawLineHandler = event -> {
        if (line == null) {
            line = new Line();
            line.setStartX(event.getX());
            line.setStartY(event.getY());
            line.setEndX(line.getStartX());
            line.setEndY(line.getStartY());
            line.setStroke(Color.WHITE);
        } else {
            line.setEndX(event.getX());
            line.setEndY(event.getY());
            this.getChildren().add(line);
            line = null;
        }
    };

    EventHandler<MouseEvent> drawPolylineHandler = event -> {
        if (polyline == null) {
            polyline = new Polyline();
            polyline.getPoints().addAll(event.getX(), event.getY());
            polyline.setStroke(Color.WHITE);

        } else {
            polyline.getPoints().addAll(event.getX(), event.getY());
            this.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    if (polyline.getPoints().size() > 3) {
                        this.getChildren().add(polyline);
                    }
                    polyline = null;
                    this.setOnKeyPressed(null);
                }

            });
        }
    };
    EventHandler<MouseEvent> drawRectangleHandler = event -> {

        if (rectangle == null) {
            rectangle = new Rectangle();
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
            rectangle.setStroke(Color.WHITE);
            rectangle.setFill(null);
        } else {
            if (event.getX() < rectangle.getX()) {
                double startX = rectangle.getX();
                rectangle.setX(event.getX());
                rectangle.setWidth(startX - event.getX());
            } else {
                rectangle.setWidth(event.getX() - rectangle.getX());
            }
            if (event.getY() < rectangle.getY()) {
                double startY = rectangle.getY();
                rectangle.setY(event.getY());
                rectangle.setHeight(startY - event.getY());
            } else {
                rectangle.setHeight(event.getY() - rectangle.getY());
            }
            this.getChildren().add(rectangle);
            rectangle = null;
        }

    };


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


}
