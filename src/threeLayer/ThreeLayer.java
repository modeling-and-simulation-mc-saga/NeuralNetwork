package threeLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;
import model.AbstractMultiLayer;
import model.Neuron;
import model.CorrectResponse;

/**
 * 3 Layer Perceptron
 *
 * @author tadaki
 */
public class ThreeLayer extends AbstractMultiLayer {

    List<Neuron> associateNeurons;
    private final boolean debug = false;
    private final DoubleFunction<Double> fd;
    private final DoubleFunction<Double> gd;

    /**
     *
     * @param numInput The number of inputs including fixed one
     * @param numAssociateNeurons The number of associate neurons including fixed one
     * @param f Response function at assoicate layer
     * @param g Response function at output layer
     * @param fd Derivative of f
     * @param gd Derivative of g
     * @param answer
     * @param random
     */
    public ThreeLayer(int numInput, int numAssociateNeurons,
            DoubleFunction<Double> f, DoubleFunction<Double> g,
            DoubleFunction<Double> fd, DoubleFunction<Double> gd,
            CorrectResponse answer, Random random) {

        super(numInput, answer, random);

        //initialize response neuron
        List<Double> initialWeight = simpleWeight(numAssociateNeurons);
        if (!debug) {
            initialWeight = randamWeight(numAssociateNeurons);
        }
        responseNeuron = new Neuron("response unit", initialWeight, g);

        //initialize neurons in associate layer
        associateNeurons = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < numAssociateNeurons; i++) {
            List<Double> initialWeight2 = simpleWeight(numInput);
            if (!debug) {
                initialWeight2 = randamWeight(numInput);
            }
            Neuron neuron
                    = new Neuron("a" + String.valueOf(i), initialWeight2, f);
            if (i == numAssociateNeurons - 1) {
                neuron.setFixOutput(true);
            }
            associateNeurons.add(neuron);
        }

        this.fd = fd;
        this.gd = gd;
    }

    @Override
    public Double response(List<Double> input) {
        List<Double> aResponse = associateResponse(input);
        return responseNeuron.response(aResponse);
    }

    private List<Double> associateResponse(List<Double> input) {
        List<Double> aResponse = Collections.synchronizedList(new ArrayList<>());
        associateNeurons.forEach(n -> aResponse.add(n.response(input)));
        return aResponse;
    }

    @Override
    public void update(List<Double> input, double coeff) {
        double eta = response(input);
        double etac = answer.apply(input);
        double c = eta - etac;
        List<Double> sList = responseNeuron.getWeight();
        c = updateResponseUnit(input, coeff, c, sList);
        if (debug) {
            System.out.println(input + ":" + c);
        }
        updateAssociateUnit(input, coeff, c, sList);
    }

    /**
     * update neurons in assoicate layer
     *
     * @param input
     * @param coeff
     * @param c
     * @param sList
     */
    private void updateAssociateUnit(List<Double> input,
            double coeff, double c, List<Double> sList) {
        for (int k = 0; k < associateNeurons.size(); k++) {
            Neuron neuron = associateNeurons.get(k);
            if (!neuron.isFixOutput()) {
                double x = 0.;
                List<Double> w = neuron.getWeight();
                for (int i = 0; i < input.size(); i++) {
                    x += w.get(i) * input.get(i);
                }
                double cc = c * fd.apply(x) * sList.get(k);
                neuron.update(input, coeff * cc);
            }
        }
    }

    /**
     * update responseNeuron
     *
     * @param input
     * @param coeff
     * @param c
     * @return
     */
    private double updateResponseUnit(List<Double> input,
            double coeff, double c, List<Double> sList) {
        //assoiation unit
        List<Double> aResponse = associateResponse(input);
        double x = 0.;
        for (int k = 0; k < associateNeurons.size(); k++) {
            x += aResponse.get(k) * sList.get(k);
        }
//        System.out.println(aResponse+":"+sList+":"+x);
        c *= gd.apply(x);
        responseNeuron.update(aResponse, coeff * c);
        return c;
    }

    public double getEnergy(List<List<Double>> inputList) {
        double energy = 0.;
        for (List<Double> input : inputList) {
            double eta = response(input);
            double etac = answer.apply(input);
            double c = eta - etac;
            energy += c * c / 2.;
        }
        return energy / inputList.size();
    }

    public List<Neuron> getAssociateNeurons() {
        return associateNeurons;
    }

}
