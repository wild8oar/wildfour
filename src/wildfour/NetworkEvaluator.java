package wildfour;

import neural1.Network;

public class NetworkEvaluator implements Evaluator {
	
	private final Network network;

	public NetworkEvaluator(Network network) {
		this.network = network;
	}

	@Override
	public double evaluate(PlayField field) {
		return network.computeOutputs(field.encodeAsNetworkInput())[0];
	}

}
