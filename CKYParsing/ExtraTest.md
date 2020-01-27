**Part1:**
Map<String, String[][]> grammar = new HashMap<String, String[][]>();
Map<String, String[]> lexicon = new HashMap<>();
String expectedTable;
CKYRecognizer recognizer;

// Define the flight grammar
grammar.clear();
grammar.put("Nominal", new String[][] { { "Nominal", "Noun" }, { "Nominal", "PP" } });
grammar.put("NP", new String[][] { { "Det", "Nominal" } });
grammar.put("PP", new String[][] { { "Preposition", "NP" } });
grammar.put("S", new String[][] { { "NP", "VP" }, { "X1", "VP" }, { "Verb", "NP" }, { "X2", "PP" },
        { "Verb", "PP" }, { "VP", "PP" } });
grammar.put("VP", new String[][] { { "book", "include", "prefer" }, { "Verb", "NP" }, { "X2", "PP" },
        { "Verb", "PP" }, { "VP", "PP" } });
grammar.put("X1", new String[][] { { "Aux", "NP" } });
grammar.put("X2", new String[][] { { "Verb", "NP" } });

lexicon.clear();
lexicon.put("Aux", new String[] { "does" });
lexicon.put("Det", new String[] { "a", "an", "that", "this", "the" });
lexicon.put("Nominal", new String[] { "book", "elephant", "flight", "meal", "money", "pajamas" });
lexicon.put("Noun", new String[] { "book", "flight", "elephant", "meal", "money", "pajamas" });
lexicon.put("NP", new String[] { "i", "hustoun", "me", "my", "nwa", "she" });
lexicon.put("Preposition", new String[] { "from", "in", "near", "on", "through", "to" });
lexicon.put("S", new String[] { "book", "include", "prefer", "shot" });
lexicon.put("Verb", new String[] { "book", "include", "prefer", "shot" });
lexicon.put("VP", new String[] { "book", "include", "prefer", "shot" });

expectedTable = "[][NP][S][][S][][S][S]\n"
             + "[][][S, VP, Verb][][S, VP, X2][][S, VP, X2][S, VP, X2]\n"
             + "[][][][Det][NP][][NP][NP]\n"
             + "[][][][][Nominal, Noun][][Nominal][Nominal]\n"
             + "[][][][][][Preposition][PP][]\n"
             + "[][][][][][][NP][]\n"
             + "[][][][][][][][Nominal, Noun]\n"
             + "[][][][][][][][]\n";
checkParsingTable(recognizer, "i shot an elephant in my pajamas", expectedTable);
        
expectedTable = "[][NP][S][][S][][S][S]\n" 
             + "[][][Nominal, Noun, S, VP, Verb][][S, VP, X2][][S, VP, X2][S, VP, X2]\n"
             + "[][][][Det][NP][][NP][NP]\n" 
             + "[][][][][Nominal, Noun][][Nominal][Nominal]\n"
             + "[][][][][][Preposition][PP][]\n" 
             + "[][][][][][][NP][]\n" 
             + "[][][][][][][][Nominal, Noun]\n"
             + "[][][][][][][][]\n";
checkParsingTable(recognizer, "i book an elephant in my pajamas", expectedTable);

checkParsingTable(recognizer, "i shot a tiger in my pajamas", null);







**Part 2:**

Map<String, RightSide[]> grammar = new HashMap<>();
Map<String, RightSide[]> lexicon = new HashMap<>();
String expectedTree;
ProbabilisticCKYParser parser;

// Define the flight grammar
grammar.clear();
grammar.put("Nominal",
        new RightSide[] { new RightSide(0.2, "Nominal", "Noun"), new RightSide(0.05, "Nominal", "PP") });
grammar.put("NP", new RightSide[] { new RightSide(0.2, "Det", "Nominal"),
        new RightSide(0.03, "Nominal", "Noun"), new RightSide(0.0075, "Nominal", "PP") });
grammar.put("PP", new RightSide[] { new RightSide(1.0, "Preposition", "NP") });
grammar.put("S", new RightSide[] { new RightSide(0.8, "NP", "VP"), new RightSide(0.15, "X1", "VP"),
        // S → VP [.05] x VP → Verb NP [.20] = 0.01
        new RightSide(0.01, "Verb", "NP"),
        // S → VP [.05] x VP → Verb NP PP [.10] = 0.005
        new RightSide(0.005, "X2", "PP"),
        // S → VP [.05] x VP → Verb PP [.15]
        new RightSide(0.0075, "Verb", "PP"),
        // S → VP [.05] x VP → VP PP [.15]
        new RightSide(0.0075, "VP", "PP"),
        // S → VP [.05] x VP → VP → Verb NP NP [.05]
        new RightSide(0.0025, "X3", "PP"), });
