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
    private static String testPath;
    private static List<String> bugFiles;
    private static int N_gram;
    private static CSV_handler myCSV = new CSV_handler();
    private static boolean modelPerLine = true;
    private static boolean sentenceMarker = true;
    private static double highlightRatio = DataCenter.highlightRatio;
    private static double threshold = 0;

    public void Initilation(String repoPath, String trainSetPathJava, List<String> bugFiles, int ngramLength, String testPath){
        this.repoPath = repoPath;
        this.trainSetPathJava = trainSetPathJava;
        this.bugFiles = bugFiles;
        this.N_gram = ngramLength;
        this.testPath = testPath;
    }

    public JavaRunner(){
        // TODO Auto-generated constructor stub
    }

    public List<Sentence> LocateModeling() throws IOException{

        List<String> bugFiles = this.bugFiles;
        File train = new File(trainSetPathJava);
        File test = new File(testPath);


        LexerRunner.setLexer(new JavaLexer());
        LexerRunner.perLine(modelPerLine);
        LexerRunner.addSentenceMarkers(sentenceMarker);
        LexerRunner.useExtension("java");


        ModelRunner.perLine(modelPerLine);
        ModelRunner.setNGramOrder(N_gram);
        Model model = new JMModel(new GigaCounter());
        ModelRunner.learn(model, train);
//        Stream<Pair<File, List<List<Double>>>> modeledFiles = ModelRunner.modelbug(model, test, bugFiles);
        Stream<Pair<File, List<List<Double>>>> modeledFiles = ModelRunner.model(model, test);
        Map<File, List<List<Double>>> stored = new HashMap<>();
        List<Pair<File, List<List<Double>>>> modeledFilesList = modeledFiles.collect(Collectors.toList());
        for(int index = 0; index < modeledFilesList.size(); index ++){
            Pair<File, List<List<Double>>> thisPair = modeledFilesList.get(index);
            stored.put(thisPair.left, thisPair.right);
        }

//        for(String bgf : bugFiles){
//            File bugFile = new File(bgf);
//            Stream<Pair<File, List<List<Double>>>> modeledFiles = ModelRunner.model(model, bugFile);
//            List<Pair<File, List<List<Double>>>> modeledFilesList = modeledFiles.collect(Collectors.toList());
//            for(int index = 0; index < modeledFilesList.size(); index ++){
//                Pair<File, List<List<Double>>> thisPair = modeledFilesList.get(index);
//                stored.put(thisPair.left, thisPair.right);
//            }
//        }


//        Map<File, List<List<Double>>> stored = modeledFiles.collect(Collectors.toMap(Pair::left, Pair::right));
//        List<Pair<File, List<List<Double>>>> result = modeledFiles.collect(Collectors.toList());
        List<Sentence> sentences = FileUtil.getSentence(stored);
        List<Sentence> sortedSentences = sentences.stream().sorted(Comparator.comparing(Sentence::getEntropy).reversed()).collect(Collectors.toList());
        List<Sentence> resultSentences = sortedSentences.subList(0, (new Double(sortedSentences.size()*highlightRatio)).intValue());

        return resultSentences;












    }


}
