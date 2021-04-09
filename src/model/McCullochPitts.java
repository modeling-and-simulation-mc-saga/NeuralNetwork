package model;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * McCulloch-Pitts model
 * 
 * @author tadaki
 */
public class McCullochPitts {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DoubleFunction<Double> step = x -> {
            if (x >= 0.) {
                return 1.;
            }
            return 0.;
        };
        
        //Define not, and, or gates
        List<Double> dnot = Arrays.asList(-1. - 0.5);
        Neuron not = new Neuron("No", dnot, step);
        List<Double> dand = Arrays.asList(1., 1., -1.5);
        Neuron and = new Neuron("And", dand, step);
        List<Double> dor = Arrays.asList(1., 1., -0.5);
        Neuron or = new Neuron("Or", dor, step);

        System.out.println("Test of Not");
        for (int i = 0; i < 2; i++) {
            List<Double> x = Arrays.asList((double) i, 1.);
            double out = not.response(x);
            System.out.println(i + " " + out);
        }
        
        System.out.println("Test of And and Or");
        System.out.println("i j and or");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                List<Double> x = Arrays.asList((double) i, (double) j, 1.);
                double a = and.response(x);
                double b = or.response(x);
                System.out.println(i + " " + j + " " + a + " " + b);
            }
        }
    }

}
