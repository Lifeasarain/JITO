package zju.defect;

import zju.defect.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Trainsettest {
    public static void main(String args[]){
//        File file = new File("/Users/lifeasarain/Desktop/trainsetTest.java");
        String content = "Hello World3";
        List<String> trainLineContent = new ArrayList<String>();
        trainLineContent.add(content);
        FileUtil.writeLinesToFile(trainLineContent, "/Users/lifeasarain/Desktop/trainsetTest.java");
    }
}
