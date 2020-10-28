package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import javafx.application.Application;
import javafx.application.Platform;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import java.util.Random;
import java.sql.SQLException;

public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
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
    int ghost_move = 4;
    int playerHp = map.getPlayer().getHealth();
    GameDatabaseManager dbManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
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
        scene.setOnKeyReleased(this::onKeyReleased);
        scene.addEventFilter(KeyEvent.KEY_PRESSED,this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
        TextInputDialog td = new TextInputDialog("name");
        td.setTitle("Enter name");
        td.setHeaderText("Enter your name");
        td.showAndWait().ifPresent(name -> {
            nameLabel.setText(name);
            map.getPlayer().setName(name);
        });
    }
    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }

    private void changeMap() {
        if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.EXIT){
            map = MapLoader.loadMap("/map2.txt");
            Canvas canvas = new Canvas(
                    map.getWidth() * Tiles.TILE_WIDTH,
                    map.getHeight() * Tiles.TILE_WIDTH);
            GraphicsContext context = canvas.getGraphicsContext2D();
            refresh();
        }
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

    private void fight(String enemy, int dx, int dy){
        switch (enemy){
            case "skeleton":
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(dx, dy).getActor() == map.getSkeleton()){
                    int damageToSkeleton = map.getSkeleton().getHealth() - 50;
                    map.getSkeleton().setHealth(damageToSkeleton);
                    playerHp = playerHp - 10;
                }
                if (map.getSkeleton().getHealth() <= 0){
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(dx, dy).setActor(null);
                }
                break;
            case "octopus":
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(dx, dy).getActor() == map.getOctopus()){
                    playerHp = playerHp - 100;
                }
                break;
            default: break;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:

                changeMap();
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(0,-1);
                    refresh();
                }else{
                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).getItem() != null){
                        if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).getItem().getTileName() == "apple"){
                            playerHp = playerHp + 20;
                            map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).setItem(null);
                        }
                    }
                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).getActor() != null){
                        fight(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, -1).getActor().getTileName(),0,-1);

                    }
                    map.getPlayer().move(0, -1);
                    itemDetection();
                    refresh();
                    ghost_move++;
                    break;

                }
            case DOWN:

                changeMap();
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(0,1);
                    refresh();
                } else{

                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, 1).getActor() != null){
                        fight(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, 1).getActor().getTileName(),0,1);

                    }

                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, 1).getItem() != null){
                        if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, 1).getItem().getTileName() == "apple"){
                            playerHp = playerHp + 20;
                            map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(0, 1).setItem(null);
                        }
                    }

                    map.getPlayer().move(0, 1);
                    itemDetection();
                    refresh();
                    ghost_move++;
                    break;
                }
            case LEFT:
                changeMap();
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(-1,0);
                    refresh();
                } else{

                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(-1, 0).getItem() != null){
                        if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(-1, 0).getItem().getTileName() == "apple"){
                            playerHp = playerHp + 20;
                            map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(-1, 0).setItem(null);
                        }
                    }

                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(-1, 0).getActor() != null){
                        fight(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(-1, 0).getActor().getTileName(),-1,0);

                    }

                    map.getPlayer().move(-1, 0);
                    itemDetection();
                    refresh();
                    ghost_move++;
                    break;
                }
            case RIGHT:
                changeMap();
                if(nameLabel.getText().equals("Ciprian") || nameLabel.getText().equals("Despanu") || nameLabel.getText().equals("Andrei")){
                    map.getPlayer().move2(1,0);
                    refresh();
                }else{

                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(1, 0).getItem() != null){
                        if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(1, 0).getItem().getTileName() == "apple"){
                            playerHp = playerHp + 20;
                            map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(1, 0).setItem(null);
                        }
                    }

                    if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(1, 0).getActor() != null){
                        fight(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getNeighbor(1, 0).getActor().getTileName(),1,0);

                    }
                    map.getPlayer().move(1, 0);
                    itemDetection();
                    refresh();
                    ghost_move++;
                    break;

                }
            case S:
                TextInputDialog td = new TextInputDialog("name");
                td.setTitle("Save Game");
                td.setHeaderText("Save name");
                td.showAndWait().ifPresent(name -> {
                    Player player = map.getPlayer();
                    dbManager.savePlayer(player);
                });
                break;

        }
    }

    private int moveOctopus = 0;

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if(playerHp <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Game Over");
            alert.setContentText("You died , restart the game and try again!");

            alert.showAndWait();
            System.exit(0);
        }

        if(map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.FINISH){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Congratulations");
            alert.setContentText("You Win");

            alert.showAndWait();
            System.exit(0);
        }

        int startX;
        int endX;

        if(map.getWidth() <= 25) {
            startX = 0;
            endX = 25;
        } else {
            int before = 12;
            int after = 12;
            int playerX = map.getPlayer().getX();
            if ((playerX - before) < 0 ){
                before = playerX;
                after += after - before;
            } else if((map.getWidth() - 1) - playerX < after) {
                after = map.getWidth() - 1 - playerX;
                before += before - after;
            }
            startX = playerX - before;
            endX = playerX + after + 1;
        }



        for (int x = startX; x < endX; x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x-startX, y);
                }
                else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x-startX, y);}
                else {
                    Tiles.drawTile(context, cell, x-startX, y);
                }
            }
        }
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
        if(ghost_move % 2 == 0){

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
        }

        //Ghost move
        int x = 0;
        int y = 0;
        if(ghost_move % 5 == 0){

            while(!map.getCell(x, y).getTileName().equals("floor")){
                x = rand.nextInt(map.getWidth()-1);
                y = rand.nextInt(map.getHeight()-1);
            }
        }


        map.getGhost().move(x-map.getGhost().getX(), y-map.getGhost().getY());
        inventoryLabel.setText("" + map.getPlayer().getInventory());
        healthLabel.setText("Health:  " + playerHp);
        skeletonHealthLabel.setText("Skeleton health:  " + map.getSkeleton().getHealth());
    }

        private void setupDbManager() {
            dbManager = new GameDatabaseManager();
            try {
                dbManager.setup();
            } catch (SQLException ex) {
                System.out.println("Cannot connect to database.");
            }
        }

        private void exit() {
            try {
                stop();
            } catch (Exception e) {
                System.exit(1);
            }
            System.exit(0);
        }
}
