import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.*;


public class LoginPanel extends JPanel implements ActionListener{

    private JPanel detailsPanel;
    private JPanel buttonPanel;
    private JLabel userLabel;
    private JLabel passLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginButton;

    private Gui uiRef;

    //BoxLayout layout;


    public LoginPanel(Gui uiRef) {
        initialise();
        this.uiRef = uiRef;
    }
    
    private void initialise() {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        userLabel = new JLabel("Username: ");
        passLabel = new JLabel("Password: ");

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(100, 20));
        usernameField.setMaximumSize(new Dimension(100, 20));

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(100, 20));
        passwordField.setMaximumSize(new Dimension(100, 20));

        c.insets = new Insets(0, 0, 5, 0);
        c.gridx = 0;
        c.gridy = 1;
        detailsPanel.add(userLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        detailsPanel.add(usernameField, c);
        c.gridx = 0;
        c.gridy = 2;
        detailsPanel.add(passLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        detailsPanel.add(passwordField, c);
        
        this.add(detailsPanel);

        loginButton = new JButton("Login");
        loginButton.setActionCommand("LOGIN");
        loginButton.addActionListener(this);

        buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        this.add(buttonPanel);
        
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.LIGHT_GRAY);
    }

    

    public void actionPerformed(ActionEvent action) {
        if (action.getActionCommand() == "LOGIN") {
            login();
        }
    }

    private void login() {
        FileHandler fHandler = new FileHandler("Users.txt");
            Map<String, ArrayList<String>> records;

            records = fHandler.parseAsDict(fHandler.read(), FileHandler.SEPERATOR, 0);
            ArrayList<String> userLogin = records.get(usernameField.getText());
            
            if(userLogin == null) {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error Logging into Account", JOptionPane.ERROR_MESSAGE, null);
                return;
            }

            if(Arrays.equals(passwordField.getPassword(), userLogin.get(0).toCharArray()) ) {
                userLogin.add(usernameField.getText());

                switch (userLogin.get(1)) {
                    case "ADMIN":
                        createAdminPage(userLogin);
                        break;
                    case "STUDENT":
                        createStudentPage(usernameField.getText());
                        break;
                }
            }else{
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error Logging into Account", JOptionPane.ERROR_MESSAGE, null);
            }
    }

    private void createAdminPage(ArrayList<String> userDetails) {
        Admin admin = new Admin(userDetails);
        Admin.AdminPanel adminPanel = admin.new AdminPanel();
        uiRef.add(adminPanel);
        uiRef.remove(this);
        uiRef.validate();
        uiRef.repaint();
    }

    private void createStudentPage(String username){
        Student student = new Student(username);
        Student.StudentPanel studPanel = student.new StudentPanel();
        uiRef.add(studPanel);
        uiRef.remove(this);
        uiRef.validate();
        uiRef.repaint();
    }

}
