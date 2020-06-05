package zju.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import zju.defect.Sentence;


import java.io.File;
import java.util.Properties;

public class UnhighlightAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
//        Project project = e.getData(DataKeys.PROJECT);
        Project project = e.getProject();
        String fullPath = project.getProjectFilePath();
        String projectPath = fullPath.substring(0, fullPath.length() - 15);
        String projectName = projectPath.substring(projectPath.lastIndexOf("/")+1);


        try {
//            Properties props = new Properties();
//            props.load(this.getClass().getResourceAsStream("/server.properties"));
//            String storePath = props.getProperty("storepath");

            MyMarkupModel myMarkupModel = new MyMarkupModel();
            for (Sentence sentence : DataCenter.MarkedLine) {
                myMarkupModel.unhighlight(sentence.getFile(), project, sentence.getLineNumber());
            }



        }catch (Exception ex) {
            ex.printStackTrace();
            Messages.showMessageDialog("Error", "Error", Messages.getInformationIcon());
            return;
        }
    }
}
