%maven org.junit.jupiter:junit-jupiter-api:5.3.1
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import java.util.Arrays;

/**
 * Simple structure representing a list of expected state sequences.
 * Note that in very rare cases more than one solution is possible.
 * This is why this class offers an array of state sequences.
 * It is not necessary to use this in the student's implementation!
 */
public static class ExpectedStateSequence {
    /**
     * An array of expected states (and their alternatives).
     */
    public final String[][] states;
    /**
     * The logarithm of the probability of this sequence.
     */
    public final double logProbability;

    public ExpectedStateSequence(double logProbability, String[]...states) {
        this.states = states;
        this.logProbability = logProbability;
    }
}

public static final double DELTA = 0.000001;

public static void checkViterbi(String[] states, double[][] transitionMatrix, String[] observationVocab,
        double[][] emissionMatrix, String[] observations, ExpectedStateSequence expectedSequence) {
    try {
        ViterbiAlgorithm viterbi = new ViterbiAlgorithm(states, observationVocab, transitionMatrix, emissionMatrix);
        long time1 = System.currentTimeMillis();
        StateSequence sequence = viterbi.getStateSequence(observations);
        time1 = System.currentTimeMillis() - time1;
        System.out.println("Viterbi took " + time1 + "ms");
        // Check whether the result state sequence matches one of the expected
        // sequences 
        int id = 0;
        while((id < expectedSequence.states.length) && (!Arrays.equals(sequence.states, expectedSequence.states[id]))) {
            ++id;
        }
        // If there is no expected squence that fits to the given result
        if(id >= expectedSequence.states.length) {
            StringBuilder message = new StringBuilder();
            message.append("The determined sequence ");
            message.append(Arrays.toString(sequence.states));
            message.append("\n does not match the expected state");
            if(expectedSequence.states.length > 1) {
                message.append("s ");
                for(int i = 0; i < expectedSequence.states.length; ++i) {
                    message.append('\n');
                    message.append(Arrays.toString(expectedSequence.states[i]));
                }
            } else {
                message.append(' ');
                message.append(Arrays.toString(expectedSequence.states[0]));
            }
            Assertions.fail(message.toString());
        }
        double diff = Math.abs(expectedSequence.logProbability - sequence.logProbability);
        Assertions.assertTrue(diff < DELTA, "The calculated probability (" + sequence.logProbability
                + ") does not match the expected probability (" + expectedSequence.logProbability + ").");
        System.out.println("Test passed");
    } catch (AssertionFailedError e) {
        throw e;
    } catch (Throwable e) {
        System.err.println("Your solution caused an unexpected error:");
        throw e;
    }
}

String observations[];
ExpectedStateSequence expectedSequence;
String[] states;
String[] sequence;
double[][] transitionMatrix;
String[] observationVocab;
double[][] emissionMatrix;

System.out.println("---------- Ice cream example ----------");
states = new String[] { "HOT", "COLD" };
transitionMatrix = new double[][] { { 0, 0.8, 0.2, 0 }, { 0, 0.6, 0.3, 0.1 }, { 0, 0.4, 0.5, 0.1 },
        { 0, 0, 0, 0 } };
observationVocab = new String[] { "1", "2", "3" };
emissionMatrix = new double[][] { { 0.2, 0.4, 0.4 }, { 0.5, 0.4, 0.1 } };
observations = new String[] { "3", "1", "3" };
expectedSequence = new ExpectedStateSequence(Math.log(0.0009216), new String[] { "HOT", "HOT", "HOT" });
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);

observations = new String[] { "3", "2", "1", "1" };
expectedSequence = new ExpectedStateSequence(Math.log(0.000288), new String[] { "HOT", "HOT", "COLD", "COLD" });
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);

observations = new String[] { "1", "3", "3", "2", "3", "2", "1", "3", "1", "1", "1" };
expectedSequence = new ExpectedStateSequence(Math.log(3.439853568E-9), 
        new String[] { "HOT", "HOT", "HOT", "HOT", "HOT", "HOT", "HOT", "HOT", "COLD", "COLD", "COLD" });
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);
