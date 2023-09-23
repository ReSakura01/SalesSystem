package org.pyq.System;

import com.alibaba.fastjson.JSON;
import org.pyq.Exception.ExceptionDialog;
import org.pyq.Exception.MyException;
import org.pyq.Message.MessageDialog;
import org.pyq.User.User;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RegisterDialog extends JDialog {
    ArrayList<User> users;
    private JButton SureButton, CancelButton;
    private JTextField nameField, codeField, code1Field;

    public RegisterDialog(JFrame parent, String title) {
        super(parent, title, true);

        users = new ArrayList<>();
        nameField = new JTextField(20);
        codeField = new JTextField(20);
        code1Field = new JTextField(20);
        SureButton = new JButton("确认");
        CancelButton = new JButton("取消");

        LodeUser();

        SureButton.addActionListener(e -> {
            Check ();
        });
        CancelButton.addActionListener(e -> {
            dispose();
        });

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("用户名:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("密码:"));
        inputPanel.add(codeField);
        inputPanel.add(new JLabel("确认密码:"));
        inputPanel.add(code1Field);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(SureButton);
        buttonPanel.add(CancelButton);

        getContentPane().add(inputPanel);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 设置对话框的属性
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    // 载入文件信息
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
    // 检查注册的用户是否符合标准
    private void Check () {
        String code = codeField.getText();
        String code1 = code1Field.getText();
        try {
            String name = nameField.getText();
            for (User u : users) {
                if (u.getName().equals(name)) {
                    throw new MyException ("该用户名已存在，请重新输入");
                }
            }
            if (!code.equals(code1)) {
                throw new MyException ("两次输入的密码不相同，请重新输入！");
            }
            ArrayList<User> users1 = new ArrayList<>();
            users1 = users;
            users1.add (new User(name, code));
            String jsonString = JSON.toJSONString(users1);
            try {
                // 写入JSON文件
                FileWriter writer = new FileWriter("User.json");
                writer.write(jsonString);
                writer.close();
                dispose();
                new MessageDialog(null, "Success", "创建成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MyException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }
}
