import javax.swing.*;

class ExceptionDialog extends JDialog {
    public ExceptionDialog(JFrame parent, String title, String message) {
        super(parent, title, true);

        // 创建异常对话框的内容面板
        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        panel.add(label);

        // 设置异常对话框的属性
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}

public class ExceptionDialogExample {
    public static void main(String[] args) {
        try {
            // 模拟抛出异常
            throw new RuntimeException("An error occurred.");
        } catch (RuntimeException e) {
            // 创建异常对话框，并显示异常信息
            ExceptionDialog dialog = new ExceptionDialog(null, "Error", e.getMessage());
        }
    }
}
