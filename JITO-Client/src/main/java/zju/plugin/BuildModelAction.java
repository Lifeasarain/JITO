package zju.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import zju.defect.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;


public class BuildModelAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

//        Project project = e.getData(DataKeys.PROJECT);
        Project project = e.getProject();
        String fullPath = project.getProjectFilePath();
        String projectPath = fullPath.substring(0, fullPath.length() - 15);
        String projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);


        Build build = new Build();
        ProgressManager.getInstance().run(
                new Task.Modal(project, "Build Model", true) {
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
        try {
            String result = build.buildModel(projectName, projectPath);
            Messages.showMessageDialog(result, "Information", Messages.getInformationIcon());
        }catch (Exception es){
            Messages.showMessageDialog(es.getMessage(), "Information", Messages.getInformationIcon());
        }





//    public static void main(String args[]){
//        DataCenter.pythonEnv = "/opt/anaconda3/envs/tensorflow/bin/python3.7 ";
//        DataCenter.pythonProject = "/Users/lifeasarain/Desktop/JITO/JIT-Identification";
//        Build build = new Build();
//        String result = null;
//        result = build.buildModel("druid", "/Users/lifeasarain/IdeaProjects/druid");
//        System.out.print(result);
//    }
    }
}
