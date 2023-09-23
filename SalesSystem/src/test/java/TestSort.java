import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestSort extends AbstractTableModel {
    private List<Employee> employees;
    private String[] columnNames = {"编号", "姓名", "性别", "销售额"};
    private int sortedColumn = -1;
    private boolean ascending = true;

    public TestSort(List<Employee> employees) {
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
                return Integer.class; // 编号列的数据类型为 Integer
            case 1:
                return String.class; // 姓名列的数据类型为 String
            case 2:
                return String.class; // 性别列的数据类型为 String
            case 3:
                return Double.class; // 销售额列的数据类型为 Double
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

    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "John", "Male", 5000));
        employees.add(new Employee(2, "Alice", "Female", 6000));
        employees.add(new Employee(3, "Bob", "Male", 4500));
        employees.add(new Employee(4, "Mary", "Female", 7000));

        TestSort tableModel = new TestSort(employees);
        JTable table = new JTable(tableModel);

        // 添加鼠标监听器
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                tableModel.sortTable(column);
            }
        });

        JFrame frame = new JFrame("Employee Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().add(new JScrollPane(table));
        frame.setVisible(true);
    }

    private static class Employee {
        private int id;
        private String name;
        private String sex;
        private double sum;

        public Employee(int id, String name, String sex, double sum) {
            this.id = id;
            this.name = name;
            this.sex = sex;
            this.sum = sum;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSex() {
            return sex;
        }

        public double getSum() {
            return sum;
        }
    }
}
