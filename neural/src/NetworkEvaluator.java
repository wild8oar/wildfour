package wildfour;

import neural1.Network;

public class NetworkEvaluator implements Evaluator {
	
	private final Network network;

	public NetworkEvaluator(Network network) {
		this.network = network;
	}

	@Override
	public double evaluate(PlayField field, int move) {
		return network.computeOutputs(field.encodeFieldAsNetworkInput())[0];
	}

}
