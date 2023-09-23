
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pyq.Emploee.Employee;

import java.io.IOException;
import java.util.ArrayList;

public class TestJackson {
    public static void main (String[] args) throws IOException {
        Employee employee1 = new Employee(1, "PYQ", "男", 0);

        ObjectMapper objectMapper = new ObjectMapper();

        // {"name":"PYQ","id":1,"sex":"男"}
        String s = objectMapper.writeValueAsString(employee1);

        System.out.println(employee1.getSex());
        System.out.println(s);

        // 将json赋给class
        String str = "{\"id\":2,\"name\":\"YYN\",\"sex\":\"女\"}";
        Employee employee2 = objectMapper.readValue(str, Employee.class);

        System.out.println(employee2.getName());

        // 将集合转化为json
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add (new Employee(1, "YYN", "女", 0));
        employees.add (new Employee(2, "MKJ", "男", 0));
        employees.add (new Employee(3, "PYQ", "男", 0));

        String s1 = objectMapper.writeValueAsString(employees);

        // [{"id":2,"name":"YYN","sex":"女"},{"id":3,"name":"MKJ","sex":"男"},{"id":4,"name":"PYQ","sex":"男"}]

        String str1 = "[{\"id\":2,\"name\":\"YYN\",\"sex\":\"女\"},{\"id\":3,\"name\":\"MKJ\",\"sex\":\"男\"},{\"id\":4,\"name\":\"PYQ\",\"sex\":\"男\"}]";

        Object o = objectMapper.readValue(str1, new TypeReference<ArrayList<Employee> >() {
        });

        System.out.println(o);
    }
}