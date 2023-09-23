package org.pyq.Emploee;

public class Employee {
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

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", sum=" + sum +
                '}';
    }

    public void setSum (double sum) { this.sum = sum; }

    public void setName(String name) {
        this.name = name;
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

    public double getSum() { return sum; }
}