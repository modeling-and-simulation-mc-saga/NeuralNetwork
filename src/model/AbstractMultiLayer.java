package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author tadaki
 */
public abstract class AbstractMultiLayer {

    private final String nl = System.getProperty("line.separator");

    protected final int numInput;//number of inputs including fixed one
    protected final CorrectResponse answer;//correct response
    protected final Random random;
    protected Neuron responseNeuron;//The neuron for response

    public AbstractMultiLayer(int numInput, CorrectResponse answer,
            Random random) {
        this.numInput = numInput;
        this.answer = answer;
        this.random = random;
    }

    /**
     * Initialize weights
     *
     * @param numInput
     * @return
     */
    protected final List<Double> simpleWeight(int numInput) {
        List<Double> w
                = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < numInput; i++) {
            w.add(0.);
        }
        w.set(0, 1.);
        Neuron.normalize(w);
        return w;
    }

    /**
     * Initialize random weights
     *
     * @param numInput
     * @return
     */
    protected final List<Double> randamWeight(int numInput) {
        List<Double> w
                = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < numInput; i++) {
            w.add(random.nextDouble() - 0.5);
        }
        Neuron.normalize(w);
        return w;
    }

    /**
     * Return the response neuron
     *
     * @return
     */
    public Neuron getResponseNeuron() {
        return responseNeuron;
    }

    public String getResponseStr() {
        List<List<Double>> allInput = Neuron.allInput(numInput);
        StringBuilder sb = new StringBuilder();
        for (List<Double> input : allInput) {
            double r = response(input);
            sb.append(input).append(":");
            sb.append(String.format("%.2f", r));
            sb.append(nl);
        }
        return sb.toString();
    }

    /**
     * Return the final response corresponding to the input
     *
     * @param input
     * @return
     */
    public abstract Double response(List<Double> input);

    /**
     * Update weights corresponding to the inputs
     *
     * @param input
     * @param coeff
     */
    public abstract void update(List<Double> input, double coeff);
}
