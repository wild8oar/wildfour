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
	public double evaluate(PlayField field) {
		return network.computeOutputs(field.encodeAsNetworkInput())[0] + RAND.nextDouble()*RANDOMNESS;
	}

}
