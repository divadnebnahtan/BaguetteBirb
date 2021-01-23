package me.aloidia.baguettebirb;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import me.aloidia.baguettebirb.sprites.Baguette;
import me.aloidia.baguettebirb.sprites.Pigeon;

import java.util.*;

public class Main extends Application {
    public static final Random random = new Random();
    public final List<Baguette> baguettes = new ArrayList<>();
    public final HashMap<Text, Boolean> displayText = new HashMap<>();

    private final Highscore hs = new Highscore();
    private final Pane root = new Pane();
    private final List<String> input = new ArrayList<>();

    public boolean clickMove = false;
    public int secondsRemaining = 0;
    public double baguetteMultiplier = 1.0d;

    public Pigeon pigeon;
    private GameLoop loop;

    public static void main(String[] args) {
        launch(args);
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(runnable);
            }
        };
        new Timer().schedule(timerTask, delay);
    }

    @Override
    public void start(Stage stage) {
        hs.load();
        createMenu();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            if (!input.contains(e.getCode().toString())) input.add(e.getCode().toString());
        });
        scene.setOnKeyReleased(e -> input.remove(e.getCode().toString()));
        scene.setOnMouseClicked(e -> {
            if (clickMove) {
                pigeon.setPosX(e.getX() - (pigeon.getWidth() * pigeon.getScaleX()) / 2);
                pigeon.setPosY(e.getY() - (pigeon.getHeight() * pigeon.getScaleY()) / 2);
            }
        });
        stage.setOnCloseRequest(e -> {
            if (pigeon != null) hs.addScore(pigeon.getScore());
            Platform.exit();
            System.exit(0);
        });
        stage.setTitle(Constants.TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void createContent() {
        root.getChildren().clear();
        root.setPrefSize(Constants.SIZE, Constants.SIZE);

        Canvas canvas = new Canvas(Constants.SIZE, Constants.SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        resetGCSettings(gc);

        root.getChildren().addAll(canvas);
        loop = new GameLoop(this, gc);

        startGame();
    }

    private void createMenu() {
        root.getChildren().clear();
        root.setPrefSize(Constants.SIZE, Constants.SIZE);
        root.setId("root");
        root.getStylesheets().add(Constants.styleSheet());

        Label titleLbl = new Label(Constants.TITLE);
        titleLbl.setId("titleLbl");
        titleLbl.setAlignment(Pos.CENTER);
        titleLbl.setFont(Font.font(48));
        titleLbl.setPrefSize(Constants.SIZE, 100);

        Label scoreLbl = new Label("Top scores:\n" + hs.toString());
        scoreLbl.setId("scoreLbl");
        scoreLbl.setLayoutX(50);
        scoreLbl.setLayoutY(90);
        scoreLbl.setFont(Font.font(24));
        scoreLbl.setAlignment(Pos.CENTER);
        scoreLbl.setTextAlignment(TextAlignment.CENTER);
        scoreLbl.setPrefSize(500, 400);

        Button startBtn = new Button("Start");
        startBtn.setId("startBtn");
        startBtn.setLayoutX(50);
        startBtn.setLayoutY(520);
        startBtn.setMnemonicParsing(false);
        startBtn.setFont(Font.font(20));
        startBtn.setPrefSize(500, 50);
        startBtn.setOnAction(event -> createContent());

        root.getChildren().addAll(titleLbl, scoreLbl, startBtn);
    }

    public void handlePresses() {

        if (pigeon.canWalk()) {
            if (!Collections.disjoint(input, Constants.WALKING_KEYS)) {
                pigeon.setWalking(true);
            }
            if (input.contains("A") || input.contains("LEFT")) {
                pigeon.addVel(-Constants.PIGEON_SPEED * pigeon.getSpeedMultiplier(), 0);
                pigeon.setFacing(true);
            }
            if (input.contains("D") || input.contains("RIGHT")) {
                pigeon.addVel(Constants.PIGEON_SPEED * pigeon.getSpeedMultiplier(), 0);
                pigeon.setFacing(false);
            }
            if (input.contains("W") || input.contains("UP")) {
                pigeon.addVel(0, -Constants.PIGEON_SPEED * pigeon.getSpeedMultiplier());
            }
            if (input.contains("S") || input.contains("DOWN")) {
                pigeon.addVel(0, Constants.PIGEON_SPEED * pigeon.getSpeedMultiplier());
            }
        }
    }

    public void handleCollisions() {
        baguettes.removeIf(baguette -> {
            boolean ret = pigeon.intersects(baguette);
            if (ret) {
                pigeon.peck();

                double balance = 0.0d;
                if (baguette.getPosX() <= 250) balance = -1.0d;
                else if (baguette.getPosX() >= 350) balance = 1.0d;

                playSound(Constants.getEatingSFX(), 0.5d, balance, 1.0d, 0.0d, 1);
                pigeon.consume(baguette, this);
            }
            return ret;
        });
    }

    public void handleReleases() {
        if (Collections.disjoint(input, Constants.WALKING_KEYS)) {
            pigeon.setWalking(false);
        }
    }

    public void spawnBaguette() {
        Baguette baguette = new Baguette();
        int padding = 5;

        int highX = (int) (Constants.SIZE - ((baguette.getWidth() * baguette.getScaleX()) + padding));
        int baguetteX = random.nextInt(highX - padding) + padding + 1;

        int lowY = 40 + padding;
        int highY = (int) (Constants.SIZE - (40 + (baguette.getWidth() * baguette.getScaleY()) + padding));
        int baguetteY = random.nextInt(highY - lowY) + lowY + 1;

        baguette.setPosX(baguetteX);
        baguette.setPosY(baguetteY);
        baguettes.add(baguette);
    }

    public void startGame() {
        pigeon = new Pigeon();
        baguettes.clear();
        displayText.clear();
        clickMove = false;
        secondsRemaining = Constants.GAME_TIME;
        baguetteMultiplier = 1.0d;
        loop.start();
    }

    public void stopGame(GraphicsContext gc) {
        loop.stop();
        hs.addScore(pigeon.getScore());
        pigeon = null;
        runTaskLater(this::createMenu, 2000L);

        gc.fillRoundRect(0, 250, Constants.SIZE, 100, 5.0, 5.0);
        gc.setFill(Color.RED);
        gc.setFont(Font.font(42));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("Game Over", Constants.SIZE / 2d, Constants.SIZE / 2d);
        resetGCSettings(gc);
    }

    public void resetGCSettings(GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
        gc.setFont(Font.font(26));
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
    }

    public void playSound(AudioClip media, double volume, double balance, double rate, double pan, int priority) {
        media.play(volume, balance, rate, pan, priority);
    }

    private void showText(String str, Paint fill, boolean center, double x, double y, long duration) {
        Text text = new Text(x, y, str);
        text.setFont(Font.font(26));
        text.setFill(fill);

        displayText.put(text, center);
        if (duration > 0) {
            runTaskLater(() -> displayText.remove(text), duration);
        }
    }

    public void topText(String str, Paint fill) {
        showText(str, fill, false, 170, 30, 1000L);
    }

    public void bottomText(String str, Paint fill, long duration) {
        showText(str, fill, true, Constants.SIZE / 2d, Constants.SIZE - 20, duration);
    }
}
