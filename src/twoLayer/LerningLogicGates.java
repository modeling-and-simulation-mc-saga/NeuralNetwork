package twoLayer;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;
import model.*;

/**
 * Learning logical gates by two-layer perceptron
 *
 * @author tadaki
 */
public class LerningLogicGates {

    private final int numInput;//2 for NOT gate and 3 for AND and OR gates
    private final Random random;
    private final TwoLayer sys;

    public LerningLogicGates(int numInput, CorrectResponse answer,
            List<Double> answerWeight, DoubleFunction<Double> responseFunction,
            Random random) {
        this.numInput = numInput;
        this.random = random;
        sys = new TwoLayer(numInput, answer, responseFunction, random);
        sys.setTargetWeight(answerWeight);
    }

    public List<Point2D.Double> learning(int tmax, double c) {
        List<Point2D.Double> plist
                = Collections.synchronizedList(new ArrayList<>());

        for (int t = 0; t < tmax; t++) {
            double m = oneLearningStep(c);
            plist.add(new Point2D.Double((double) t, m));
        }
        return plist;
    }

    private double oneLearningStep(double c) {
        List<Double> input = new ArrayList<>();
        for (int i = 0; i < numInput - 1; i++) {
            int k = 2 * random.nextInt(2) - 1;
            input.add((double) k);
        }
        input.add(1.);
        sys.update(input, c);
        return sys.deviation();
    }

    public Double checkResponse(List<Double> input) {
        return sys.response(input);
    }

    public static BufferedWriter openWriter(String filename)
            throws FileNotFoundException {
        FileOutputStream fStream = new FileOutputStream(new File(filename));
        return new BufferedWriter(new OutputStreamWriter(fStream));
    }

}
