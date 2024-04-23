import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login Form");
        setSize(500, 250); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240)); // Set background color

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        // Set button colors
        loginButton.setBackground(new Color(50, 150, 250));
        loginButton.setForeground(Color.WHITE);
        signupButton.setBackground(new Color(50, 150, 250));
        signupButton.setForeground(Color.WHITE);

            loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "");

                    if (conn != null) { // Check if connection is successfully established
                        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);

                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            // Authentication successful
                            //String userEmail = rs.getString("email");
                            JOptionPane.showMessageDialog(Login.this, "Login Successful!\nRedirecting to Dashboard...");

                            GUIMain guiMain = new GUIMain();
                            GUIMain.main(new String[]{});
                            guiMain.setVisible(true);
                            dispose();

                        } else {
                            // Authentication failed
                            JOptionPane.showMessageDialog(Login.this, "Invalid username or password!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Failed to establish connection to the database!");
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Login.this, "Error: Login Failed!");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

            signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignupForm signupForm = new SignupForm();
                signupForm.setVisible(true);
                dispose();
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signupButton);

        add(panel);
    }

    public static void main(String[] args) {
        Login loginForm = new Login();
        loginForm.setVisible(true);
    }
}

class SignupForm extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignupForm() {
        setTitle("Signup Form");
        setSize(500, 300); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2)); // Increased rows
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240)); // Set background color

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField();
        emailField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton signupButton = new JButton("Signup");

        // Set button color
        signupButton.setBackground(new Color(50, 150, 250));
        signupButton.setForeground(Color.WHITE);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                Connection conn = null;
                PreparedStatement pstmt = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "");

                    if (conn != null) { // Check if connection is successfully established
                        String sql = "INSERT INTO users (name, email, username, password) VALUES (?, ?, ?, ?)";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, name);
                        pstmt.setString(2, email);
                        pstmt.setString(3, username);
                        pstmt.setString(4, password);

                        int rowsInserted = pstmt.executeUpdate();
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(SignupForm.this, "Signup Successful! Redirecting to Login Page...");
                            Login loginForm = new Login();
                            loginForm.setVisible(true);
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(SignupForm.this, "Signup Failed!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(SignupForm.this, "Failed to establish connection to the database!");
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignupForm.this, "Error: Signup Failed!");
                } finally {
                    try {
                        if (pstmt != null) pstmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signupButton);

        add(panel);
    }
}
