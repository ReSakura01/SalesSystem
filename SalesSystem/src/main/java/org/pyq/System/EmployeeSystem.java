package org.pyq.System;

import com.alibaba.fastjson.JSON;
import org.pyq.Emploee.Employee;
import org.pyq.Emploee.EmployeeTableModel;
import org.pyq.Exception.MyException;
import org.pyq.Message.MessageDialog;
import org.pyq.Exception.ExceptionDialog;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmployeeSystem extends JFrame {
    /*

     */
    private Set<Integer> idset;
    private ArrayList<Employee> employees;
    private JButton addButton, deleteButton, updateButton, searchButton;
    private JTextField idField, nameField, sexField;
    private JTable employeeTable;
    EmployeeTableModel model;

    public EmployeeSystem() {
        idset = new HashSet<>();
        employees = new ArrayList<>();

        nameField = new JTextField(20);
        sexField = new JTextField(20);
        idField = new JTextField(20);
        addButton = new JButton("添加");
        deleteButton = new JButton("删除");
        updateButton = new JButton("修改");
        searchButton = new JButton("查询");

        employeeTable = new JTable();

        addButton.addActionListener(e -> {
            // AddEmployee();
            new AddDialog (null, "添加");
        });
        deleteButton.addActionListener(e -> {
            // DeleteEmployee();
            new DeletDialog(null, "删除");
        });
        updateButton.addActionListener(e -> {
            // UpdateEmployee();
            // new MessageDialog(null, "^ ^", "亲~ 我们这款软件暂时不支持修改哦~");
            new ModifyDialog(null, "修改");
        });
        searchButton.addActionListener(e -> {
            // SearchEmployee();
            new MessageDialog(null, "^ ^", "亲~ 我们这款软件暂时只支持名称查找哦~");
            new SearchDialog(null, "查找");
        });

        // 载入文件信息
        LodeEmployees ();
        model = new EmployeeTableModel(employees);
        employeeTable.setModel((TableModel) model);

        employeeTable.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = employeeTable.columnAtPoint(e.getPoint());
                model.sortTable(column);
            }
        });

        // 创建布局
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);

        // 添加界面元素到窗口
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // 设置窗口属性
        setTitle("Employee Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();                         // 自动调节窗口大小
        setLocationRelativeTo(null);    //居中
        setVisible(true);
    }

    // 将json文件里的内容读出到employees中
    private void LodeEmployees () {
        try {
            // 读取JSON文件
            BufferedReader reader = new BufferedReader(new FileReader("Employees.json"));
            String line;
            StringBuilder jsonBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            // 解析JSON数据
            String jsonData = jsonBuilder.toString();
            // TODO: 在这里处理JSON数据
            employees = (ArrayList<Employee>) JSON.parseArray(jsonData, Employee.class);
            UpdateTable (employees);
            for (Employee e : employees) {
                idset.add (e.getId());
            }
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 添加员工信息
    private void AddEmployee () {
        String name = nameField.getText();
        String sex = sexField.getText();
        try {
            int id = Integer.parseInt(idField.getText());
            try {
                if (idset.contains(id)) {
                    throw new MyException("亲~ 该id已经存在了哦~");
                } else if (id <= 0) {
                    throw new MyException("亲~ id输入格式有误，id应该是个正数哦~");
                } else {
                    if (sex.equals("男") || sex.equals("女")) {
                        employees.add(new Employee(id, name, sex, 0));
                        idset.add (id);
                        UpdateEmployee();
                        new MessageDialog(null, "Success", "录入成功！");
                    } else {
                        throw new MyException("亲~ 性别输入错误了哦~");
                    }
                }
            } catch (MyException e) {
                new ExceptionDialog(null, "Error", e.getMessage());
            }
        } catch (NumberFormatException e) {  // 如果字符串不能转为数字
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 删除员工信息
    private void DeleteEmployee() {
        /*
            直接让无关的Field置空，后续可以让其无法输入
         */
        sexField.setText("");
        nameField.setText("");
        try {
            int f = -1;
            int id = Integer.parseInt(idField.getText());
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId() == id) {
                    f = i;
                }
            }
            if (f == -1) {
                new ExceptionDialog(null, "Warnning", "亲~ 你需要删除的元素找不到哦~");
            } else {
                employees.remove(f);
                idset.remove(id);
                UpdateEmployee();
                new MessageDialog(null, "Success", "删除成功！");
            }
        } catch (NumberFormatException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 查找员工信息
    private void SearchEmployee() {
        String name = nameField.getText();

        ArrayList<Employee> employees1 = new ArrayList<>();

        for (int i = 0; i < employees.size(); i ++) {
            if (employees.get(i).getName().equals(name)) {
                employees1.add (employees.get(i));
            }
        }
        if (employees1.size() == 0) {
            new ExceptionDialog(null, "Warnning", "亲~ 你需要查找的员工找不到哦~");
        } else {
            EmployeeTableModel model = new EmployeeTableModel(employees1);
            JTable employeeTable1 = new JTable();
            employeeTable1.setModel((TableModel) model);
            JOptionPane.showMessageDialog(null, new JScrollPane(employeeTable1), "查找结果", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // 更新员工信息
    private void UpdateEmployee() {
        String jsonString = JSON.toJSONString(employees);
        try {
            // 写入JSON文件
            // System.out.println(jsonString);
            FileWriter writer = new FileWriter("Employees.json");
            writer.write(jsonString);
            writer.close();
            // UpdateTable (employees);
            model.fireTableDataChanged();
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    private void ModifyEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            for (int i = 0; i < employees.size(); i ++) {
                if (employees.get(i).getId() == id) {
                    employees.get(i).setName(name);
                }
            }
            UpdateEmployee();
            new MessageDialog(null, "Success", "修改成功！");
        } catch (NumberFormatException e){
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    private void UpdateTable(List<Employee> employees) {
        model = new EmployeeTableModel(employees);
        employeeTable.setModel((TableModel) model);
    }

    class AddDialog extends JDialog {
        public AddDialog(JFrame parent, String title) {
            super(parent, title, true);
            // 创建对话框的内容面板
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            inputPanel.add(new JLabel("编号:"));
            inputPanel.add(idField);
            inputPanel.add(new JLabel("姓名:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("性别:"));
            inputPanel.add(sexField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                AddEmployee ();
            });
            cancelButton.addActionListener(e -> {
                dispose();
                idField.setText("");
                nameField.setText("");
                sexField.setText("");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(sureButton);
            buttonPanel.add(cancelButton);

            getContentPane().add(inputPanel);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            // 设置对话框的属性
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    class DeletDialog extends JDialog {
        public DeletDialog (JFrame parent, String title) {
            super(parent, title, true);
            // 创建对话框的内容面板
            JPanel inputPanel = new JPanel(new GridLayout(1, 2));
            inputPanel.add(new JLabel("编号:"));
            inputPanel.add(idField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                DeleteEmployee();
            });
            cancelButton.addActionListener(e -> {
                dispose();
                idField.setText("");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(sureButton);
            buttonPanel.add(cancelButton);

            getContentPane().add(inputPanel);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            // 设置对话框的属性
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    class SearchDialog extends JDialog {
        public SearchDialog (JFrame parent, String title) {
            super(parent, title, true);
            // 创建对话框的内容面板
            JPanel inputPanel = new JPanel(new GridLayout(1, 2));
            inputPanel.add(new JLabel("姓名:"));
            inputPanel.add(nameField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                SearchEmployee();
            });
            cancelButton.addActionListener(e -> {
                dispose();
                nameField.setText("");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(sureButton);
            buttonPanel.add(cancelButton);

            getContentPane().add(inputPanel);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            // 设置对话框的属性
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }

    class ModifyDialog extends JDialog {
        public ModifyDialog (JFrame parent, String title) {
            super(parent, title, true);
            // 创建对话框的内容面板
            JPanel inputPanel = new JPanel(new GridLayout(2, 2));
            inputPanel.add(new JLabel("编号:"));
            inputPanel.add(idField);
            inputPanel.add(new JLabel("姓名:"));
            inputPanel.add(nameField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                ModifyEmployee();
            });
            cancelButton.addActionListener(e -> {
                dispose();
                nameField.setText("");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(sureButton);
            buttonPanel.add(cancelButton);

            getContentPane().add(inputPanel);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            // 设置对话框的属性
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            pack();
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }
}