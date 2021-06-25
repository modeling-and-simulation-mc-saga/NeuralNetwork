package threeLayer;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;
import model.*;

/**
 *
 * @author tadaki
 */
public class LearningXOR {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Random myRandom = new Random(98724L);
        double alpha = 1.;
        double c = 0.1;
        int max = 1000;
        int numInput = 3;
        int numNeuronInAssoicationLayer = 3;
        DoubleFunction<Double> f
                = x -> (Math.exp(alpha * x) - Math.exp(-alpha * x))
                / (Math.exp(alpha * x) + Math.exp(-alpha * x));
        DoubleFunction<Double> fd
                = x -> 4 * alpha / (Math.exp(alpha * x) + Math.exp(-alpha * x))
                / (Math.exp(alpha * x) + Math.exp(-alpha * x));
        CorrectResponse cResponce = input -> {
            if (input.get(0) + input.get(1) > 1.5) {
                return -1.;
            }
            if (input.get(0) + input.get(1) < -1.5) {
                return -1.;
            }
            return 1.;
        };
        LearningThreeLayer sys = new LearningThreeLayer(
                myRandom, f, f, fd, fd, numInput, numNeuronInAssoicationLayer, 
                cResponce);
        List<Point2D.Double> plist = sys.learning(max, c);
        try ( BufferedWriter out = 
                LearningThreeLayer.openWriter("learningXOR.txt")) {
            for (Point2D.Double p : plist) {
                String str = p.x + " " + p.y;
                out.write(str);
                out.newLine();
            }
        }

        System.out.println("responce");
        System.out.println(sys.getResponseStr());

        System.out.println("responce unit");
        System.out.println(sys.responceUnitStr());
        System.out.println("associative units");
        System.out.println(sys.associativeUnitStr());

    }

}
