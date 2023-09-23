package org.pyq.Message;

import javax.swing.*;

public class MessageDialog extends JDialog {
    public MessageDialog (JFrame parent, String title, String message) {
        super(parent, title, true);

        // 创建对话框的内容面板
        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        panel.add(label);

        getContentPane().add(panel);

        // 设置对话框的属性
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
