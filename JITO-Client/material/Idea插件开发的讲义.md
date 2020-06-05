## Idea插件开发的讲义

[TOC]

### 前言

来这里找到志同道合的伙伴。

### 使用DevKit插件开发流程

1. 搭建开发环境
2. 创建一个插件项目
3. **创建动作**(插件具体内容的开发)
4. 运行和调试插件
5. 部署插件
6. 发布插件

#### 环境准备

1.  安装IDEA
2. 在IDEA中**Plugin DevKit**，为开发IDEA插件提供支持
3. 配置**IntelliJ Platform Plugin SDK**

#### 创建一个插件项目

1. file->new Project
     ![创建插件工程01](img\01.png)

2. 填写工程名称
     ![02](img\02.png)

3. 工程创建成功，工程目录如下，plugin.xml是核心配置文件
     ![03](img\03.png)

4. 核心配置文件说明

   ```xml
   <idea-plugin>
       
     <!-- 插件唯一id，不能和其他插件项目重复，所以推荐使用com.xxx.xxx的格式
          插件不同版本之间不能更改，若没有指定，则与插件名称相同 -->
     <id>com.your.company.unique.plugin.id</id>
      
     <!-- 插件名称，别人在官方插件库搜索你的插件时使用的名称 -->
     <name>Plugin display name here</name>
     
     <!-- 插件版本号 -->
     <version>1.0</version>
       
     <!-- 供应商主页和email（不能使用默认值，必须修改成自己的）-->
     <vendor email="support@yourcompany.com" url="http://www.yourcompany.com">YourCompany</vendor>
     <!-- 插件的描述 （不能使用默认值，必须修改成自己的。并且需要大于40个字符）-->
     <description><![CDATA[
         Enter short description for your plugin here.<br>
         <em>most HTML tags may be used</em>
       ]]></description>
     <!-- 插件版本变更信息，支持HTML标签；
          将展示在 settings | Plugins 对话框和插件仓库的Web页面 -->
     <change-notes><![CDATA[
         Add change notes here.<br>
         <em>most HTML tags may be used</em>
       ]]>
     </change-notes>
   
    <!-- 插件兼容IDEAbuild 号-->
     <idea-version since-build="173.0"/>
   
     <!-- 插件所依赖的其他插件的id -->
     <depends>com.intellij.modules.platform</depends>
   
     <extensions defaultExtensionNs="com.intellij">
     <!-- 声明该插件对IDEA core或其他插件的扩展 -->
     </extensions>
   
     <!-- 编写插件动作 -->
     <actions>
     </actions>
   
   </idea-plugin>
   ```
#### 创建一个动作action 
1. 创建action

  ![04](img\04.png)
2. 填写相关参数
- ①  action的基本信息，其中Name属性的值作为将来菜单的文本内容
- ②  作为Tools菜单下的子菜单
- ③  子菜单位置放在第一个
- ④  为子菜单添加快捷键
![05](img\05.png)

3. 编写点击菜单的通知内容

   ```java
   /**
    * 通过Pulgins Devkit创建的action继承了Ananction
    *
    */
   public class TestAction extends AnAction {
   
   
       /**
        * 需要实现点击事件发生之后的抽象方法
        */
       @Override
       public void actionPerformed(AnActionEvent e) {
           NotificationGroup notificationGroup = new NotificationGroup("testid", NotificationDisplayType.BALLOON, false);
           /**
            * content :  通知内容
            * type  ：通知的类型，warning,info,error
            */
           Notification notification = notificationGroup.createNotification("测试通知", MessageType.INFO);
           Notifications.Bus.notify(notification);
       }
   }
   ```
#### 运行和调试插件

1. 和我正常调试java代码一样，也可以在需要的位置打上断点。

![07](img\07.png)
2. 运行结果
![08](img\08.png)

#### 部署插件
1. 打包
![09](img\09.png)
2. 部署（从硬盘选择安装文件的方式）
![10](img\10.png)

#### 发布插件

 注册idea账号访问<https://plugins.jetbrains.com/author/me/>
1. 登录插件库
![11](img\11.png)
2. 选择打包好的插件，进行上传，等待审核结果。一般需要2-3个工作日出结果。如果成功了，别人就可以在线搜索咱们开发的插件了。
![12](img\12.png)



