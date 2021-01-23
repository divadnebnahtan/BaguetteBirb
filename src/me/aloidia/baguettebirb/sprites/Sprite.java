package me.aloidia.baguettebirb.sprites;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    private Image image;
    private double posX = 0.0, posY = 0.0;
    private double velX = 0.0, velY = 0.0;
    private double scaleX = 1.0, scaleY = 1.0;
    private double width = 0.0, height = 0.0;

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void addPos(double x, double y) {
        setPosX(getPosX() + x);
        setPosY(getPosY() + y);
    }

    public void addVel(double x, double y) {
        setVelX(getVelX() + x);
        setVelY(getVelY() + y);
    }

    public void setImage(Image image) {
        this.image = image;
        setWidth(image.getWidth());
        setHeight(image.getHeight());
    }

    public void update(double time) {
        addPos(getVelX() * time, getVelY() * time);
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, posX, posY, width * scaleX, height * scaleY);
    }

    public Rectangle2D getBounds() {
        double x2 = posX + (width * scaleX), y2 = posY + (height * scaleY);
        double x = Math.min(x2, getPosX()), y = Math.min(y2, getPosY());
        double w = Math.abs(getPosX() - x2), h = Math.abs(getPosY() - y2);
        return new Rectangle2D(x, y, w, h);
    }

    public boolean intersects(Sprite other) {
        return this.getBounds().intersects(other.getBounds());
    }}
