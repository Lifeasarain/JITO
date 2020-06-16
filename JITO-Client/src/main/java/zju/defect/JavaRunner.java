package zju.defect;

import slp.core.counting.giga.GigaCounter;
import slp.core.lexing.Lexer;
import slp.core.lexing.LexerRunner;
import slp.core.lexing.code.JavaLexer;
//import slp.core.lexing.runners.LexerRunner;
import slp.core.modeling.Model;
//import slp.core.modeling.dynamic.CacheModel;
//import slp.core.modeling.dynamic.NestedModel;
import slp.core.modeling.ModelRunner;
import slp.core.modeling.mix.MixModel;
import slp.core.modeling.ngram.JMModel;
//import slp.core.modeling.runners.ModelRunner;
import slp.core.translating.Vocabulary;
import slp.core.util.Pair;
import slp.core.util.Util;

import zju.defect.util.CSV_handler;
import zju.defect.util.FileUtil;
import zju.plugin.DataCenter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaRunner {
    private static String repoPath;
    private static String trainSetPathJava;
    private static List<String> bugFiles;
    private static int N_gram;
    private static CSV_handler myCSV = new CSV_handler();
    private static boolean modelPerLine = true;
    private static boolean sentenceMarker = true;
    private static double highlightRatio = DataCenter.highlightRatio;
    private static double threshold = 0;

    public void Initilation(String repoPath, String trainSetPathJava, List<String> bugFiles, int ngramLength){
        this.repoPath = repoPath;
        this.trainSetPathJava = trainSetPathJava;
        this.bugFiles = bugFiles;
        this.N_gram = ngramLength;
    }

    public JavaRunner(){
        // TODO Auto-generated constructor stub
    }

    public List<Sentence> LocateModeling() throws IOException{

        List<String> bugFiles = this.bugFiles;
        File train = new File(trainSetPathJava);
        File test = new File(repoPath);

        // 1. Lexing
        //   a. Set up lexer using a JavaLexer
        //		- The second parameter informs it that we want to files as a block, not line by line

        // old
//        Lexer lexer = new JavaLexer();
//        LexerRunner lexerRunner = new LexerRunner(lexer, false);

        LexerRunner.setLexer(new JavaLexer());
        LexerRunner.perLine(modelPerLine);
        LexerRunner.addSentenceMarkers(sentenceMarker);
        LexerRunner.useExtension("java");


        ModelRunner.perLine(modelPerLine);
        ModelRunner.setNGramOrder(N_gram);
        Model model = new JMModel(new GigaCounter());
        ModelRunner.learn(model, train);
//        Stream<Pair<File, List<List<Double>>>> modeledFiles = ModelRunner.modelbug(model, test, bugFiles);
        Map<File, List<List<Double>>> stored = new HashMap<>();
        for(String bgf : bugFiles){
            File bugFile = new File(bgf);
            Stream<Pair<File, List<List<Double>>>> modeledFiles = ModelRunner.model(model, bugFile);
            List<Pair<File, List<List<Double>>>> modeledFilesList = modeledFiles.collect(Collectors.toList());
            for(int index = 0; index < modeledFilesList.size(); index ++){
                Pair<File, List<List<Double>>> thisPair = modeledFilesList.get(index);
                stored.put(thisPair.left, thisPair.right);
            }
        }


//        Map<File, List<List<Double>>> stored = modeledFiles.collect(Collectors.toMap(Pair::left, Pair::right));
//        List<Pair<File, List<List<Double>>>> result = modeledFiles.collect(Collectors.toList());
        List<Sentence> sentences = FileUtil.getSentence(stored);
        List<Sentence> sortedSentences = sentences.stream().sorted(Comparator.comparing(Sentence::getEntropy).reversed()).collect(Collectors.toList());
        List<Sentence> resultSentences = sortedSentences.subList(0, (new Double(sortedSentences.size()*highlightRatio)).intValue());

        return resultSentences;





        //   b. Since our data does not contain sentence markers (for the start and end of each file), add these here
        //		- The model will assume that these markers are present and always skip the first token when modeling


        // old
//        lexerRunner.setSentenceMarkers(true);
//        //   c. Only lex (and model) files that end with "java". See also 'setRegex'
//        lexerRunner.setExtension("java");




        // 2. Vocabulary:
        //    - For code, we typically make an empty vocabulary and let it be built while training.
        //    - Building it first using the default settings (no cut-off, don't close after building)
        //		should yield the same result, and may be useful if you want to write the vocabulary before training.
        //    - If interested, use: VocabularyRunner.build(lexerRunner, train);

        // old
//        Vocabulary vocabulary = new Vocabulary();

        // 3. Model
        //	  a. We will use an n-gram model with simple Jelinek-Mercer smoothing (works well for code)
        //		 - The n-gram order of 6 is used, which is also the standard
        //       - Let's use a GigaCounter (useful for large corpora) here as well; the nested model later on will copy this behavior.


        //old
//        Model model = new JMModel(6, new GigaCounter());

        //       - Then, add an ngram-cache component.
        //         * Order matters here; the last model in the mix gets the final say and thus the most importance

//        model = MixModel.standard(model, new CacheModel());

        //old

        //       - Finally, we can enable dynamic updating for the whole mixture (won't affect cache; it's dynamic by default)
//        model.setDynamic(true);
//        //	  c. We create a ModelRunner with this model and ask it to learn the train directory
//        //		 - This invokes Model.learn for each file, which is fine for n-gram models since these are count-based;
//        //         other model implementations may prefer to train in their own way.
//        ModelRunner modelRunner = new ModelRunner(model, lexerRunner, vocabulary);
//
//
//        modelRunner.learnDirectory(train);

        // 4. Running
        //    a. Finally, we model each file in 'test' recursively

        //old
//        Stream<Pair<File, List<List<Double>>>> modeledFiles = modelRunner.modelBugDirectory(test, bugFiles);
        //	  b. Retrieve overall entropy statistics using ModelRunner's convenience method

//        DoubleSummaryStatistics statistics = modelRunner.getStats(modeledFiles);
//        System.out.printf("Modeled %d tokens, average entropy:\t%.4f\n", statistics.getCount(), statistics.getAverage());

        //old
//        Map<File, List<List<Double>>> stored = modeledFiles.collect(Collectors.toMap(Pair::left, Pair::right));
//        List<Pair<File, List<List<Double>>>> result = modeledFiles.collect(Collectors.toList());

//        for(Pair<File, List<List<Double>>> pair : result){
//            System.out.println(pair.left());
//            System.out.println(pair.right());
//        }

        //old
//        List<Sentence> sentences = Util.getSentence(stored);
//        for(Sentence sentence : sentences){
//            System.out.println(sentence.getLineNumber());
//        }

        // old
//        List<Sentence> sortedSentences = sentences.stream().sorted(Comparator.comparing(Sentence::getEntropy).reversed()).collect(Collectors.toList());
//        List<Sentence> resultSentences = sortedSentences.subList(0, (new Double(sortedSentences.size()*0.1)).intValue());
//
//        return resultSentences;

//        System.out.println(sentences.stream().mapToDouble(Sentence::getEntropy).average());

    }


}
