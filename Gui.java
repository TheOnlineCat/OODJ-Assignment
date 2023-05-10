import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

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
    public static JPanel createRecordButton(String[] details) {
        int rowSize = 460;
        JPanel recordPanel = new JPanel();
        recordPanel.setBorder(BorderFactory.createMatteBorder(2, 1, 2, 1, Color.GRAY));
        recordPanel.setMaximumSize(new Dimension(rowSize, 20));
        recordPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridBagLayout());
        textPanel.setMaximumSize(new Dimension(700, 20));
        GridBagConstraints c = new GridBagConstraints();
        //c.insets = new Insets(3, 3, 3, 3);
        c.gridx = 0;
        c.gridy = 0;
        for(int i = 0; i < details.length; i++) {
            JLabel label = new JLabel(details[i]);
            label.setName(Integer.toString(i));
            label.setMinimumSize(new Dimension(rowSize/details.length,16));
            label.setPreferredSize(new Dimension(rowSize/details.length, 16));
            label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            textPanel.add(label, c);
            c.gridx += 1;
            
        }
        recordPanel.add(textPanel);
        return(recordPanel);
    }

    public static void displayLabelGrid(JPanel panelRef, ArrayList<String> info, String[] labels, int start) {
        Dimension labelSize = new Dimension(0, 20);
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        for(int idx = start; idx < info.size(); idx++) {
            c.gridy = idx;
            c.gridx = 0;
            c.weightx = 1;
            labelSize.width = 20;
            JLabel leftLabel = new JLabel(labels[idx] + ": ");
            leftLabel.setPreferredSize(labelSize);
            panelRef.add(leftLabel, c);

            c.gridx = 1;
            c.weightx = 4;
            labelSize.width = 130;
            JLabel rightLabel = new JLabel(info.get(idx), SwingConstants.RIGHT);
            rightLabel.setPreferredSize(labelSize);
            panelRef.add(rightLabel, c);

            panelRef.validate();
        } 
    }

    public static void displayFormGrid(JPanel panelRef, ArrayList<String> initialInfo, String[] labels, int start) {
        Dimension labelSize = new Dimension(90, 20);
        Dimension textFieldSize = new Dimension(150, 20);

        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        for(int idx = start; idx < initialInfo.size(); idx++) {
            c.gridy = idx;
            c.gridx = 0;
            c.weightx = 1;
            //c.insets = new Insets(0, 0, 0, 2);
            
            JLabel leftLabel = new JLabel(labels[idx] + ": ", SwingConstants.RIGHT);
            leftLabel.setMinimumSize(labelSize);
            leftLabel.setMaximumSize(labelSize);
            leftLabel.setPreferredSize(labelSize);
            //leftLabel.setBorder(BorderFactory.createBevelBorder(0));
            panelRef.add(leftLabel, c);

            c.gridx = 1;
            c.weightx = 4;
            JTextField rightField = new JTextField(initialInfo.get(idx));
            rightField.setPreferredSize(textFieldSize);
            rightField.setMinimumSize(textFieldSize);
            rightField.setMaximumSize(textFieldSize);
            rightField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    rightField.setForeground(Color.BLACK);
                }

                @Override
                public void focusLost(FocusEvent e) {}
                
            });
            panelRef.add(rightField, c);

            panelRef.validate();
        } 
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

    // private JComboBox<String> createMenu(String[] choices) {

    //     menuBox = new JComboBox<String>(choices);
    //     menuBox.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             //Gui newWindow = new Gui("HostelApplication");
    //             //SwitchWindow();
    //             System.out.println("switch");
    //         }
    //     });

    //     menuBox.setVisible(false);
    //     return(menuBox);
    // }
}
