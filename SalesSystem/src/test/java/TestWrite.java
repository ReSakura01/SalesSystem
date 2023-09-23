import com.alibaba.fastjson.JSON;
import org.pyq.Emploee.Employee;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TestWrite {
    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add (new Employee(1, "YYN", "女", 0));
        employees.add (new Employee(2, "MKJ", "男", 0));
        employees.add (new Employee(3, "PYQ", "男", 0));

        // 创建JSON数据
        String jsonString = JSON.toJSONString(employees);

        try {
            // 写入JSON文件
            FileWriter writer = new FileWriter("test.json");
            writer.write(jsonString);
            writer.close();
            System.out.println("JSON数据已成功写入文件。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
