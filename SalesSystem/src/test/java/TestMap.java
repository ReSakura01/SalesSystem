import java.util.Map;
import java.util.TreeMap;

public class TestMap {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new TreeMap<>();
        int a = 1;
        map.put (3, 3);
        map.put (3, map.get(3) + a);
        System.out.println(map.get(3) + a);
    }
}
