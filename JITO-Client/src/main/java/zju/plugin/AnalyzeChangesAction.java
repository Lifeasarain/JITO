package zju.plugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import zju.defect.DefectLocater;
import zju.defect.Sentence;
import zju.defect.util.GitUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class AnalyzeChangesAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
//        if (!Util.hasBuilt){
//            Messages.showMessageDialog("Build Model First", "Error", Messages.getInformationIcon());
//            return;
//        }

//        Project project = e.getData(DataKeys.PROJECT);
        Project project = e.getProject();
        String fullPath = project.getProjectFilePath();
        String projectPath = fullPath.substring(0, fullPath.length() - 15);
        String projectName = projectPath.substring(projectPath.lastIndexOf("/")+1);
        GitUtil gitUtil = new GitUtil();
        String commitHash = gitUtil.getLastCommit(projectPath);

        ProgressManager.getInstance().run(
                new Task.Modal(project, "Analyze The Change", true) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {

                        indicator.setFraction(0.1);
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
        DefectLocater defectLocater = new DefectLocater();
        try {
            if (defectLocater.analyzeProb(commitHash)) {
//                Messages.showMessageDialog(projectName, "Information", Messages.getInformationIcon());
                List<Sentence> sentences = defectLocater.modelRuning(projectPath, commitHash, 6, projectName);
                MyMarkupModel myMarkupModel = new MyMarkupModel();
                for (Sentence sentence : sentences) {
                    DataCenter.MarkedLine.add(sentence);
                    myMarkupModel.highlight(sentence.getFile(), project, sentence.getLineNumber());
                }
                Messages.showMessageDialog("Marked Sentences", "Information", Messages.getInformationIcon());
            }
            else{
                Messages.showMessageDialog("There is no bug", "Information", Messages.getInformationIcon());
            }
//            String result = defectLocater.analyzeProb(commitHash);
//            Messages.showMessageDialog(result, "Information", Messages.getInformationIcon());
        }catch (Exception ex){
            ex.printStackTrace();
            Messages.showMessageDialog(ex.getMessage(), "Error", Messages.getInformationIcon());
            return;
        }
    }
//    public static void main(String args[]){
//        DefectLocater defectLocater = new DefectLocater();
//
//        DataCenter.pythonProject = "/Users/lifeasarain/Desktop/JITO/JIT-Identification";
//        defectLocater.analyzeProb("a7844ab716eeeaddad24061dcf6224264444c67b");
//        try {
//            List<Sentence> sentences = defectLocater.modelRuning("/Users/lifeasarain/IdeaProjects/druid", "a7844ab716eeeaddad24061dcf6224264444c67b", 6, "druid");
//            for(Sentence s : sentences){
//                System.out.println(s.getFile().getPath());
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
////            Messages.showMessageDialog("Error", "Error", Messages.getInformationIcon());
//            return;
//        }
//    }
}
