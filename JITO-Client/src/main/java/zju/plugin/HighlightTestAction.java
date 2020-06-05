package zju.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import zju.defect.Sentence;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class HighlightTestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        System.out.println("1");
//        Project project = e.getData(DataKeys.PROJECT);
        Project project = e.getProject();
        String fullPath = project.getProjectFilePath();
        String projectPath = fullPath.substring(0, fullPath.length() - 15);
        String projectName = projectPath.substring(projectPath.lastIndexOf("/")+1);


        try {
            Properties props = new Properties();
            props.load(this.getClass().getResourceAsStream("/server.properties"));

            MyMarkupModel myMarkupModel = new MyMarkupModel();

            Sentence sentence = new Sentence();
            sentence.setEntropy(1);
            sentence.setLineNumber(30);
            sentence.setFile(new File("/Users/lifeasarain/IdeaProjects/druid/src/main/java/com/alibaba/druid/util/DruidDataSourceUtils.java"));
            List<Sentence> sentences = new LinkedList<>();
            sentences.add(sentence);

            Util util = new Util();
            for (Sentence testsentence : sentences) {
                DataCenter.MarkedLine.add(testsentence);
                String path = "/Users/lifeasarain/IdeaProjects/druid/src/main/java/com/alibaba/druid/util/DruidDataSourceUtils.java";
                File markFile = new File(path);
                myMarkupModel.highlight(markFile, project, testsentence.getLineNumber());
            }

            System.out.println(DataCenter.MarkedLine);

        }catch (Exception ex) {
            ex.printStackTrace();
            Messages.showMessageDialog("Error", "Error", Messages.getInformationIcon());
            return;
        }
    }
}
