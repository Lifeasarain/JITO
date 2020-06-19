package zju.defect;



import org.apache.commons.text.StringEscapeUtils;
//import slp.core.example.BasicJavaRunner;
import zju.defect.util.CSV_handler;
import zju.defect.util.FileUtil;
import zju.defect.util.GitUtil;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import com.google.gson.Gson;
import zju.plugin.DataCenter;


public class DefectLocater {
    public static int ngramLength = 6;
    private static GitUtil gitUtil = new GitUtil();




    public Boolean analyzeProb(String commitHash){
        String result = null;
        try{

            String pythonenv = DataCenter.pythonEnv;
            String pythonpath = DataCenter.pythonProject+"/defect_features/run_model.py ";
            String pythonProjectPath = DataCenter.pythonProject;

            String pythonArgs = pythonenv + pythonpath + commitHash+" "+pythonProjectPath;

            Process proc = Runtime.getRuntime().exec(pythonArgs);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                result = line;
            }
            in.close();
            proc.waitFor();

        }catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (result.equals("1"));
//        return result;

    }

    public List<Sentence> modelRuning(String repoPath, String commitHash, int ngramLength, String projectName) throws IOException {

        String inputContent = DataCenter.pythonProject+"/defect_features/utils/data_tmp";
        String trainSetPathJava = DataCenter.pythonProject + "/train/"+projectName+"Train.java";
        String testPath = DataCenter.pythonProject+"/train/javafile.java";


        List<String> bugFiles = gitUtil.getBugFilePath(repoPath, commitHash);
        getCleanLines(inputContent,trainSetPathJava, projectName);
        JavaRunner jr = new JavaRunner();
        jr.Initilation(repoPath, trainSetPathJava, bugFiles, ngramLength, testPath);
        List<Sentence> sentences = jr.LocateModeling();
        return sentences;
    }

    public void getCleanLines(String inputContent, String trainSetPathJava, String projetName)throws IOException {
        CSV_handler myCSV = new CSV_handler();
//        File file = new File(trainSetPathJava);
//        if (!file.exists()) {
//        file.getParentFile().mkdir();
//        }
        List<String> trainLineContent = new ArrayList<String>();
        String inputContentCsv = inputContent + "/" + projetName + "_a.csv";
        List<String[]> content = myCSV.getContentFromFile(new File(inputContentCsv));
        for (int i = 0; i < content.size(); i++) {
            String[] thisLine = content.get(i);
            String lineContent = thisLine[13];
            if ("".equals(lineContent) || "[]".equals(lineContent)) {
                continue;
            }
            String[] oneLine = lineContent.substring(2,lineContent.length()-2).split("\", \"");
            for(int j = 0; j < oneLine.length; j++){
                String codeLine = StringEscapeUtils.unescapeJava(oneLine[j]);
                if(codeLine.length() >= 10){

                    String preprocessedLineContent = FileUtil.PreprocessCode(codeLine.trim());
                    trainLineContent.add(preprocessedLineContent);
                }
            }
        }
        FileUtil.writeLinesToFile(trainLineContent, trainSetPathJava);
    }



//    public static void main(String[] args) throws IOException {
//        DefectLocater dfl = new DefectLocater();
////        List<Sentence> sentences = dfl.modelRuning("/Users/lifeasarain/IdeaProjects/druid", "a7844ab716eeeaddad24061dcf6224264444c67b", ngramLength, "druid");
////        for(Sentence sentence : sentences){
////            System.out.println(sentence.getLineNumber());
////        }
////        DataCenter.pythonEnv = "/opt/anaconda3/envs/tensorflow/bin/python3.7 ";
//        DataCenter.pythonProject = "/Users/lifeasarain/Desktop/tmp/JITO/JITO-Identification";
////        dfl.analyzeProb("a7844ab716eeeaddad24061dcf6224264444c67b");
//        List<Sentence> sentences = dfl.modelRuning("/Users/lifeasarain/IdeaProjects/druid", "a7844ab716eeeaddad24061dcf6224264444c67b", 6, "druid");
//        for (Sentence sentence : sentences) {
//            System.out.println(sentence.getEntropy());
//        }
//    }
}
