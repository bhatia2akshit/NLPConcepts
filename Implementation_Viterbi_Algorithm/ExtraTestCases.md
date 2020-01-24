# Extra TestCases that can be used:

## Viterbi Algorithm

### First Test Case

System.out.println("---------- Football manager example ----------");
states = new String[] { "VICTORY", "DRAW", "DEFEAT" };
transitionMatrix = new double[][] { { 0, 0.1, 0.5, 0.4, 0 }, { 0, 0.15, 0.4, 0.4, 0.05 },
        { 0, 0.3, 0.3, 0.3, 0.1 }, { 0, 0.2, 0.2, 0.4, 0.2 }, { 0, 0, 0, 0, 0 } };
observationVocab = new String[] { "CHAMPAGNE", "BEER", "COFFEE" };
emissionMatrix = new double[][] { { 0.8, 0.15, 0.05 }, { 0.1, 0.7, 0.2 }, { 0.1, 0.4, 0.5 } };

observations = new String[] { "COFFEE" };
expectedSequence = new ExpectedStateSequence(Math.log(0.4) + Math.log(0.5) + Math.log(0.2),
        new String[] { "DEFEAT" });
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);

observations = new String[] { "COFFEE", "COFFEE", "COFFEE" };
expectedSequence = new ExpectedStateSequence(
        Math.log(0.4) + 3 * Math.log(0.5) + 2 * Math.log(0.4) + Math.log(0.2),
        new String[] { "DEFEAT", "DEFEAT", "DEFEAT" });
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);

observations = new String[] { "CHAMPAGNE", "CHAMPAGNE", "CHAMPAGNE" };
expectedSequence = new ExpectedStateSequence(Math.log(0.000096), new String[] { "DRAW", "VICTORY", "DEFEAT" });
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);

observations = new String[] { "BEER", "COFFEE", "BEER", "COFFEE", "BEER", "COFFEE", "CHAMPAGNE" };
expectedSequence = new ExpectedStateSequence(
        Math.log(0.5) // start -> DRAW
        + Math.log(0.7) // DRAW -> BEER 
        + Math.log(0.3) // DRAW -> DEFEAT
        + 5 * Math.log(0.4) // 5 * DEFEAT -> DEFEAT
        + 3 * Math.log(0.5) // 3 * DEFEAT -> COFFEE
        + 2 * Math.log(0.4) // 2 * DEFEAT -> BEER
        + Math.log(0.1) // DEFEAT -> CHAMPAGNE
        + Math.log(0.2), // DEFEAT -> end
        new String[] { "DRAW", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT" },
        new String[] { "DRAW", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT", "VICTORY" });
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);




### Second Test Case


System.out.println("---------- Football manager example ----------");
observations = new String[1000];
Arrays.fill(observations, "CHAMPAGNE");
sequence = new String[1000];
Arrays.fill(sequence, "VICTORY");
sequence[0] = "DRAW";
sequence[sequence.length - 1] = "DEFEAT";
expectedSequence = new ExpectedStateSequence(
        Math.log(0.5) // start -> DRAW
        + Math.log(0.1) // DRAW -> CHAMPAGNE 
        + Math.log(0.3) // DRAW -> VICTORY
        + 997 * Math.log(0.15) // 997 * VICTORY -> VICTORY
        + 998 * Math.log(0.8) // 998 * VICTORY -> CHAMPAGNE
        + Math.log(0.4) // VICTORY -> DEFEAT
        + Math.log(0.1) // DEFEAT -> CHAMPAGNE
        + Math.log(0.2), // DEFEAT -> end
        sequence);
checkViterbi(states, transitionMatrix, observationVocab, emissionMatrix, observations, expectedSequence);








## Learning HMM From Annotated Data

### First Test Case

System.out.println("---------- Football manager example ----------");
testObservations = new String[][] { { "COFFEE" }, { "COFFEE", "COFFEE", "COFFEE" },
        { "CHAMPAGNE", "CHAMPAGNE", "CHAMPAGNE" },
        { "BEER", "COFFEE", "BEER", "COFFEE", "BEER", "COFFEE", "BEER" }, new String[1000] };
expectedSequences = new String[][] { { "DEFEAT" }, { "DEFEAT", "DEFEAT", "DEFEAT" },
        { "DRAW", "VICTORY", "DEFEAT" }, { "DRAW", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT", "DEFEAT" },
        new String[1000] };
Arrays.fill(testObservations[4], "CHAMPAGNE");
Arrays.fill(expectedSequences[4], "VICTORY");
expectedSequences[4][0] = "DRAW";
expectedSequences[4][expectedSequences[4].length - 1] = "DEFEAT";
checkHMMLearningViaViterbi("/srv/distribution/football-sequences.txt", 3, 3, testObservations, expectedSequences);


### Second Test Case

System.out.println("---------- Ice cream emission matrix smoothing example ----------");
/*
 * In this example, there are only 3_HOT tokens following each other or 1_COLD
 * tokens following each other (50 sequences per token comprising 50 tokens
 * each). There are two sequences "3_HOT 1_COLD" and "1_COLD 3_HOT". However,
 * with smoothing applied, the two example observation sequences have a higher
 * probability to be created by only HOT or only COLD states.
 */
testObservations = new String[][] { { "3", "3", "1", "3" }, { "1", "1", "1", "1", "3", "1" } };
expectedSequences = new String[][] { { "HOT", "HOT", "HOT", "HOT" },
        { "COLD", "COLD", "COLD", "COLD", "COLD", "COLD" } };
checkHMMLearningViaViterbi("/srv/distribution/icecream-sequences-emission-smoothing.txt", 2, 2, testObservations,
        expectedSequences);
        
       

