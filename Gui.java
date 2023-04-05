import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.lang.Class.*;


public class Gui extends JFrame{

    private JFrame window;
    private JPanel headerPanel;
    private JButton backButton;
    private JComboBox<String> menuBox;
    private String[] choices = {"Profile", "Hostel Applications"};

    private JFrame newWindow;
    //private JFrame[] childrenFrames;

    public Gui(String Title) {
        this.setName(Title);;
        headerPanel = new JPanel();
        backButton = new JButton();


        //Create Button
        if (FileHandler.Exists("Resources\\menu.png")) {
            this.setButtonIcon(backButton, new ImageIcon("Resources\\menu.png"), 30, 30); 
        }
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuBox.setVisible(!menuBox.isVisible());
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(createMenu(choices));
        
        

        //Window Parameters
        this.add(headerPanel, BorderLayout.WEST);
        this.setBackground(new Color(173, 216, 230));
        this.setSize(500, 400);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

    }

    private JComboBox<String> createMenu(String[] choices) {
        menuBox = new JComboBox<String>(choices);
        menuBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Gui newWindow = new Gui("HostelApplication");
                //SwitchWindow();
                System.out.println("switch")
            }
        });

        menuBox.setVisible(false);
        return(menuBox);
    }

    private void SwitchWindow(Gui window){
        window.setVisible(true);
        this.dispose();
    }

    protected void setButtonIcon(JButton button, ImageIcon icon, int width, int height) {
        Image sizedimg = icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(sizedimg);
        button.setIcon(icon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
    }

    // protected void SetName(String name){
    //     this.setName(name);
    // }
    

    

}
