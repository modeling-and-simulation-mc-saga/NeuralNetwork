package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.DoubleFunction;

/**
 * model neuron
 *
 * @author tadaki
 */
public class Neuron {

    private final String nl = System.getProperty("line.separator");

    private final String label;
    private final List<Double> weight;//the last elements is the threshold
    private final DoubleFunction<Double> function;//responce function
    private boolean fixOutput = false;//true corresponding to input of the threshold

    /**
     *
     * @param label
     * @param weight
     * @param function
     */
    public Neuron(String label, List<Double> weight,
            DoubleFunction<Double> function) {
        this.label = label;
        this.weight = weight;
        this.function = function;
    }

    /**
     * Response to input
     *
     * @param input
     * @return
     */
    public Double response(List<Double> input) {
        if (fixOutput) {
            return 1.;
        }
        double x = 0;
        for (int i = 0; i < weight.size(); i++) {
            x += weight.get(i) * input.get(i);
        }
        return function.apply(x);
    }

    /**
     * return string output to the input
     *
     * @param input
     * @return
     */
    public String strResponse(List<Double> input) {
        Double r = response(input);
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (int i = 0; i < input.size() - 1; i++) {
            sj.add(String.valueOf(input.get(i)));
        }
        String str = label + sj.toString() + "=" + r;
        return str;
    }

    /**
     * Return all possible output
     *
     * @return
     */
    public String getAllResponseStr() {
        List<List<Double>> data = allInput(weight.size());
        StringBuilder sb = new StringBuilder();
        for (List<Double> inputData : data) {
            double r = response(inputData);
            sb.append(inputData).append(":");
            sb.append(String.format("%.2f", r));
            sb.append(nl);
        }
        return sb.toString();
    }

    public void normalizeWeight() {
        normalize(weight);
    }

    /**
     * Normalize vector
     *
     * @param vector
     */
    public static void normalize(List<Double> vector) {
        double a = 0.;
        for (int i = 0; i < vector.size(); i++) {
            double w = vector.get(i);
            a += w * w;
        }
        a = Math.sqrt(a);
        for (int i = 0; i < vector.size(); i++) {
            double w = vector.get(i);
            vector.set(i, w / a);
        }

    }

    /**
     * Update weights responding to input
     *
     * @param input
     * @param coeff
     */
    public void update(List<Double> input, double coeff) {
        if (fixOutput) {
            return;
        }
        for (int i = 0; i < weight.size(); i++) {
            double w = weight.get(i) - coeff * input.get(i);
            weight.set(i, w);
        }
    }

    /********* setters and getters *****************/
    
    
    public List<Double> getWeight() {
        List<Double> list = Collections.synchronizedList(new ArrayList<>());
        weight.forEach(w -> list.add(w));
        return list;
    }

    public void setFixOutput(boolean fixOutput) {
        this.fixOutput = fixOutput;
    }

    public boolean isFixOutput() {
        return fixOutput;
    }

    /**
     * Generate all possible input patters
     *
     * @param n
     * @return
     */
    public static List<List<Double>> allInput(int n) {
        List<List<Double>> inputList = 
                Collections.synchronizedList(new ArrayList<>());
        List<Double> list = new ArrayList<>();
        createInputList(0, list, inputList, n);
        return inputList;
    }

    private static void createInputList(int k, List<Double> list,
            List<List<Double>> inputList, int n) {
        if (k > n - 2) {
            list.add(1.);
            inputList.add(list);
            return;
        }
        List<Double> newList = new ArrayList<>(list);
        newList.add(1.);
        createInputList(k + 1, newList, inputList, n);
        newList = new ArrayList<>(list);
        newList.add(-1.);
        createInputList(k + 1, newList, inputList, n);
    }

}
