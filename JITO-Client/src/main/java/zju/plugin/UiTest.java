package zju.plugin;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class UiTest {
    public static void main(String[] agrs){
        JFrame frame = new JFrame("GridWeightTest");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 100));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        TextField pythonEnvText = new TextField("Python Interpreter Path");
        pythonEnvText.setPreferredSize(new Dimension(160, 30));
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
            int result = jFileChooser.showOpenDialog(null);
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

        TextField pythonProjectText = new TextField("JITO-Identification Path");
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
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        panel.add(pythonProjectButton, gbc);



        TextField dbUsernameText = new TextField("Train set start time (eg. 2020-1-1)");
//        dbUsernameText.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        panel.add(dbUsernameText, gbc);


        TextField dbPasswordText = new TextField("Database password");
//        dbPasswordText.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        panel.add(dbPasswordText, gbc);

        frame.add(panel);
        frame.setVisible(true);

        Messages.showMessageDialog("This change is likely buggy", "Information", Messages.getInformationIcon());
        Messages.showMessageDialog("This change is likely clean", "Information", Messages.getInformationIcon());

    }
}
