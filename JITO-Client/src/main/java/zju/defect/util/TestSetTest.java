package zju.defect.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSetTest {
    public static void main(String args[]) throws IOException {
        File testSet = new File("/Users/lifeasarain/Desktop/tmp/JITO/JITO-Identification/train/test.txt");
        String content= FileUtils.readFileToString(testSet,"UTF-8");
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(content);
        JsonArray array = element.getAsJsonArray();
        ArrayList<Pair> originalFiles = new ArrayList<Pair>();
        for (int i = 0; i < array.size(); i++) {

            String pairStr = array.get(i).toString();
            String filePath = pairStr.substring(0, pairStr.indexOf(":"));
            filePath = filePath.substring(2, filePath.length() - 1);
            int lineNumber = Integer.parseInt(pairStr.substring(pairStr.indexOf(":") + 1, pairStr.length() - 1));
            Pair<String, Integer> pair = new Pair<>(filePath, lineNumber);
            originalFiles.add(pair);
            Integer.parseInt(originalFiles.get(i).getValue().toString());
            System.out.println(originalFiles.get(i).getValue().toString());
            System.out.println(originalFiles.get(i).getKey().toString());
//            File originalFile = new File(originalFiles.get(i).getKey().toString());
        }
    }
}
