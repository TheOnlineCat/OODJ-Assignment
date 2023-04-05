import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;




public class Gui{

    private JFrame window;
    private JPanel headerPanel;
    private JButton backButton;
    private JButton settingButton;

    private ImageIcon back;

    public Gui(String Title) {
        window = new JFrame(Title);
        headerPanel = new JPanel();

        backButton = new JButton();

        // // to remote the spacing between the image and button's borders
        // backButton.setMargin(new Insets(0, 0, 0, 0));
        // // to remove the border
        // backButton.setBorder(null);


        if (FileHandler.Exists("Resources\\340.png")) {
            ImageIcon icon =new ImageIcon("Resources\\340.png");
            Image newimg = icon.getImage().getScaledInstance(30,30, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newimg);
            backButton.setIcon(icon);
            backButton.setBorder(BorderFactory.createEmptyBorder());
            backButton.setContentAreaFilled(false);
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("back");
                }
            });
        }
            

        headerPanel.add(backButton);
        window.add(headerPanel);
        window.setSize(300, 200);
        window.setVisible(true);

    }

    public void SetName(String name){
        window.setName(name);
    }

    

}
