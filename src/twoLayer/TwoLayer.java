package twoLayer;

import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;
import model.AbstractMultiLayer;
import model.Neuron;
import model.CorrectResponse;

/**
 * Two-layer perceptron
 *
 * @author tadaki
 */
public class TwoLayer extends AbstractMultiLayer {

    private List<Double> targetWeight;//Target weight vector
    private final boolean debug = false;

    public TwoLayer(int numInput, CorrectResponse answer,
            DoubleFunction<Double> function, Random myRandom) {
        super(numInput, answer, myRandom);
        List<Double> initialWeight = simpleWeight(numInput);
        if (!debug) {
            initialWeight = randamWeight(numInput);
        }
        responseNeuron = new Neuron("responce unit", initialWeight, function);
    }

    @Override
    public Double response(List<Double> input) {
        return responseNeuron.response(input);
    }

    @Override
    public void update(List<Double> input, double coeff) {
        double r = response(input);
        double rc = answer.apply(input);
        double c = r - rc;
        if (debug) {
            System.out.print(input);
            System.out.print(" " + r + " " + rc + " ");
            responseNeuron.update(input, c * coeff);
            responseNeuron.normalizeWeight();
            System.out.print(responseNeuron.getWeight());
            System.out.print(" ");
        } else {
            responseNeuron.update(input, c * coeff);
            responseNeuron.normalizeWeight();
        }
    }

    /**
     * Inner product between current and target weight vectors
     *
     * @return
     */
    public double deviation() {
        List<Double> weight = responseNeuron.getWeight();
        double c = 0.;
        for (int i = 0; i < weight.size(); i++) {
            c += weight.get(i) * targetWeight.get(i);
        }
        if (debug) {
            System.out.println(c);
        }
        return c;
    }

    public void setTargetWeight(List<Double> targetWeight) {
        this.targetWeight = targetWeight;
    }

    public List<Double> getWeight() {
        return responseNeuron.getWeight();
    }
}