### 小试牛刀之毒鸡汤插件

#### 需求

##### 需求描述

- 在idea启动的时候，弹出对话框，展示一碗毒鸡汤。当点击再干一碗的时候，我们切换内容。

![13](img\13.png)

##### 需求分析

1. 怎么抓住idea启动的这个时间点？
2. 如何显示一个对话框？
3. 怎么添加按钮的点击事件？
4. 毒鸡汤内容的来源？

#### 代码编写
##### Components组件

| 组件类型             | 描述                                                         | 接口                 | plugins.xml加载配置元素 |
| -------------------- | ------------------------------------------------------------ | -------------------- | ----------------------- |
| ApplicationComponent | 在IDEA启动的时候初始化，整个IDEA中只有一个实例。             | ApplicationComponent | <application-componens> |
| ProjectComponent     | IDEA会为每一个Project实例创建对应级别的Component             | ProjectComponent     | <project-componens>     |
| ModuleComponent      | IDEA会为每一个已经加载的Project中的每一个模块（Module）创建Module级别的Component | ModuleComponent      | <module-componens>      |

![14](img\14.png)

Application 级别的 components 在 IDEA 启动时加载    初始化：调用 `initComponent()` 方法。所以我们覆写`initComponent()`方法，找到idea启动的时点。

##### 弹出对话框

翻阅官方文档<https://www.jetbrains.org/intellij/sdk/docs/user_interface_components/dialog_wrapper.html>

```java
package icu.jogeen.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * 对话框类,继承IDEA的DialogWrapper
 */
public class TuantDialog extends DialogWrapper {


    public TuantDialog() {
        super(true);
        init();//初始化dialog
        setTitle("每天一碗毒鸡汤");//设置对话框标题标题
    }

    /**
     * 创建对话框中间的内容面板
     * @return
     */
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        //创建一个面板，设置其布局为边界布局
        JPanel centerPanel = new JPanel(new BorderLayout());
        //创建一个文字标签，来承载内容
        JLabel label = new JLabel("毒鸡汤的内容");
        //设置首先大小
        label.setPreferredSize(new Dimension(100,100));
        //将文字标签添加的面板的正中间
        centerPanel.add(label,BorderLayout.CENTER);
        return centerPanel;
    }
}

```

![15](img\15.png)



##### 自定义按钮，并添加点击事件

覆写`createSouthPanel()`方法。

  ```java
  @Override
  protected JComponent createSouthPanel() {
      JPanel southPanel = new JPanel(new FlowLayout());
      JButton button=new JButton("再干一碗");
      button.addActionListener(e -> {
          label.setText("再干一碗");
      });
      southPanel.add(button);
      return southPanel;
  }
  ```

##### 毒鸡汤内容来源

- 获取网上的毒鸡汤API地址，<https://api.nextrt.com/V1/Dutang>。
- 为了使用RestTemplate发送Http请求，添加3个依赖包。

![16](img\16.png)
使用RestTemplate发送http请求毒鸡汤API

```java
public class ContentUtil {
    public static String getContent() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> forEntity = new RestTemplate().getForEntity("https://api.nextrt.com/V1/Dutang", Map.class);
            HttpStatus statusCode = forEntity.getStatusCode();
            String content = "";
            if (statusCode.is2xxSuccessful()) {
                List data = (List) forEntity.getBody().get("data");
                Map<String, String> contontMap = (Map<String, String>) data.get(0);
                return contontMap.get("content");
            }
        } catch (Exception e) {
            return  "汤碗都碎了";
        }
        return "今天没有鸡汤";
    }
}
```

- 布局

  ![16.1](img\16.1.png)

### 进入正题之笔记插件

#### 需求

##### 需求描述

- 在idea中选择任意文本，添加笔记的标题和内容。最后可以将笔记按照指定特定模板，生成markdown文章。
- 选中任意文本右键弹出包含自定义的子菜单`JogeenNoteAction`
  ![17](img\17.png)
- 点击子菜单`JogeenNoteAction`弹出对话框，在对话框中，编辑这条笔记的标题和内容，点击添加到笔记列表
  ![18](img\18.png)
