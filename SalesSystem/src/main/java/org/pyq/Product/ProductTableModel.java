package org.pyq.Product;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
    private List<Product> products;
    private String[] columnNames = {"编号", "产品名", "价格"};

    public ProductTableModel (List<Product> employees) {
        this.products = employees;
    }

    public int getRowCount() {
        return products.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = products.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return product.getId();
            case 1:
                return product.getName();
            case 2:
                return product.getPrice();
            default:
                return null;
        }
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }
}
