package org.pyq.Exception;

import javax.swing.*;

public class ExceptionDialog extends JDialog {
    public ExceptionDialog(JFrame parent, String title, String message) {
        super(parent, title, true);

        // 创建异常对话框的内容面板
        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        panel.add(label);

        // 设置异常对话框的属性
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}