package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label nameLabel = new Label();
    Label healthLabel = new Label();
    Random rand = new Random();
    Label inventoryLabel = new Label();
    Button pickButton = new Button("Pick Up Item");
    Label skeletonHealthLabel = new Label();
    Label octopusHealthLabel = new Label();
    Label ghostHealthLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        pickButton.setVisible(false);

        ui.add(healthLabel, 0, 0);
        ui.add(skeletonHealthLabel, 0, 1);
        skeletonHealthLabel.setVisible(false);
        ui.add(octopusHealthLabel, 0, 2);
        octopusHealthLabel.setVisible(false);
        ui.add(ghostHealthLabel, 0, 3);
        ghostHealthLabel.setVisible(false);

        ui.add(new Label("Inventory: "), 0, 2);
        ui.add(inventoryLabel, 0, 4);

        ui.add(new Label("Name: "),0, 5);
        ui.add(nameLabel,1,5);


        ui.add(pickButton,0,150);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.addEventFilter(KeyEvent.KEY_PRESSED,this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
        TextInputDialog td = new TextInputDialog("name");
        td.setTitle("Enter name");
        td.setHeaderText("Enter your name");
        td.showAndWait().ifPresent(name -> {nameLabel.setText(name);});
    }

    private void itemDetection() {
        if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getItem() != null) {
            pickButton.setVisible(true);
            refresh();
        } else {
            pickButton.setVisible(false);
        }
        pickButton.setOnAction(e -> {
            pickButton.setVisible(false);
            map.getPlayer().addToInventory(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getItem().getName());
            map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setItem(null);
            refresh();
        });
    }

    private void fight(String enemy){
        switch (enemy){
            case "skeleton":
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).getActor() == map.getSkeleton()){
                    int damageToSkeleton = map.getSkeleton().getHealth() - 50;
                    int damageToPlayer = map.getPlayer().getHealth() - 10;
                    map.getSkeleton().setHealth(damageToSkeleton);
                    map.getPlayer().setHealth(damageToPlayer);
                }
                if (map.getSkeleton().getHealth() <= 0){
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).setActor(null);
                }
                break;
            default: break;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(0,-1);
                    refresh();
                }
                fight("skeleton");
                map.getPlayer().move(0, -1);
                itemDetection();
                refresh();
                break;
            case DOWN:
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(0,1);
                    refresh();
                }
                fight("skeleton");

                map.getPlayer().move(0, 1);
                itemDetection();
                refresh();
                break;
            case LEFT:
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(-1,0);
                    refresh();
                }
                fight("skeleton");

                map.getPlayer().move(-1, 0);
                itemDetection();
                refresh();
                break;
            case RIGHT:
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(1,0);
                    refresh();
                }else{
                    map.getPlayer().move(1, 0);
                    itemDetection();
                    refresh();
                    break;

                }
                fight("skeleton");
                map.getPlayer().move(1, 0);
                refresh();
                break;
        }
    }

    private int moveOctopus = 0;
    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                }
                else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);}
                else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("Health:  " + map.getPlayer().getHealth());
        skeletonHealthLabel.setText("Skeleton health:  " + map.getSkeleton().getHealth());
        if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, 1).getActor() == map.getSkeleton() && !skeletonHealthLabel.isVisible()){
            skeletonHealthLabel.setVisible(true);
        } else if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).getActor() == map.getSkeleton()){
            skeletonHealthLabel.setVisible(true);
        } else if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(1, 0).getActor() == map.getSkeleton()){
            skeletonHealthLabel.setVisible(true);
        } else if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(-1, 0).getActor() == map.getSkeleton()){
            skeletonHealthLabel.setVisible(true);
        } else {
            skeletonHealthLabel.setVisible(false);
        }

        //Octopus move
        if (moveOctopus <2 ){
            map.getOctopus().move(1,0);
        } else{
            map.getOctopus().move(-1,0);
        }
        moveOctopus++;
        if (moveOctopus == 8){
            moveOctopus = 0;
            map.getOctopus().move(3,0);
        }

        //Ghost move
        int x = 0;
        int y = 0;
        while(!map.getCell(x, y).getTileName().equals("floor")){
            x = rand.nextInt(map.getWidth()-1);
            y = rand.nextInt(map.getHeight()-1);
        }
        map.getGhost().move(x-map.getGhost().getX(), y-map.getGhost().getY());
        healthLabel.setText("" + map.getPlayer().getHealth());
        inventoryLabel.setText("" + map.getPlayer().getInventory());
    }
}
