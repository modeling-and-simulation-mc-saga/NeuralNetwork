package twoLayer;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;
import model.Neuron;
import model.CorrectResponse;

/**
 *
 * @author tadaki
 */
public class LearningOrGate {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        double c = 0.01;
        int max = 1000;
        Random random = new Random(34892L);

        //or gate
        int numInput = 3;
        CorrectResponse correctResponse = input -> {
            if (input.get(0) + input.get(1) > -1.) {
                return 1.;
            }
            return -1.;
        };
        List<Double> answerWeight = Arrays.asList(1., 1., 1.);
        Neuron.normalize(answerWeight);
        DoubleFunction<Double> responseFunction = x -> {
            if (x >= 0.) {
                return 1.;
            }
            return -1.;
        };

        LerningLogicGates or = new LerningLogicGates(
                numInput, correctResponse, answerWeight, responseFunction, random);
        List<Point2D.Double> plist = or.learning(max, c);
        try ( BufferedWriter out = LerningLogicGates.openWriter("orGate.txt")) {
            for (Point2D.Double p : plist) {
                String str = p.x + " " + p.y;
                out.write(str);
                out.newLine();
            }
        }
        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                List<Double> input = Arrays.asList((double) i, (double) j, 1.);
                double r = or.checkResponse(input);
                System.out.println(i + " " + j + ":" + (int) r);
            }
        }

    }

}
