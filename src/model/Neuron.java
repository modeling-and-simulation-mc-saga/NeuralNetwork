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

    private final String label;
    private final List<Double> weight;//最後の要素は閾値に相当
    private final DoubleFunction<Double> function;//応答関数
    private boolean fixOutput = false;//隠れ層用ダミーではtrue

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

    
    public String strResponse(List<Double> input) {
        Double r = response(input);
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (int i = 0; i < input.size() - 1; i++) {
            sj.add(String.valueOf(input.get(i)));
        }
        String str = label + sj.toString() + "=" + r;
        return str;
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

}
