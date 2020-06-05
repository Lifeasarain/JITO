//package main;
//
//import com.intellij.lang.annotation.AnnotationHolder;
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.DataKeys;
//import com.intellij.openapi.progress.ProgressIndicator;
//import com.intellij.openapi.progress.ProgressManager;
//import com.intellij.openapi.progress.Task;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.ui.Messages;
//import com.intellij.psi.PsiElement;
//import com.intellij.psi.PsiFile;
//import FtpUtil;
//import HttpClientPool;
//import Util;
//import MyMarkupModel;
//import  com.intellij.openapi.editor.impl.DocumentMarkupModel;
//import org.jetbrains.annotations.NotNull;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Properties;
//
//public class TestAction extends AnAction {
//
//    @Override
//    public void actionPerformed(AnActionEvent e) {
//        // TODO: insert action logic here
////        MyMarkupModel myMarkupModel = new MyMarkupModel();
////        Project project = e.getData(DataKeys.PROJECT);
////        File file1 = new File("/Users/lifeasarain/IdeaProjects/JIT/src/main/java/zju/defect/DefectLocater.java");
////        File file2 = new File("/Users/lifeasarain/IdeaProjects/JIT/src/main/java/zju/defect/JavaRunner.java");
////        myMarkupModel.highlight(file1, project, 15);
////        myMarkupModel.highlight(file1, project, 16);
////        myMarkupModel.highlight(file2, project, 50);
//        Project project = e.getData(DataKeys.PROJECT);
//        String fullPath = project.getProjectFilePath();
//        String projectPath = fullPath.substring(0, fullPath.length() - 15);
//        String projectName = projectPath.substring(projectPath.lastIndexOf("/")+1);
//        ProgressManager.getInstance().run(
//                new Task.Modal(project, "Build Model", true) {
//                    @Override
//                    public void run(@NotNull ProgressIndicator indicator) {
//
//                        indicator.setFraction(0.1);
//                        try {
//                            Thread.sleep(700);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//        try {
//            Properties props = new Properties();
//            props.load(this.getClass().getResourceAsStream("/server.properties"));
//            String serverAddr = props.getProperty("server-address");
//
//
//            //上传整个工程文件
//            FtpUtil ftp=new FtpUtil(props.getProperty("server-address"), 21,props.getProperty("ftpuser"), props.getProperty("ftppassword"));
//            ftp.ftpLogin();
//            boolean uploadFlag = ftp.uploadFiles(projectPath, props.getProperty("storepath")+projectName);
////            if(uploadFlag) {
////                Messages.showMessageDialog("success", "Information", Messages.getInformationIcon());
////            }
//            if(uploadFlag){
//                String result = null;
//                result = HttpClientPool.getHttpClient().post("http://"+ serverAddr + ":9981/test/buildmodel", projectName);
//                Messages.showMessageDialog(result, "Information", Messages.getInformationIcon());
//            }
//            else{
//                Messages.showMessageDialog("Upload project failed", "Information", Messages.getInformationIcon());
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Messages.showMessageDialog("Failed to connect to server.", "Information", Messages.getInformationIcon());
//            return;
//        }
//
//    }
//}
