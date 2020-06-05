package zju.plugin;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PropertiesDialog extends DialogWrapper {
    private EditorTextField pythonEnvText;
    private EditorTextField pythonProjectText;
//    private EditorTextField startTimeText;
//    private EditorTextField endTimeText;

    public PropertiesDialog() {
        super(true);
        setTitle("Set Properties");
        init();
    }
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        pythonEnvText = new EditorTextField("Python Interpreter Path");
//        pythonEnvText.setPreferredSize(new Dimension(160, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        panel.add(pythonEnvText, gbc);

        JButton pythonEnvButton = new JButton("+");
        pythonEnvButton.setPreferredSize(new Dimension(30,30));
        pythonEnvButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(0);
            int result = jFileChooser.showOpenDialog(panel);
            if (JFileChooser.APPROVE_OPTION == result) {
                File pythonEnv = jFileChooser.getSelectedFile();
                pythonEnvText.setText(pythonEnv.getPath());
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        panel.add(pythonEnvButton, gbc);

        pythonProjectText = new EditorTextField("Analyze Part Path");
//        pythonProjectText.setPreferredSize(new Dimension(160, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        panel.add(pythonProjectText, gbc);


        JButton pythonProjectButton = new JButton("+");
        pythonProjectButton.setPreferredSize(new Dimension(30,30));
        pythonProjectButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(1);
            int result = jFileChooser.showOpenDialog(panel);
            if (JFileChooser.APPROVE_OPTION == result) {
                File pythonEnv = jFileChooser.getSelectedFile();
                pythonProjectText.setText(pythonEnv.getPath());
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        panel.add(pythonProjectButton, gbc);



//        startTimeText = new EditorTextField("Train set start time");
//        startTimeText.setPreferredSize(new Dimension(200, 30));
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 2;
//        gbc.gridheight = 1;
//        gbc.weightx = 0.7;
//        gbc.weighty = 1;
//        panel.add(startTimeText, gbc);
//
//
//        endTimeText = new EditorTextField("Train set end time");
//        endTimeText.setPreferredSize(new Dimension(200, 30));
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 2;
//        gbc.gridheight = 1;
//        gbc.weightx = 0.7;
//        gbc.weighty = 1;
//        panel.add(endTimeText, gbc);


        return panel;
    }

    @Override
    protected JComponent createSouthPanel(){
        JPanel panel = new JPanel();
        JButton button = new JButton("Conform");
        button.addActionListener(e ->{
            DataCenter.pythonEnv = pythonEnvText.getText()+" ";
            DataCenter.pythonProject = pythonProjectText.getText();
//            DataCenter.startTime = startTimeText.getText();
//            DataCenter.endTime = endTimeText.getText();
            PropertiesDialog.this.dispose();
        });
        panel.add(button);
        return panel;
    }
}
