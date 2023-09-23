import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestRead {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("test.json"));
            String line;
            while ((line = reader.readLine()) != null) {
                // 处理每一行的文本
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            // 处理读取文件时的异常
            e.printStackTrace();
        }

    }
}