- 填写文档的标题，点击生成文档。选择生成文档保存的目录
    ![19](img\19.png)
- 打开生成的文档，展示生成的文档
    ![20](img\20.png)

##### 需求分析

1. 如何添加一个右键点击之后的子菜单
2. 如何获取编辑器中已经选中的文本
3. 如何弹出对话，获取用户编辑的笔记内容
4. 如何使用ToolWindow展示笔记列表
5. 如何在ToolWindow中添加表格
6. 如何让用户选择文档生成的目录
7. 如何将笔记列表静态化生成文档

#### 代码编写

##### 创建工程

新创建一个工程叫做MarkBook,作为我们的工程名，也作为这个插件产品的名称

```xml
<idea-plugin>
  <id>com.itheima.cd.markbook.id</id>
  <name>MarkBook</name>
  <version>1.0</version>
  <vendor email="chenjiagen@itcast.cn" url="http://www.itheima.com">itheima</vendor>

  <description><![CDATA[
      这是一款可以帮助程序在阅读代码是添加笔记,并将笔记生成文档的插件。<br>
      <em>MarkDown文档</em>
    ]]></description>

  <change-notes><![CDATA[
      第一版，包含笔记添加和文档生成的主体功能<br>
      <em>仅支持生成Markdown形式笔记。</em>
    ]]>
  </change-notes>

  <!-- please see http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/build_number_ranges.html for description -->
  <idea-version since-build="173.0"/>

  <!-- please see http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/plugin_compatibility.html
       on how to target different products -->
  <depends>com.intellij.modules.platform</depends>

  <extensions defaultExtensionNs="com.intellij">
    <!-- Add your extensions here -->
  </extensions>

  <actions>
    <!-- Add your actions here -->
  </actions>

</idea-plugin>
```

##### 添加一个右键点击之后的子菜单

- 创建action，注意选择EditorPopupMenu，顺便设置了快捷键方式ctrl+p

![21](img\21.png)

- 创建字后自动生成的配置文件和PopupAction类

```xml
    <action id="MB_PopupAction" class="com.itheima.markbook.action.PopupAction" text="添加MB笔记" description="添加MB笔记的子菜单">
      <add-to-group group-id="EditorPopupMenu" anchor="first"/>
      <keyboard-shortcut keymap="$default" first-keystroke="ctrl P"/>
    </action>
```

```java
public class PopupAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        System.out.println("添加笔记的操作");
    }
}
```

- 测试结果

  ![22](img\22.png)

##### 获取编辑器中已经选中的文本

- 修改`PopupAction`对象

```java
public class PopupAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取当前编辑器对象
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        //获取选择的数据模型
        SelectionModel selectionModel = editor.getSelectionModel();
        //获取当前选择的文本
        String selectedText = selectionModel.getSelectedText();
        System.out.println(selectedText);
    }
}
```

##### 弹出对话框，获取用户编辑的笔记内容

- 创建`AddNoteDialog`

  ```java
  package com.itheima.markbook.dialog;
  
  import com.intellij.openapi.ui.DialogWrapper;
  import com.intellij.ui.EditorTextField;
  import org.jetbrains.annotations.Nullable;
  
  import javax.swing.*;
  import java.awt.*;
  
  public class AddNoteDialog extends DialogWrapper {
      /**
       * 标题输入框
       */
      private EditorTextField etfTitle;
      /**
       * 内容输入框
       */
      private EditorTextField etfMark;
  
  
      public AddNoteDialog() {
          super(true);
          init();
          setTitle("添加笔记注释");
      }
  
      @Nullable
      @Override
      protected JComponent createCenterPanel() {
          JPanel panel = new JPanel(new BorderLayout());
          etfTitle = new EditorTextField("笔记标题");
          etfMark = new EditorTextField("笔记内容");
          etfMark.setPreferredSize(new Dimension(200,100));
          panel.add(etfTitle, BorderLayout.NORTH);
          panel.add(etfMark, BorderLayout.CENTER);
          return panel;
      }
  
      @Override
      protected JComponent createSouthPanel() {
          JPanel panel = new JPanel(new FlowLayout());
          JButton btnAdd = new JButton("添加到笔记列表");
          //按钮点击事件处理
          btnAdd.addActionListener(e -> {
              //获取标题
              String title = etfTitle.getText();
              //获取内容
              String content = etfMark.getText();
              System.out.println(title + ":" + content);
          });
          panel.add(btnAdd);
          return panel;
      }
  }
  
  ```

