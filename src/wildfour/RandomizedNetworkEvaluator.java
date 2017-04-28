package wildfour;

import java.util.Random;

import neural1.Network;

public class RandomizedNetworkEvaluator implements Evaluator {
		
	private final Network network;
	private static final Random RAND = new Random();
	private static final double RANDOMNESS = 0.01;

	public RandomizedNetworkEvaluator(Network network) {
		this.network = network;
	}

	@Override
	public double evaluate(PlayField field, int move) {
		double[] encoded;
		if (network.getInputCount() == 8) {
			encoded = field.encodeFeaturesAsNetworkInput();
		} else {
			encoded = field.encodeFieldAsNetworkInput();
		}
		return network.computeOutputs(encoded)[0] + RAND.nextDouble()*RANDOMNESS;
	}

}
