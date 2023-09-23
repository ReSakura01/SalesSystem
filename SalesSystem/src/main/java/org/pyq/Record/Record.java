package org.pyq.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Record {
    private LocalDate date;           // 销售日期
    private LocalTime time;           // 销售时间
    private String SerialNumber;      // 流水号
    private int EmpId;                // (Employee) 员工编号
    private int ProId;                // (Product) 产品编号
    private int num;                  // 销售数量
    private double amount;            // 销售金额

    public Record(LocalDate date, LocalTime time, String serialNumber, int empId, int proId, int num, double amount) {
        this.date = date;
        this.time = time;
        SerialNumber = serialNumber;
        EmpId = empId;
        ProId = proId;
        this.num = num;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Record{" +
                "date=" + date +
                ", time=" + time +
                ", SerialNumber='" + SerialNumber + '\'' +
                ", EmpId=" + EmpId +
                ", ProId=" + ProId +
                ", num=" + num +
                ", amount=" + amount +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public int getEmpId() {
        return EmpId;
    }

    public int getProId() {
        return ProId;
    }

    public int getNum() {
        return num;
    }

    public double getAmount() {
        return amount;
    }
}
