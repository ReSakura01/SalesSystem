package org.pyq.Record;

import org.pyq.Emploee.Employee;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RecordTableModel extends AbstractTableModel {
    private List<Record> records;
    private String[] columnNames = {"销售日期", "销售时间", "流水号", "员工编号", "产品编号", "销售数量", "销售金额"};

    public RecordTableModel (List<Record> records) {
        this.records = records;
    }

    public int getRowCount() {
        return records.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Record record = records.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return record.getDate();
            case 1:
                return record.getTime();
            case 2:
                return record.getSerialNumber();
            case 3:
                return record.getEmpId();
            case 4:
                return record.getProId();
            case 5:
                return record.getNum();
            case 6:
                return record.getAmount();
            default:
                return null;
        }
    }
}
