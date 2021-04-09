package threeLayer;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;
import model.*;

/**
 *
 * @author tadaki
 */
public class LearningXOR {

    private final String nl = System.getProperty("line.separator");
    private final CorrectResponse cResponce = input -> {
        if (input.get(0) + input.get(1) > 1.5) {
            return -1.;
        }
        if (input.get(0) + input.get(1) < -1.5) {
            return -1.;
        }
        return 1.;
    };
    private final int numInput = 3;
    private final int numNeurons = 3;
    private final ThreeLayer sys;
    private final Random random;

    public LearningXOR(Random random, double alpha) {
        DoubleFunction<Double> f
                = x -> (Math.exp(alpha * x) - Math.exp(-alpha * x))
                / (Math.exp(alpha * x) + Math.exp(-alpha * x));
        DoubleFunction<Double> fd
                = x -> 4 * alpha / (Math.exp(alpha * x) + Math.exp(-alpha * x))
                / (Math.exp(alpha * x) + Math.exp(-alpha * x));

        sys = new ThreeLayer(numInput, numNeurons,
                f, f, fd, fd, cResponce, random);
        this.random = random;
    }

    public List<Point2D.Double> learning(int maxT, double coeff) {
        List<Point2D.Double> data
                = Collections.synchronizedList(new ArrayList<>());
        for (int t = 0; t < maxT; t++) {
            sys.update(generateRandomInput(), coeff);
            double e = sys.getEnergy(allInput());
            data.add(new Point2D.Double(t, e));
        }
        return data;
    }

    private List<Double> generateRandomInput() {
        List<Double> data = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < numInput - 1; i++) {
            int k = 2 * random.nextInt(2) - 1;
            data.add((double) k);
        }
        data.add(1.);
        return data;
    }

    private List<List<Double>> allInput() {
        List<List<Double>> data = new ArrayList<>();
        data.add(Arrays.asList(-1., -1., 1.));
        data.add(Arrays.asList(1., -1., 1.));
        data.add(Arrays.asList(-1., 1., 1.));
        data.add(Arrays.asList(1., 1., 1.));
        return data;
    }

    public String getResponseStr() {
        List<List<Double>> data = allInput();
        StringBuilder sb = new StringBuilder();
        for (List<Double> inputData : data) {
            double r = sys.response(inputData);
            sb.append(inputData).append(":").append(r);
            sb.append(nl);
        }
        return sb.toString();
    }

    public static BufferedWriter openWriter(String filename)
            throws FileNotFoundException {
        FileOutputStream fStream = new FileOutputStream(new File(filename));
        return new BufferedWriter(new OutputStreamWriter(fStream));
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Random myRandom = new Random(98724L);
        double alpha = 1.;
        double c = 0.1;
        int max = 1000;
        LearningXOR sys = new LearningXOR(myRandom, alpha);
        List<Point2D.Double> plist = sys.learning(max, c);
        try ( BufferedWriter out = openWriter("learningXOR.txt")) {
            for (Point2D.Double p : plist) {
                String str = p.x + " " + p.y;
                out.write(str);
                out.newLine();
            }
        }

        System.out.println(sys.getResponseStr());
    }

}
