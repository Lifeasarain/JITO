package zju.defect;

//import org.apache.commons.collections.BagUtils;
import zju.plugin.DataCenter;

import java.io.*;

import java.io.IOException;
import java.util.Properties;


public class Build {



    public String buildModel(String projectName, String projectPath){
        String result = null;
        try{

            String dataPath = projectPath.substring(0, projectPath.length()-projectName.length());
            String pythonenv = DataCenter.pythonEnv;
            String pythonpath = DataCenter.pythonProject+"/defect_features/build_model.py ";
            String pythonProjectPath = DataCenter.pythonProject;

            String pythonArgs = pythonenv + pythonpath + projectName + " "+pythonProjectPath +" "+ dataPath;
            System.out.println(pythonArgs);
            Process proc = Runtime.getRuntime().exec(pythonArgs);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                result = line;
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static void main(String agrs[]){
//        Build build = new Build();
//        String test = "/Users/lifeasarain/Desktop/JITO/JIT-Identification/defect_features";
//        System.out.print(test.substring(0,test.length()-15));
//        DataCenter.pythonEnv = "/opt/anaconda3/envs/tensorflow/bin/python3.7 ";
//        DataCenter.pythonProject = "/Users/lifeasarain/Desktop/JITO/JIT-Identification";
//        build.buildModel("druid","/Users/lifeasarain/IdeaProjects/druid");
//    }

}


