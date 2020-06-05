package zju.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class SetPropertiesAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PropertiesDialog propertiesDialog = new PropertiesDialog();
        propertiesDialog.show();
        Messages.showMessageDialog("Set Success","Information", Messages.getInformationIcon());
    }
}
