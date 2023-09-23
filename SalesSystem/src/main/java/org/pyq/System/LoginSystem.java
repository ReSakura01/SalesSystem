package org.pyq.System;

import com.alibaba.fastjson.JSON;
import org.pyq.Exception.ExceptionDialog;
import org.pyq.Exception.MyException;
import org.pyq.User.User;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoginSystem extends JFrame {
    /*
        登录界面
     */
    ArrayList<User> users; // 定义用户数组
    private JTextField nameField, codeField; // 定义输入框
    private JButton addButton, inButton; // 定义按钮

    public LoginSystem() {
        users = new ArrayList<>();

        nameField = new JTextField(20);
        codeField = new JTextField(20);

        addButton = new JButton("注册");
        inButton = new JButton("登录");

        LodeUser ();

        addButton.addActionListener(e -> {
            Register();
        });
        inButton.addActionListener(e -> {
            Login();
        });

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("用户名:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("密码:"));
        inputPanel.add(codeField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(inButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // 设置窗口属性
        setTitle("登录");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();                         // 自动调节窗口大小
        setLocationRelativeTo(null);    //居中
        setVisible(true);
    }
    /*
        载入文件信息
     */
    private void LodeUser () {
        try {
            // 读取JSON文件
            BufferedReader reader = new BufferedReader(new FileReader("User.json"));
            String line;
            StringBuilder jsonBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            // 解析JSON数据
            String jsonData = jsonBuilder.toString();
            // TODO: 在这里处理JSON数据
            users = (ArrayList<User>) JSON.parseArray(jsonData, User.class);
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }
    /*
        检查用户组内是否存在
     */
    private void Login () {
        String name = nameField.getText();
        String code = codeField.getText();
        try {
            int f = -1;
            for (User p : users) {
                if (p.getName().equals(name) && p.getPassword().equals(code)) {
                    f = 1;
                }
            }
            if (f == -1) {
                throw new MyException("用户名或密码错误");
            } else {
                /*
                登录成功
                 */
                new MenuSystem();
                dispose();
            }
        } catch (MyException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    private void Register () {
        new RegisterDialog(null, "注册");
        LodeUser ();
    }
}