##### 完善笔记内容

- 确定一条笔记需要的字段创建NoteData类

```java
  package com.itheima.markbook.data;
  
  public class NoteData {
      /**
       * 笔记标题
       */
      private String title;
      /**
       * 笔记内容
       */
      private String mark;
      /**
       * 标记的源码
       */
      private String content;
      /**
       * 源码所在的文件名
       */
      private String fileName;
      /**
       * 源码所在的文件类型
       */
      private String fileType;
      
      //省略get set方法
  }
```

- 找一个存储位置

```java
  package com.itheima.markbook.data;
  
  import java.util.LinkedList;
  import java.util.List;
  
  public class DataCenter {
      /**
       * 选择的文本
       */
      public static String SELECTED_TEXT = null;
      /**
       * 当前的文件名称
       */
      public static String CURRENT_FILE_NAME = null;
      /**
       *  当前的文件类型
       */
      public static String CURRENT_FILE_TYPE = null;
      /**
       * 笔记列表集合
       */
      public static List<NoteData> NOTE_LIST = new LinkedList<>();
  
  
  }
```

- 获取文件名称和类型，存储在全局变量

```java
  //文件名称
  DataCenter.CURRENT_FILE_NAME = e.getRequiredData(CommonDataKeys.PSI_FILE).getViewProvider().getVirtualFile().getName();
  DataCenter.CURRENT_FILE_TYPE =DataCenter.CURRENT_FILE_NAME.substring(DataCenter.CURRENT_FILE_NAME.lastIndexOf(".")+1);
```

- 添加笔记到笔记列表集合
```java
  //选择的内容
  DataCenter.SELECTED_TEXT = selectedText;
  //文件名称
  DataCenter.CURRENT_FILE_NAME = e.getRequiredData(CommonDataKeys.PSI_FILE).getViewProvider().getVirtualFile().getName();
  DataCenter.CURRENT_FILE_TYPE =DataCenter.CURRENT_FILE_NAME.substring(DataCenter.CURRENT_FILE_NAME.lastIndexOf(".")+1);
```

##### 如何创建一个ToolWindow

- 创建一个GUI Form

![22](img\22.png)
- 创建字后自动生成的NoteListWindow

```java
  package com.itheima.markbook.window;
  
  import com.intellij.openapi.project.Project;
  import com.intellij.openapi.wm.ToolWindow;
  
  import javax.swing.*;
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  
  public class NoteListWindow {
      private JPanel jcontent;
      private JTextField topicEtf;
      private JTable contentTable;
      private JButton createBtn;
      private JButton clearBtn;
      private JButton closeBtn;
  
      public NoteListWindow(Project project, ToolWindow toolWindow) {
          createBtn.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
  
              }
          });
          clearBtn.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
  
              }
          });
          closeBtn.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
  
              }
          });
      }
  
      public JPanel getJcontent() {
          return jcontent;
      }
  }
```
- 创建NoteListWindowFactory

```java
package com.itheima.markbook.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class NoteListWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //创建出NoteListWindow对象
        NoteListWindow noteListWindow = new NoteListWindow(project, toolWindow);
        //获取内容工厂的实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        //获取用于toolWindow显示的内容
        Content content = contentFactory.createContent(noteListWindow.getJcontent(), "", false);
        //给toolWindow设置内容
        toolWindow.getContentManager().addContent(content);
    }
}
```
- 配置加载toolWindow扩展内容
```xml
  <extensions defaultExtensionNs="com.intellij">
    <!-- Add your extensions here -->
    <toolWindow id="MarkBookWindown" 
                secondary="true" 
                anchor="right" factoryClass="com.itheima.markbook.window.NoteListWindowFactory" icon="/markbook/pluginIcon.svg">

    </toolWindow>
  </extensions>
```

##### 在ToolWindow中添加表格
- 在数据中心添加内容

