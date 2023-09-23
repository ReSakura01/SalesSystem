import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.pyq.Emploee.Employee;

import java.util.ArrayList;
import java.util.List;

public class TestFast {
    public static void main(String[] args) {
        Employee employee = new Employee(1, "PYQ", "男", 0);

        // 将对象转为json
        String jsonString = JSON.toJSONString(employee);

        // {"id":1,"name":"PYQ","sex":"男"}
        System.out.println(jsonString);

        String str = "{\"id\":1,\"name\":\"PYQ\",\"sex\":\"男\"}";
        // 将json转为对象
        Employee employee1 = JSON.parseObject(str, Employee.class);

        // 将集合转化为json
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add (new Employee(1, "YYN", "女", 0));
        employees.add (new Employee(2, "MKJ", "男", 0));
        employees.add (new Employee(3, "PYQ", "男", 0));

        String jsonString1 = JSON.toJSONString(employees);

        System.out.println(jsonString1);

        String str1 = "[{\"id\":1,\"name\":\"YYN\",\"sex\":\"女\"},{\"id\":2,\"name\":\"MKJ\",\"sex\":\"男\"},{\"id\":3,\"name\":\"PYQ\",\"sex\":\"男\"}]";

        // 将json 转为集合
        List<Employee> employees1 = JSON.parseArray(str1, Employee.class);

        System.out.println(employees1);
    }
}
