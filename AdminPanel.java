import java.util.ArrayList;

import javax.swing.*;

public class AdminPanel extends JPanel{

    Admin admin;

    public AdminPanel(ArrayList<String> userDetails) {

        admin = new Admin(userDetails);

        JLabel title = new JLabel(admin.getName());
        this.add(title);
    }
}
