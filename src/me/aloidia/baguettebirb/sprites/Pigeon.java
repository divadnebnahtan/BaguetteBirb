package me.aloidia.baguettebirb.sprites;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.aloidia.baguettebirb.Constants;
import me.aloidia.baguettebirb.Main;

public class Pigeon extends Sprite {
    private double score = 0;
    private boolean isWalking = false;
    private boolean isLeft = false;
    private boolean peck = false;
    private double speedMultiplier = 1.0d;
    private double scoreMultiplier = 1.0d;

    public Pigeon() {
        super();
        setImage(Constants.getPigeon());
        setPosX((Constants.SIZE - getWidth() * Constants.PIGEON_SIZE) / 2);
        setPosY((Constants.SIZE - getHeight() * Constants.PIGEON_SIZE) / 2);
        setScaleX(Constants.PIGEON_SIZE);
        setScaleY(Constants.PIGEON_SIZE);
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    private double getScoreMultiplier() {
        return scoreMultiplier;
    }

    private void setScoreMultiplier(double scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public void setFacing(boolean left) {
        if (left != isLeft) {
            setScaleX(getScaleX() * -1);
            double translate = left ? getWidth() : -getWidth();
            addPos(translate * Constants.PIGEON_SIZE, 0);
            isLeft = left;
        }
    }

    public void setWalking(boolean walking) {
        if (walking != isWalking) {
            Image newImg = walking ? Constants.getPigeonWalking() : Constants.getPigeon();
            setImage(newImg);
            isWalking = walking;
        }
    }

    public void peck() {
        if (!peck) {
            isWalking = false;
            setImage(Constants.getPigeonPecking());
            addPos((isLeft ? 1 : -1) * 8 * Constants.PIGEON_SIZE, 0);
            peck = true;
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(900),
                            event -> {
                                setImage(Constants.getPigeon());
                                addPos((isLeft ? -1 : 1) * 8 * Constants.PIGEON_SIZE, 0);
                                peck = false;
                            }));
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            timeline.play();
        }
    }

    public boolean canWalk() {
        return !peck;
    }

    public double getScore() {
        return score;
    }

    public void score(int amount) {
        score += (amount * getScoreMultiplier());
    }

    public void consume(Baguette baguette, Main main) {
        switch (baguette.getType()) {
            case NORMAL:
                score(1);
                main.topText("+" + (1 * scoreMultiplier), Color.YELLOW);
                break;
            case SPEED_BOOST:
                setSpeedMultiplier(2.0d);
                Main.runTaskLater(() -> setSpeedMultiplier(1.0d), 5000L);
                score(1);
                main.bottomText("Speed Boost", Color.ORANGERED, 5000L);
                break;
            case SICKNESS:
                setScoreMultiplier(0.5d);
                Main.runTaskLater(() -> setScoreMultiplier(1.0d), 5000L);
                score(1);
                main.bottomText("Sickness! 0.5x Points", Color.YELLOWGREEN, 5000L);
                break;
            case TIME_BOOST: // finish showText
                main.secondsRemaining += 5;
                score(1);
                main.bottomText("+5 Seconds", Color.LIGHTBLUE, 1000L);
                break;
            case CLICK_MOVE:
                main.clickMove = true;
                Main.runTaskLater(() -> main.clickMove = false, 3000L);
                score(1);
                main.bottomText("Click to move", Color.BLUE, 3000L);
                break;
            case FRENZY:
                main.baguetteMultiplier = 5.0d;
                Main.runTaskLater(() -> main.baguetteMultiplier = 1.0d, 2000L);
                score(1);
                main.bottomText("FRENZY!!!", Color.HOTPINK, 2000L);
                break;
            case DECA:
                score(10);
                main.topText("+" + (10 * scoreMultiplier), Color.PURPLE);
                break;
        }
    }
}
