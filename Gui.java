import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;

public class Gui extends JFrame{

    private JPanel headerPanel;
    private JLabel titleLabel;
    private JButton backButton;
    private JComboBox<String> menuBox;

    public Gui(String Title) {
        this.setTitle(Title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        headerPanel = new JPanel();
        titleLabel = new JLabel("Hostel 101");
        titleLabel.setFont(new Font("Times New Roman", 1, 30));
        headerPanel.add(titleLabel);

        this.add(headerPanel);

        //this.setUndecorated(true);
        //this.setShape(new RoundRectangle2D.Double(10, 10, 100, 100, 50, 50));

        this.setResizable(false);
        this.setMinimumSize(new Dimension(520,420));
        this.setPreferredSize(new Dimension(520,420));
        this.setLocationRelativeTo(null);
        this.setVisible(true);  
    }

    

    // private void SwitchWindow(Panel window){
    //     //Create Button
    //     backButton = new JButton();
    //     if (FileHandler.Exists("Resources\\menu.png")) {
    //         this.setButtonIcon(backButton, new ImageIcon("Resources\\menu.png"), 30, 30); 
    //     }
        
    //     backButton.addActionListener(new ActionListener() {
    //         public void actionPerformed(ActionEvent e) {
    //             menuBox.setVisible(!menuBox.isVisible());
    //         }
    //     });
    //     headerPanel.add(backButton, BorderLayout.WEST);


    //     window.setVisible(true);
    //     this.dispose();
    // }

    public static JButton setButtonIcon(JButton button, ImageIcon icon, int width, int height) {
        Image sizedimg = icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(sizedimg);
        button.setIcon(icon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        return(button);
    }

    private JComboBox<String> createMenu(String[] choices) {

        menuBox = new JComboBox<String>(choices);
        menuBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Gui newWindow = new Gui("HostelApplication");
                //SwitchWindow();
                System.out.println("switch");
            }
        });

        menuBox.setVisible(false);
        return(menuBox);
    }
}