grammar.put("VP", new RightSide[] { new RightSide(0.2, "Verb", "NP"), new RightSide(0.1, "X2", "PP"),
        new RightSide(0.15, "Verb", "PP"), new RightSide(0.15, "VP", "PP"), new RightSide(0.05, "X3", "PP") });
grammar.put("X1", new RightSide[] { new RightSide(1.0, "Aux", "NP") });
grammar.put("X2", new RightSide[] { new RightSide(1.0, "Verb", "NP") });
grammar.put("X3", new RightSide[] { new RightSide(1.0, "NP", "NP") });

lexicon.clear();
lexicon.put("Aux", new RightSide[] { new RightSide(0.4, "can"), new RightSide(0.6, "does") });
lexicon.put("Det",
        new RightSide[] { new RightSide(0.6, "a"), new RightSide(0.10, "that"), new RightSide(0.30, "the") });
// Nominal → Noun [.75] x Noun → book [.10] | flights [.30] | meal [.015] |
// money [.05] | flight [.40] | dinner [.10] | boat [.035]
lexicon.put("Nominal",
        new RightSide[] { new RightSide(0.075, "book"), new RightSide(0.225, "flights"),
                new RightSide(0.01125, "meal"), new RightSide(0.0375, "money"), new RightSide(0.3, "flight"),
                new RightSide(0.075, "dinner"), new RightSide(0.02625, "boat") });
lexicon.put("Noun",
        new RightSide[] { new RightSide(0.1, "book"), new RightSide(0.3, "flights"),
                new RightSide(0.015, "meal"), new RightSide(0.05, "money"), new RightSide(0.4, "flight"),
                new RightSide(0.1, "dinner"), new RightSide(0.035, "boat") });
// NP → Pronoun [.35] x Pronoun → I [.40] | she [.05] | me [.15] | you [.40]
lexicon.put("NP", new RightSide[] { new RightSide(0.14, "i"), new RightSide(0.0175, "she"),
        new RightSide(0.0525, "me"), new RightSide(0.14, "you"),
        // NP → Proper-Noun [.30] x Proper-Noun → Houston [.60] | NWA [.40]
        new RightSide(0.18, "houston"), new RightSide(0.12, "nwa"),
        // NP → Nominal [.15] x Nominal → Noun [.75] x Noun → book [.10] | flights [.30]
        // | meal [.015] | money [.05] | flight [.40] | dinner [.10] | boat [.035]
        new RightSide(0.01125, "book"), new RightSide(0.03375, "flights"), new RightSide(0.0016875, "meal"),
        new RightSide(0.005625, "money"), new RightSide(0.045, "flight"), new RightSide(0.01125, "dinner"),
        new RightSide(0.0039375, "boat") });
lexicon.put("Preposition", new RightSide[] { new RightSide(0.3, "from"), new RightSide(0.15, "near"),
        new RightSide(0.2, "on"), new RightSide(0.05, "through"), new RightSide(0.3, "to") });
// S → VP [.05] x VP → Verb [.35] x Verb → book [.30] | include [.30] | prefer
// [.40]
lexicon.put("S", new RightSide[] { new RightSide(0.00525, "book"), new RightSide(0.00525, "include"),
        new RightSide(0.007, "prefer") });
lexicon.put("Verb", new RightSide[] { new RightSide(0.3, "book"), new RightSide(0.3, "include"),
        new RightSide(0.4, "prefer") });
// VP → Verb [.35] x Verb → book [.30] | include [.30] | prefer [.40]
lexicon.put("VP", new RightSide[] { new RightSide(0.105, "book"), new RightSide(0.105, "include"),
        new RightSide(0.14, "prefer") });

parser = new ProbabilisticCKYParser("S", grammar, lexicon);

// Check a simple sentence
expectedTree = "[S[X1[Aux[does]NP[Det[the]Nominal[Nominal[flight]PP[Preposition[through]NP[houston]]]]]VP[Verb[include]NP[Det[a]Nominal[meal]]]]]";
checkParsingTree(parser, "does the flight through houston include a meal", expectedTree);

// Check a sentence with an unknown word
checkParsingTree(parser, "a bus to paris", null);

// Check a sentence with an unknown structure
checkParsingTree(parser, "a meal i include", "");




**Sec Example:**
StringBuilder builder = new StringBuilder();
StringBuilder sentenceBuilder = new StringBuilder();
int repeatitions = 120;
builder.append("[S[X1[Aux[does]NP[Det[that]Nominal[");
sentenceBuilder.append("does that flight ");
for (int i = 0; i < repeatitions; ++i) {
    builder.append("Nominal[");
    sentenceBuilder.append("near nwa ");
}
builder.append("flight");
for (int i = 0; i < repeatitions; ++i) {
    builder.append("]PP[Preposition[near]NP[nwa]]");
}
builder.append("]]]VP[Verb[include]NP[meal]]]]");
sentenceBuilder.append("include meal");
expectedTree = builder.toString();
checkParsingTree(parser, sentenceBuilder.toString(), expectedTree);



