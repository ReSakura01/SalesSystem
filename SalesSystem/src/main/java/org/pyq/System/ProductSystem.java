package org.pyq.System;

import com.alibaba.fastjson.JSON;
import org.pyq.Exception.MyException;
import org.pyq.Message.MessageDialog;
import org.pyq.Product.Product;
import org.pyq.Product.ProductTableModel;
import org.pyq.Exception.ExceptionDialog;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductSystem extends JFrame {
    private Set<Integer> idset;
    private ArrayList<Product> products;
    private JButton addButton, deleteButton, updateButton, searchButton;
    private JTextField idField, nameField, priceField;
    private JTable productTable;
    ProductTableModel model;

    public ProductSystem () {
        idset = new HashSet<>();
        products = new ArrayList<>();

        nameField = new JTextField(20);
        priceField = new JTextField(20);
        idField = new JTextField(20);
        addButton = new JButton("添加");
        deleteButton = new JButton("删除");
        updateButton = new JButton("修改");
        searchButton = new JButton("查询");

        productTable = new JTable();

        addButton.addActionListener(e -> {
            // AddProduct();
            new AddDialog(null, "添加");
        });
        deleteButton.addActionListener(e -> {
            // DeleteProduct();
            new DeletDialog (null, "删除");
        });
        updateButton.addActionListener(e -> {
            new MessageDialog(null, "^ ^", "亲~ 我们这款软件暂时不支持修改哦~");
        });
        searchButton.addActionListener(e -> {
            // SearchProduct();
            new MessageDialog(null, "^ ^", "亲~ 我们这款软件暂时只支持名称查找哦~");
            new SearchDialog (null, "查找");
        });

        // 载入文件信息
        LodeProducts ();
        model = new ProductTableModel(products);
        productTable.setModel((TableModel) model);

        // 创建布局
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);

        // 添加界面元素到窗口
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        add(new JScrollPane(productTable), BorderLayout.CENTER);
        // add(new JScrollPane(productTable));

        // 设置窗口属性
        setTitle("Product Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();                         // 自动调节窗口大小
        setLocationRelativeTo(null);    //居中
        setVisible(true);
    }

    //将json文件里的内容读出到products中
    private void LodeProducts () {
        try {
            // 读取JSON文件
            BufferedReader reader = new BufferedReader(new FileReader("Products.json"));
            String line;
            StringBuilder jsonBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            // 解析JSON数据
            String jsonData = jsonBuilder.toString();
            // TODO: 在这里处理JSON数据
            products = (ArrayList<Product>) JSON.parseArray(jsonData, Product.class);
            // UpdateTable(products);

            for (Product p : products) {
                idset.add (p.getId());
            }
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 添加产品信息
    private void AddProduct () {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            try {
                if (idset.contains(id)) {
                    throw new MyException("亲~ 该id已经存在了哦~");
                } else if (id <= 0) {
                    throw new MyException("亲~ id输入格式有误，id应该是个正数哦~");
                } else {
                    products.add(new Product(id, name, price));
                    idset.add (id);
                    UpdateProduct();
                    new MessageDialog(null, "Success", "录入成功！");
                }
            } catch (MyException e) {
                new ExceptionDialog(null, "Error", e.getMessage());
            }
        } catch (NumberFormatException e) {  // 如果字符串不能转为数字
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 删除产品信息
    private void DeleteProduct () {
        /*
            直接让无关的Field置空，后续可以让其无法输入
         */
        priceField.setText("");
        nameField.setText("");
        int f = -1;
        try {
            int id = Integer.parseInt(idField.getText());
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == id) {
                    f = i;
                }
            }
            if (f == -1) {
                new ExceptionDialog(null, "Warning", "亲~ 你需要删除的元素找不到哦~");
            } else {
                products.remove(f);
                idset.remove(id);
                UpdateProduct();
                new MessageDialog(null, "Success", "删除成功！");
            }
        } catch (NumberFormatException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    // 查找产品信息
    private void SearchProduct () {
        String name = nameField.getText();

        ArrayList<Product> products1 = new ArrayList<>();

        for (int i = 0; i < products.size(); i ++) {
            if (products.get(i).getName().equals(name)) {
                products1.add (products.get(i));
            }
        }

        if (products1.size() == 0) {
            new ExceptionDialog(null, "Warning", "亲~ 你需要查找的元素找不到哦~");
        } else {
            ProductTableModel model = new ProductTableModel(products1);
            JTable productTable1 = new JTable();
            productTable1.setModel((TableModel) model);
            JOptionPane.showMessageDialog(null, new JScrollPane(productTable1), "查找结果", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // 更新产品信息
    private void UpdateProduct () {
        String jsonString = JSON.toJSONString(products);
        try {
            // 写入JSON文件
            // System.out.println(jsonString);
            FileWriter writer = new FileWriter("Products.json");
            writer.write(jsonString);
            writer.close();
            // UpdateTable (products);
            model.fireTableDataChanged();
        } catch (IOException e) {
            new ExceptionDialog(null, "Error", e.getMessage());
        }
    }

    private void UpdateTable(List<Product> products) {
        ProductTableModel model = new ProductTableModel(products);
        productTable.setModel((TableModel) model);
    }

    class AddDialog extends JDialog {
        public AddDialog(JFrame parent, String title) {
            super(parent, title, true);
            // 创建对话框的内容面板
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            inputPanel.add(new JLabel("编号:"));
            inputPanel.add(idField);
            inputPanel.add(new JLabel("产品名:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("价格:"));
            inputPanel.add(priceField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                AddProduct ();
            });
            cancelButton.addActionListener(e -> {
                dispose();
                idField.setText("");
                nameField.setText("");
                priceField.setText("");
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
                DeleteProduct();
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
            inputPanel.add(new JLabel("产品名:"));
            inputPanel.add(nameField);

            JButton sureButton = new JButton("确定");
            JButton cancelButton = new JButton("取消");

            sureButton.addActionListener(e -> {
                SearchProduct();
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