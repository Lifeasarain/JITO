package zju.plugin;

import java.io.IOException;

public class Util {
    public static boolean hasBuilt = false;

    public String getProjectPath() throws IOException {
//        File directory = new File("");//参数为空
//        String courseFile = directory.getCanonicalPath();
//        System.out.println(courseFile);
//        return courseFile;
        String courseFile = System.getProperty("user.dir");
        System.out.println(courseFile);
        return courseFile;
    }

    public String getLocalFilePath(String remotePath, String storePath, String projectName, String localProjectPath){
//        String repoPath = "/home/qiufc/data";
//        String projectName = "druid";
//        String localProjectPath = "/Users/lifeasarain/IdeaProjects/druid";
        String localFilePath = localProjectPath+remotePath.replaceAll(storePath+projectName, "");
        return localFilePath;
    }



}
