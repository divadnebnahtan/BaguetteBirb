package me.aloidia.baguettebirb;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import me.aloidia.baguettebirb.sprites.Baguette;

public class GameLoop extends AnimationTimer {
    Main main;
    GraphicsContext gc;
    private long lastExecuted = 0;

    public GameLoop(Main main, GraphicsContext gc) {
        this.main = main;
        this.gc = gc;
    }

    @Override
    public void handle(long now) {
        gc.clearRect(0, 0, Constants.SIZE, Constants.SIZE);
        gc.drawImage(Constants.getBackground(), 0, 0, Constants.SIZE, Constants.SIZE);

        if (Math.random() < Constants.BAGUETTE_CHANCE * main.baguetteMultiplier) {
            main.spawnBaguette();
        }
        for (Baguette baguette : main.baguettes) {
            baguette.render(gc);
        }

        main.pigeon.setVelX(0.0d);
        main.pigeon.setVelY(0.0d);
        main.handlePresses();
        main.handleCollisions();
        main.handleReleases();
        main.pigeon.update(1);
        main.pigeon.render(gc);

        scorebar();
        renderDisplayText();

        if (now - lastExecuted >= 1_000_000_000) {
            main.secondsRemaining--;
            if (main.secondsRemaining < 0) {
                main.stopGame(gc);
            }
            lastExecuted = now;
        }

    }

    private void scorebar() {
        gc.fillRect(0, 0, Constants.SIZE, 40);
        gc.fillRect(0, Constants.SIZE - 40, Constants.SIZE, 40);
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + main.pigeon.getScore(), 20, 30);
        gc.fillText(String.format("Time Remaining = %02d:%02d", main.secondsRemaining / 60, main.secondsRemaining % 60), 300, 30);
        main.resetGCSettings(gc);
    }

    private void renderDisplayText() {
        for (Text text : main.displayText.keySet()) {
            gc.setFill(text.getFill());
            gc.setFont(text.getFont());
            if (main.displayText.get(text)) {
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
            }

            gc.fillText(text.getText(), text.getX(), text.getY());
            main.resetGCSettings(gc);
        }
    }
}