```java
  private static String[] COLUMN_NAME={"标题","备注","文件名","代码段"};
  
  public static DefaultTableModel TABLE_MODEL = new DefaultTableModel(null,COLUMN_NAME);
```
- 定义表格初始化设置，并在`NoteListWindow`构造方法中调用`init`
```java
  public void init(){
      contentTable.setModel(DataCenter.TABLE_MODEL);
      contentTable.setEnabled(true);
  }
```

- 在`btnAdd`按钮的点击事件中添加

```java
  //添加
  DataCenter.TABLE_MODEL.addRow(DataConvert.toStringArray(noteData));
```

- 设置关闭

```java
  toolWindow.hide(null);
```

- 设置清空列表

```java
DataCenter.reset();
```

##### 让用户选择文档生成的目录

- 添加文件选择，获取用户选择的目录

```java
  createBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, project.getBaseDir());
          if(virtualFile!=null){
              String path = virtualFile.getPath();
              System.out.println(path);
          }
      }
  });
```

  ![22](img\25.png)

##### 将笔记列表静态化生成文档

- 定义处理的接口

```java
public interface Processor {
    public void process(SourceNoteData sourceNoteData) throws Exception;
}
```

- 编写Freemarker的抽象类

```java
  public abstract class AbstractFreeMarkerProcessor implements Processor {
  
      protected abstract Template getTemplate() throws IOException, Exception;
  
      protected abstract Object getModel(SourceNoteData sourceNoteData);
  
      protected abstract Writer getWriter(SourceNoteData sourceNoteData) throws FileNotFoundException, Exception;
  
  
      @Override
      public final void process(SourceNoteData sourceNoteData) throws Exception {
          Template template = getTemplate();
          Object model = getModel(sourceNoteData);
          Writer writer = getWriter(sourceNoteData);
          template.process(model, writer);
      }
  }
```

- 编写`MDFreeMarkProcessor`继承`AbstractFreeMarkerProcessor`。实现抽象方法

```java
  public class MDFreeMarkProcessor extends AbstractFreeMarkerProcessor {
      @Override
      protected Template getTemplate() throws Exception {
          //加载模板字符串
          String templateString = UrlUtil.loadText(MDFreeMarkProcessor.class.getResource("/template/md.ftl"));
          //创建模板配置
          Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
          //创建字符串模板的导入器
          StringTemplateLoader stringTemplateLoader=new StringTemplateLoader();
          //导入字符串模板
          stringTemplateLoader.putTemplate("MDTemplate",templateString);
          configuration.setTemplateLoader(stringTemplateLoader);
          //获取模板
          return configuration.getTemplate("MDTemplate");
      }
  
      @Override
      protected Object getModel(SourceNoteData sourceNoteData) {
          HashMap model = new HashMap();
          model.put("topic",sourceNoteData.getNoteTopic());
          model.put("noteList",sourceNoteData.getNoteDataList());
          return model;
      }
  
      @Override
      protected Writer getWriter(SourceNoteData sourceNoteData) throws Exception {
          String filePath = sourceNoteData.getFilePath();
          File file = new File(filePath);
          return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
      }
  
  }
```

- 添加处理操作
```java
  createBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, project.getBaseDir());
          if (virtualFile != null) {
              String path = virtualFile.getPath();
              String topic = topicEtf.getText();
              String filePath = path + "/" + topic + ".md";
              Processor processor = new MDFreeMarkProcessor();
              try {
                  processor.process(new DefaultSourceNoteData(topic, filePath, DataCenter.NOTE_LIST));
              } catch (Exception ex) {
                  ex.printStackTrace();
              }
          }
      }
  });
```

- 完善提示
  - 对话框提示
  ```java
  MessageDialogBuilder.yesNo("操作结果","添加成功!").show();
  ```
```
  - 通知提示 
​```java
NotificationGroup notificationGroup = new NotificationGroup("testid", NotificationDisplayType.BALLOON, false);
/**
 * content :  通知内容
 * type  ：通知的类型，warning,info,error
 */
Notification notification = notificationGroup.createNotification("测试通知", MessageType.INFO);
Notifications.Bus.notify(notification);
```