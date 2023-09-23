package org.pyq.Emploee;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
    /*
        表格模型
     */
    private List<Employee> employees;
    private String[] columnNames = {"编号", "姓名", "性别", "销售额"};
    private int sortedColumn = -1;
    private boolean ascending = true;

    public EmployeeTableModel(List<Employee> employees) {
        this.employees = employees;
    }

    public int getRowCount() {
        return employees.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return employee.getId();
            case 1:
                return employee.getName();
            case 2:
                return employee.getSex();
            case 3:
                return employee.getSum();
            default:
                return null;
        }
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Class<?> getColumnClass(int columnIndex) {
        // 指定每一列的数据类型，用于排序
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
            case 2:
                return String.class;
            case 3:
                return Double.class;
            default:
                return Object.class;
        }
    }

    public void sortTable(int column) {
        // 检查是否点击了已排序的列，切换升序/降序排序
        if (column == sortedColumn) {
            ascending = !ascending;
        } else {
            sortedColumn = column;
            ascending = true;
        }

        // 根据点击的列进行排序
        switch (column) {
            case 0:
                employees.sort(Comparator.comparing(Employee::getId));
                break;
            case 1:
                employees.sort(Comparator.comparing(Employee::getName));
                break;
            case 2:
                employees.sort(Comparator.comparing(Employee::getSex));
                break;
            case 3:
                employees.sort(Comparator.comparing(Employee::getSum));
                break;
        }

        if (!ascending) {
            // 如果是降序排序，则反转列表
            Comparator<Employee> comparator = null;
            switch (sortedColumn) {
                case 0:
                    comparator = Comparator.comparing(Employee::getId);
                    break;
                case 1:
                    comparator = Comparator.comparing(Employee::getName);
                    break;
                case 2:
                    comparator = Comparator.comparing(Employee::getSex);
                    break;
                case 3:
                    comparator = Comparator.comparing(Employee::getSum);
                    break;
            }
            if (comparator != null) {
                employees.sort(comparator.reversed());
            }
        }

        // 通知表格模型数据发生变化
        fireTableDataChanged();
    }
}
