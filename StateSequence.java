class StateSequence {
	/**
	 * The sequence of states.
	 */
	public final String[] states;
	/**
	 * The logarithm of the probability of this sequence.
	 */
	public final double logProbability;

	public StateSequence(String[] states, double logProbability) {
		this.states = states;
		this.logProbability = logProbability;
	}
}
