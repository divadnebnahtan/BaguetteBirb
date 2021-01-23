package me.aloidia.baguettebirb;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import me.aloidia.baguettebirb.sprites.Baguette;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Constants {
    public static final String TITLE = "Baguette Birb";
    public static final int SIZE = 600;
    public static final String HIGHSCORE_FILENAME = "scores.ser";
    public static final List<String> WALKING_KEYS = Arrays.asList("W", "S", "A", "D");

    public static final double PIGEON_SIZE = 1.5d;
    public static final double BAGUETTE_SIZE = 1.0d;

    public static final double PIGEON_SPEED = 4.0d;
    public static final int GAME_TIME = 30;
    public static final double BAGUETTE_CHANCE = 0.02d;

    public static final Map<Baguette.Type, Integer> TYPE_CHANCES = new HashMap<Baguette.Type, Integer>() {{
        put(Baguette.Type.NORMAL, 12);
        put(Baguette.Type.SPEED_BOOST, 1);
        put(Baguette.Type.SICKNESS, 1);
        put(Baguette.Type.TIME_BOOST, 1);
        put(Baguette.Type.CLICK_MOVE, 1);
        put(Baguette.Type.FRENZY, 2);
        put(Baguette.Type.DECA, 1);
    }};


    public static Image getPigeon() {
        return new Image(Constants.class.getResource("assets/images/pigeon.png").toExternalForm());
    }

    public static Image getPigeonWalking() {
        return new Image(Constants.class.getResource("assets/images/pigeon_walking.gif").toExternalForm());
    }

    public static Image getPigeonPecking() {
        return new Image(Constants.class.getResource("assets/images/pigeon_pecking.gif").toExternalForm());
    }

    public static Image getBaguette(int num) {
        return new Image(Constants.class.getResource("assets/images/baguette" + num + ".png").toExternalForm());
    }

    public static Image getBackground() {
        return new Image(Constants.class.getResource("assets/images/background.png").toExternalForm());
    }

    public static AudioClip getEatingSFX() {
        return new AudioClip(Constants.class.getResource("assets/sounds/eat.mp3").toExternalForm());
    }

    public static String styleSheet() {
        return Constants.class.getResource("assets/theme.css").toExternalForm();
    }
}
