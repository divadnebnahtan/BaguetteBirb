package me.aloidia.baguettebirb.sprites;

import me.aloidia.baguettebirb.Constants;
import me.aloidia.baguettebirb.Main;

import java.util.ArrayList;

public class Baguette extends Sprite {
    private Type type;

    public Baguette() {
        super();
        setType(Type.generateType());

        setImage(Constants.getBaguette(getType().getID()));
        setScaleX(Constants.BAGUETTE_SIZE);
        setScaleY(Constants.BAGUETTE_SIZE);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        NORMAL(0), // regular
        SPEED_BOOST(1), // red
        SICKNESS(2), // green
        TIME_BOOST(3), // light blue
        CLICK_MOVE(4), // dark blue
        FRENZY(5), // pink
        DECA(6); // purple

        private final int ID;

        Type(int ID) {
            this.ID = ID;
        }

        public static Type generateType() {
            ArrayList<Type> list = new ArrayList<>();
            for (Type type : Constants.TYPE_CHANCES.keySet()) {
                for (int i = 0; i < Constants.TYPE_CHANCES.get(type); i++) {
                    list.add(type);
                }
            }
            return list.get(Main.random.nextInt(list.size()));
        }

        public int getID() {
            return ID;
        }
    }
}

