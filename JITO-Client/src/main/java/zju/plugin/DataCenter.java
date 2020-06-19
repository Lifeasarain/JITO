package zju.plugin;

import zju.defect.Sentence;

import java.util.LinkedList;
import java.util.List;

public class DataCenter {
    // Python编译器位置
    public static String pythonEnv = "";
    // Python项目位置
    public static String pythonProject = "";
    // commit起始日期
    public static String startTime = "";
    // commit结束日期
    public static String endTime = "";
    // n-gram 训练集位置
    public static String trainSet = "";
    // 高亮比例
    public static double highlightRatio = 0.1;
    // 已高亮的代码
    public static List<Sentence> MarkedLine = new LinkedList<>();
    // 测试集位置
    public static String testSet = "";
    // 原始文件对应表
    public static String originalFile = "";

}
