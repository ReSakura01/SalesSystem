package org.pyq.System;

import com.alibaba.fastjson.JSON;
import org.pyq.Emploee.Employee;
import org.pyq.Exception.MyException;
import org.pyq.Message.MessageDialog;
import org.pyq.Record.Record;
import org.pyq.Exception.ExceptionDialog;
import org.pyq.Record.RecordTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

public class RecordSystem extends JFrame {
    /*

     */
    private Set<Integer> idset;
    private ArrayList<Record> records;
    private ArrayList<Employee> employees;
    private JButton addButton, deleteButton, updateButton, searchButton, calcButton, clearButton;
    private JTextField dateField, timeField, idField, empidField, proidField, numField, amoutField;
    private JTable recordTable;

    public RecordSystem () {
        idset = new HashSet<>();
        records = new ArrayList<>();
        employees = new ArrayList<>();

        dateField = new JTextField(20);
        timeField = new JTextField(20);
        idField = new JTextField(20);
        empidField = new JTextField(20);
        proidField = new JTextField(20);
        numField = new JTextField(20);
        amoutField = new JTextField(20);

        addButton = new JButton("添加");
        deleteButton = new JButton("删除");
        updateButton = new JButton("修改");
        searchButton = new JButton("查询");
        calcButton = new JButton("统计");
        clearButton = new JButton("清空数据");

        recordTable = new JTable();

        addButton.addActionListener(e -> {
            // AddRecord();
            new AddDialog(null, "添加");
        });
        deleteButton.addActionListener(e -> {
            // DeleteRecord();
            new DeletDialog(null, "删除");
        });
        updateButton.addActionListener(e -> {
            new MessageDialog(null, "^ ^", "亲~ 我们这款软件暂时不支持修改哦~");
        });
        searchButton.addActionListener(e -> {
            // SearchRecord();
            new SearchDialog(null, "查找");
        });
        calcButton.addActionListener(e -> {
            CalcRecord();
        });
        clearButton.addActionListener(e -> {
            InitRecords ();
        });

        // 载入文件信息
        LodeRecords ();

        // 创建布局
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(calcButton);
        buttonPanel.add(clearButton);

        // 添加界面元素到窗口
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        add(new JScrollPane(recordTable), BorderLayout.CENTER);
        // add(new JScrollPane(productTable));

        // 设置窗口属性
        setTitle("Record Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();                         // 自动调节窗口大小
        setLocationRelativeTo(null);    //居中
        setVisible(true);
    }

    // 将json文件里的内容读出到records中
    private void LodeRecords () {
        try {
            // 读取JSON文件
            BufferedReader reader = new BufferedReader(new FileReader("Records.json"));
            String line;
            StringBuilder jsonBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            // 解析JSON数据
            String jsonData = jsonBuilder.toString();
            // TODO: 在这里处理JSON数据
            records = (ArrayList<Record>) JSON.parseArray(jsonData, Record.class);
            UpdateTable(records);
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
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
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 销售信息初始化：将销售信息清空
    private void InitRecords () {
        records.clear();
        String jsonString = JSON.toJSONString(records);
        try {
            FileWriter writer = new FileWriter("Records.json");
            writer.write(jsonString);
            writer.close();
            UpdateTable(records);
            new MessageDialog(null, "Success", "清空成功");
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }
    // 添加销售信息
    private void AddRecord () {
        LodeEmployees();
        try {
            LocalDate date = LocalDate.parse(dateField.getText());
            LocalTime time = LocalTime.parse(timeField.getText());
            String id = idField.getText();
            try {
                int empid = Integer.parseInt(empidField.getText());
                int f = 0;
                for (Employee e : employees) {
                    if (e.getId() == empid) {
                        f = 1;
                    }
                }
                if (f == 0) {
                    throw new MyException ("员工库里没有此人!");
                }
                int proid = Integer.parseInt(proidField.getText());
                int num = Integer.parseInt(numField.getText());
                double amout = Double.parseDouble(amoutField.getText());
                records.add (new Record(date, time, id, empid, proid, num, amout));
                UpdateTable(records);
                new MessageDialog(null, "Success", "录入成功！");
            } catch (NumberFormatException | MyException e) {
                new ExceptionDialog(null, "Error", e.getMessage());
            }
        } catch (DateTimeParseException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 删除销售信息
    private void DeleteRecord() {
         /*
            直接让无关的Field置空，后续可以让其无法输入
            似乎可以加按月份删除
         */
        try {
            dateField.setText("");
            timeField.setText("");
            empidField.setText("");
            proidField.setText("");
            numField.setText("");
            amoutField.setText("");
            String id = idField.getText();
            int f = -1;
            for (int i = 0; i < records.size(); i ++) {
                if (records.get(i).getSerialNumber().equals(id)) {
                    f = i;
                }
            }
            if (f == -1) {
                new ExceptionDialog(null, "Warnning", "亲~ 你需要删除的元素找不到哦~");
            } else {
                records.remove(f);
                idset.remove(id);
                UpdateRecord();
                new MessageDialog(null, "Success", "删除成功！");
            }
        } catch (DateTimeParseException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 查找销售信息
    private void SearchRecord() {
        try {
            String id = idField.getText();

            ArrayList<Record> records1 = new ArrayList<>();

            for (int i = 0; i < records.size(); i ++) {
                if (records.get(i).getSerialNumber().equals(id)) {
                    records1.add (records.get(i));
                }
            }

            if (records1.size() == 0) {
                new ExceptionDialog(null, "Warnning", "亲~ 你需要删除的元素找不到哦~");
            } else {
                RecordTableModel model = new RecordTableModel(records1);
                JTable recordTable1 = new JTable();
                recordTable1.setModel((TableModel) model);
                JOptionPane.showMessageDialog(null, new JScrollPane(recordTable1), "查找结果", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 更新销售信息
    private void UpdateRecord() {
        String jsonString = JSON.toJSONString(records);
        try {
            // 写入JSON文件
            // System.out.println(jsonString);
            FileWriter writer = new FileWriter("Records.json");
            writer.write(jsonString);
            writer.close();
            UpdateTable (records);
            new MessageDialog(null, "Success", "更新成功！");
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 统计销售额
    private void CalcRecord() {
        LodeEmployees();
        try {
            Map<Integer, Double> map = new TreeMap<>();
            for (Record r : records) {
                for (Employee e : employees) {
                    if (e.getId() == r.getEmpId()) {
                        if (map.containsKey(r.getEmpId())) {
                            map.put(r.getEmpId(), map.get(r.getEmpId()) + r.getAmount());
                        } else {
                            map.put(r.getEmpId(), r.getAmount());
                        }
                    }
                }
            }
            for (Employee e : employees) {
                if (map.containsKey(e.getId())) {
                    e.setSum(map.get(e.getId()));
                } else {
                    e.setSum(0);
                }
            }
            if (map.size() == 0) {
                throw new MyException("没有统计到有销售员参与到这些订单中。");
            } else {
                String jsonString = JSON.toJSONString(employees);
                try {
                    // 写入JSON文件
                    // System.out.println(jsonString);
                    FileWriter writer = new FileWriter("Employees.json");
                    writer.write(jsonString);
                    writer.close();
                } catch (IOException e) {
                    new ExceptionDialog(null, "Error", e.getMessage());
                }
                new MessageDialog(null, "Success", "统计成功！");
            }
        } catch (MyException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    private void UpdateTable(List<Record> records) {
        RecordTableModel model = new RecordTableModel(records);
        recordTable.setModel((TableModel) model);
    }

    class AddDialog extends JDialog {
        public AddDialog(JFrame parent, String title) {
            super(parent, title, true);

            // 创建对话框的内容面板
            JPanel inputPanel = new JPanel(new GridLayout(7, 2));
            inputPanel.add(new JLabel("销售日期:"));
            inputPanel.add(dateField);
            inputPanel.add(new JLabel("销售时间:"));
            inputPanel.add(timeField);
            inputPanel.add(new JLabel("流水号:"));
            inputPanel.add(idField);
            inputPanel.add(new JLabel("员工编号:"));
            inputPanel.add(empidField);
            inputPanel.add(new JLabel("产品编号:"));
            inputPanel.add(proidField);
            inputPanel.add(new JLabel("销售数量:"));
            inputPanel.add(numField);
            inputPanel.add(new JLabel("销售金额:"));
            inputPanel.add(amoutField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                AddRecord ();
            });
            cancelButton.addActionListener(e -> {
                dispose();
                dateField.setText("");
                timeField.setText("");
                idField.setText("");
                empidField.setText("");
                proidField.setText("");
                numField.setText("");
                amoutField.setText("");
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
            inputPanel.add(new JLabel("流水号:"));
            inputPanel.add(idField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                DeleteRecord ();
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
            inputPanel.add(new JLabel("流水号:"));
            inputPanel.add(idField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                SearchRecord();
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
}
