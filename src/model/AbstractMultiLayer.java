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

    protected final int numInput;//固定入力も含む
    protected final CorrectResponse answer;//入力に対する正しい応答
    protected final Random random;
    protected Neuron responseNeuron;//応答層のニューロン

    public AbstractMultiLayer(int numInput, CorrectResponse answer, 
            Random random) {
        this.numInput = numInput;
        this.answer = answer;
        this.random = random;
    }

    /**
     * シミュレーション用に、重みの単純な初期値を作る
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
     * シミュレーション用に、ランダムな重みの初期値を作る
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
     * 出力ニューロンを返す
     * @return 
     */
    public Neuron getResponseNeuron() {
        return responseNeuron;
    }

    public String getResponseStr(){
        List<List<Double>> allInput = Neuron.allInput(numInput);
        StringBuilder sb=new StringBuilder();
        for(List<Double> input:allInput){
            double r = response(input);
            sb.append(input).append(":").append(String.format("%.2f",r));
            sb.append(nl);
        }
        return sb.toString();
    }
    
    /**
     * 入力に対して、最終的な出力を得る
     * @param input
     * @return 
     */
    public abstract Double response(List<Double> input);
    
    /**
     * 入力に対して、各層のニューロンの重みを更新する
     * @param input
     * @param coeff 
     */
    public abstract void update(List<Double> input, double coeff);
}
