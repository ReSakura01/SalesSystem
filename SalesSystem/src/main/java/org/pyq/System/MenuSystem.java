package org.pyq.System;

import javax.swing.*;
import java.awt.*;

public class MenuSystem extends JFrame {
    private JButton EmployButton, ProductButton, RecordButton;

    public MenuSystem() {
        EmployButton = new JButton("员工信息");
        ProductButton = new JButton("产品信息");
        RecordButton = new JButton("销售信息");

        EmployButton.addActionListener(e -> {
            new EmployeeSystem();
        });
        ProductButton.addActionListener(e -> {
            new ProductSystem();
        });
        RecordButton.addActionListener(e -> {
            new RecordSystem();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(EmployButton);
        buttonPanel.add(ProductButton);
        buttonPanel.add(RecordButton);

        add(buttonPanel);

        setTitle("菜单");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();                         // 自动调节窗口大小
        setLocationRelativeTo(null);    //居中
        setVisible(true);
    }
}
