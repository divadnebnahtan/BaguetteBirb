package me.aloidia.baguettebirb;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Highscore implements Serializable {
    private List<Double> scores = new ArrayList<>();

    public void addScore(double score) {
        scores.add(score);
        save();
    }

    public double getScore(int rank) { // Get the score at rank N
        update();
        try {
            return scores.get(rank - 1);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    private void update() {
        Collections.sort(scores);
        Collections.reverse(scores);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        for (int i = 1; i <= 10; i++) {
            ret.append(i).append(". ").append(getScore(i)).append("\n");
        }
        return ret.toString();
    }

    private void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream(Constants.HIGHSCORE_FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(this);

            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void load() {
        try {
            FileInputStream fileIn = new FileInputStream(Constants.HIGHSCORE_FILENAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            Highscore hs = (Highscore) in.readObject();
            this.scores = hs.scores;

            in.close();
            fileIn.close();
        } catch (FileNotFoundException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}