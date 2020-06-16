package zju.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import zju.defect.Build;


public class BuildModelAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

//        Project project = e.getData(DataKeys.PROJECT);
        Project project = e.getProject();
        String fullPath = project.getProjectFilePath();
        String projectPath = fullPath.substring(0, fullPath.length() - 15);
        String projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);



        ProgressManager.getInstance().run(
                new Task.Backgroundable(project, "Model Building", true) {
                    public void run(@NotNull ProgressIndicator indicator) {
                        indicator.setFraction(0.1);
                        Build build = new Build();
                        try {
                            String result = build.buildModel(projectName, projectPath);
                            indicator.setFraction(1.0);
                            indicator.setText("Model Build Success");
                            ProjectNotifier projectNotifier = new ProjectNotifier();
                            projectNotifier.notify("Model Build Succuess");
//                            Messages.showMessageDialog(result, "Information", Messages.getInformationIcon());
                        }catch (Exception es){
//                            Messages.showMessageDialog(es.getMessage(), "Information", Messages.getInformationIcon());
                        }
                    }
                });
//        Messages.showMessageDialog("Model Build Success", "Information", Messages.getInformationIcon());


//        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
//            public void run() {
//                ApplicationManager.getApplication().runReadAction(new Runnable() {
//                    public void run() {
//                        // do whatever you need to do
//                        Build build = new Build();
//                        try {
//                            String result = build.buildModel(projectName, projectPath);
////                            Messages.showMessageDialog(result, "Information", Messages.getInformationIcon());
//                        }catch (Exception es){
////                            Messages.showMessageDialog(es.getMessage(), "Information", Messages.getInformationIcon());
//                        }
//
//                    }
//                });
//            }
//        });










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
