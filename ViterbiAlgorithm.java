import java.util.Arrays;
import java.util.List;

public class ViterbiAlgorithm {

	String states[];
	String observationVocab[];
	double[][] transitionMatrix;
	double emissionMatrix[][];

	public ViterbiAlgorithm(String[] states, String[] observationVocab, double[][] transitionMatrix,
			double[][] emissionMatrix) {
		// YOUR CODE HERE

		this.states = states;
		this.observationVocab = observationVocab;
		this.transitionMatrix = transitionMatrix;
		this.emissionMatrix = emissionMatrix;
	}

	public StateSequence getStateSequence(String[] observations) {

		double[][] viterbi = new double[states.length + 2][observations.length];
		int[][] backPointer = new int[states.length + 2][observations.length];
		List<String> observationVocabList = Arrays.asList(observationVocab);

		for (int s = 0; s < states.length; s++) {
			viterbi[s][0] = Math.log(transitionMatrix[0][s + 1])
					+ Math.log(emissionMatrix[s][observationVocabList.indexOf(observations[0])]);
		}

		for (int obs = 1; obs < observations.length; obs++) {
			// System.out.println("*******observation at "+obs+" is :
			// "+observations[obs]);
			for (int state = 0; state < states.length; state++) {
				// double temp = -Double.MAX_VALUE;
				// System.out.println("+++++++++The state is: " +states[state]);
				for (int state1 = 0; state1 < states.length; state1++) {
					// System.out.println("-----the previous state is "+state1);
					double temp1 = viterbi[state1][obs - 1] + Math.log(transitionMatrix[state1 + 1][state + 1])
							+ Math.log(emissionMatrix[state][observationVocabList.indexOf(observations[obs])]);

					// System.out.println("the value is: "+temp1);
					if(viterbi[state][obs]!=0){
					if (temp1 > viterbi[state][obs]) {
						backPointer[state][obs] = state1;
						// System.out.println("the backpointer is:
						// "+states[backPointer[state][obs]]);
						viterbi[state][obs] = temp1;
					}}
					else{
						backPointer[state][obs] = state1;
						viterbi[state][obs] = temp1;
					}

				}
			}
		}

		double temp = -Double.MAX_VALUE;

		// System.out.println("*****Final******");
		for (int state = 0; state < states.length; state++) {
			viterbi[states.length][observations.length - 1] = viterbi[state][observations.length - 1]
					+ Math.log(transitionMatrix[state + 1][states.length + 1]);
			// System.out.println("the state is: "+states[state]+" final value
			// is: "+viterbi[states.length][observations.length-1]);
			if (viterbi[states.length][observations.length - 1] > temp) {
				temp = viterbi[states.length][observations.length - 1];
				backPointer[states.length][observations.length - 1] = state;
				// System.out.println("back pointer new one is:
				// "+states[state]);
			}
		}
		int state = backPointer[states.length][observations.length - 1];
		String[] stateT = new String[observations.length + 1];
		stateT[observations.length] = states[state];
		for (int obs = observations.length - 1; obs > 0; obs--) {

			stateT[obs] = states[backPointer[state][obs]];
			state = backPointer[state][obs];
		}

		String[] str = new String[observations.length];
		for (int i = 0; i < str.length; i++) {
			str[i] = stateT[i + 1];
		}
		
		StateSequence seq = new StateSequence(str, temp);
		
		return seq;
	}}
