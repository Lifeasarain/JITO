package zju.plugin;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.*;
import java.io.File;

public class MyMarkupModel {
    public void highlight(File file, Project project, int line){
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        MarkupModel markupModel = DocumentMarkupModel.forDocument(document, project, true);
        TextAttributes buginfo = new TextAttributes(null, Color.yellow, null, null, Font.PLAIN);
        markupModel.addLineHighlighter(line - 1, HighlighterLayer.ERROR, buginfo);
    }
    public void unhighlight(File file, Project project, int line){
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        MarkupModel markupModel = DocumentMarkupModel.forDocument(document, project, true);
        TextAttributes buginfo = new TextAttributes(null, Color.red, null, null, Font.PLAIN);
//        markupModel.addLineHighlighter(line - 1, HighlighterLayer.ERROR, buginfo);
        markupModel.removeAllHighlighters();
    }


}
