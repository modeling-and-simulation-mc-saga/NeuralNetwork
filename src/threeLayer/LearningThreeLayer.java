package threeLayer;

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
 *
 * @author tadaki
 */
public class LearningThreeLayer {

    private final String nl = System.getProperty("line.separator");
    private final CorrectResponse cResponce;
    private final int numInput;
    private final int numNeurons;
    private final ThreeLayer sys;
    private final Random random;

    public LearningThreeLayer(Random random, 
            DoubleFunction<Double> f, DoubleFunction<Double> g,
            DoubleFunction<Double> fd, DoubleFunction<Double> gd,
            int numInput, int numNeurons,CorrectResponse cResponce) {
        this.numInput = numInput;
        this.numNeurons = numNeurons;
        this.cResponce = cResponce;
        sys = new ThreeLayer(numInput, numNeurons,
                f, g, fd, gd, cResponce, random);
        this.random = random;
    }

    public List<Point2D.Double> learning(int maxT, double coeff) {
        List<Point2D.Double> data
                = Collections.synchronizedList(new ArrayList<>());
        for (int t = 0; t < maxT; t++) {
            sys.update(generateRandomInput(), coeff);
            double e = sys.getEnergy(Neuron.allInput(numInput));
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

    /**
     * Return the response from the system
     * @return 
     */
    public String getResponseStr() {
        return sys.getResponseStr();
    }

    /**
     * responce from the response unit
     * @return 
     */
    public String responceUnitStr(){
        Neuron responceUnit = sys.getResponseNeuron();
        return responceUnit.getAllResponseStr();
    }
    
    public String associativeUnitStr(){
        StringBuilder sb = new StringBuilder();
        List<Neuron> neurons = sys.getAssociateNeurons();
        for(Neuron n:neurons){
            if (!n.isFixOutput()){
                sb.append(n.toString()).append(nl);
                sb.append(n.getAllResponseStr()).append(nl);
            }
        }
        return sb.toString();
    }
    
    public static BufferedWriter openWriter(String filename)
            throws FileNotFoundException {
        FileOutputStream fStream = new FileOutputStream(new File(filename));
        return new BufferedWriter(new OutputStreamWriter(fStream));
    }

}
