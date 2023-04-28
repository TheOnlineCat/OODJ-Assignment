import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;


public class Student extends User{ 
    

    public Student(ArrayList<String> userDetails) {
        super(userDetails);
        getUsername();
        getName();
    }


    public static Map<String, ArrayList<String>> getStudent(Map<String, ArrayList<String>> usersDict) {
        Map<String, ArrayList<String>> studentMap = new HashMap<>();
        for(String key : usersDict.keySet()) {
            if(usersDict.get(key).get(1).equals("STUDENT")) {
                studentMap.put(key, usersDict.get(key));
            }
        }
        return(studentMap);        
    }


    
    
    //create confirmation methods, where i save all the updated data into the arraylist, 
    //then overwrite the map with the new 'arraylist' (filehandler 'write')

    public class StudentPanel extends JPanel{

        JPanel jPanel;
        JLabel jlabel;
        JLabel usernameLabel;
        JLabel usernameLabel2;
        JLabel nameLabel;
        JLabel mailLabel;
        JLabel roomLabel;
        JLabel paxLabel;
        JLabel arrivalLabel;
        JLabel departureLabel;
        JLabel specialLabel;
        JTextField nameField;
        JTextField mailField;
        JTextArea specialField;
        JRadioButton roomFieldS;
        JRadioButton roomFieldM;
        JRadioButton roomFieldL;
        JButton clearButton;

        Dimension labelSize = new Dimension(120, 20);
        Font placeholder = new Font("Times New Romans",Font.ITALIC,12); 
        Font text = new Font("Times New Romans",Font.PLAIN,12); 


        public StudentPanel(){  
            //FORM 1 (LEFT)
            JTabbedPane tabbed = new JTabbedPane();
            
            JPanel applicationPanel = new JPanel();
            applicationPanel.setLayout(new BorderLayout());

            JPanel form2Panel = createRightForm();
            JPanel formPanel = createLeftForm();

            applicationPanel.add(formPanel, BorderLayout.WEST);
            applicationPanel.add(form2Panel, BorderLayout.EAST);
            /* 
            
            
            formPanel.validate();
            formPanel.repaint();

            
            */

            //FORM 2 (RIGHT)
            
  

            JPanel availableRooms = new JPanel();



            
            JPanel applicationHistory = new JPanel();

   
            tabbed.addTab("Make Hostel Application", applicationPanel);
            tabbed.addTab("Available Rooms", availableRooms);
            tabbed.addTab("Application History", applicationHistory);
            tabbed.setMinimumSize(new Dimension(500,300));
            tabbed.setPreferredSize(new Dimension(500,300));
            tabbed.setVisible(true);

            this.add(tabbed); //adds the tabbed panel to 'StudentPanel'
        }
        

        private JPanel createLeftForm() {
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setPreferredSize(new Dimension(250, 280));
            formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            GridBagConstraints c = new GridBagConstraints();    
            c.insets = new Insets(0, 5, 20, 5);
            c.weightx = 1;
            c.weighty = 1;
            c.fill=GridBagConstraints.HORIZONTAL;

            usernameLabel = new JLabel("Username: ");
            usernameLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 0;
            formPanel.add(usernameLabel, c);


            usernameLabel2 = new JLabel(getUsername());
            usernameLabel2.setMinimumSize(labelSize);
            c.gridx = 1;
            c.gridy = 0;
            formPanel.add(usernameLabel2, c);
            
            nameLabel = new JLabel("Name: ");
            nameLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 1;
            formPanel.add(nameLabel, c);

            //name textfield
            nameField = new JTextField("Enter name here");
            nameField.setColumns(10);
            nameField.setFont(placeholder);
            nameField.setForeground(Color.DARK_GRAY);
            //nameField.setMinimumSize(new Dimension(100,20));
            c.gridx = 1;
            c.gridy = 1;
            nameField.addFocusListener(new FocusListener() {  //when clicked, placeholder dissappears, when clicked away, placeholder reappears.
                @Override
                public void focusGained(FocusEvent event){
                    if(nameField.getText().equals("Enter name here")){
                        nameField.setText("");
                        nameField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (nameField.getText().equals("")){
                        nameField.setText("Enter name here");
                        nameField.setFont(placeholder);
                    }
                }
            });
            formPanel.add(nameField, c);

            mailLabel = new JLabel("E-mail: ");
            mailLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 2;
            c.anchor = GridBagConstraints.NORTH; // set anchor to the top
            formPanel.add(mailLabel, c);

            //mail text field
            mailField = new JTextField("Enter e-mail here");
            //mailField.setMinimumSize(new Dimension(100,20));
            mailField.setFont(placeholder);
            mailField.setForeground(Color.DARK_GRAY);
            c.gridx = 1;
            c.gridy = 2;
            mailField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(mailField.getText().equals("Enter e-mail here")){
                        mailField.setText("");
                        mailField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (mailField.getText().equals("")){
                        mailField.setText("Enter e-mail here");
                        mailField.setFont(placeholder);
                    }
                }
            });
            formPanel.add(mailField, c);

            
            roomLabel = new JLabel("Room Type: ");
            roomLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 3;
            formPanel.add(roomLabel,c);


            //radio buttons under another panel(integrated onto left panel)
            JPanel radioPanel = new JPanel(new GridLayout(3,1));
            roomFieldS = new JRadioButton("Small");
            roomFieldM = new JRadioButton("Medium");
            roomFieldL = new JRadioButton("Large");
            ButtonGroup group = new ButtonGroup();
            group.add(roomFieldS);
            group.add(roomFieldM);
            group.add(roomFieldL);


            radioPanel.add(roomFieldS,c);
            radioPanel.add(roomFieldM,c);
            radioPanel.add(roomFieldL,c);
            c.gridx = 1;
            c.gridy = 3;
            c.anchor = GridBagConstraints.CENTER; // set anchor to the center
            formPanel.add(radioPanel, c);


            
            paxLabel = new JLabel("Number of guests: ");
            paxLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 4;
            formPanel.add(paxLabel, c);

            String[] guestNum = {"1", "2", "3", "3+"}; //feature: 3+ cannot book small room
            JComboBox<String> paxField = new JComboBox<>(guestNum);
            //paxField.setMinimumSize(new Dimension(100,20));
            c.gridx = 1;
            c.gridy = 4;
            formPanel.add(paxField, c);


            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    nameField.setText(" "); //clears textfield
                    nameField.setFont(text); //set text back to plain instead of italics
    
                    mailField.setText(" ");
                    mailField.setFont(text);
    
                    specialField.setText(" ");
                    specialField.setFont(text);
    
                    group.clearSelection();
    
                    paxField.setSelectedIndex(0);
                }});


            return(formPanel);
        }

        private JPanel createRightForm() {
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setPreferredSize(new Dimension(250, 280));
            formPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(0, 5, 20, 5);
            c.weightx = 1;
            c.weighty = 1;
            

            arrivalLabel = new JLabel("Date of arrival: ");
            arrivalLabel.setMinimumSize(labelSize);
            arrivalLabel.setPreferredSize(labelSize);
            arrivalLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            c.gridx = 0;
            c.gridy = 0;
            formPanel.add(arrivalLabel, c);

            departureLabel = new JLabel("Date of departure: ");
            departureLabel.setMinimumSize(labelSize);
            departureLabel.setPreferredSize(labelSize);
            departureLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            c.gridx = 0;
            c.gridy = 1;
            formPanel.add(departureLabel, c);

            specialLabel = new JLabel("Additional Request: ");
            specialLabel.setMinimumSize(labelSize);
            specialLabel.setPreferredSize(labelSize);
            c.gridx = 0;
            c.gridy = 2;
            c.anchor = GridBagConstraints.NORTH; // set anchor to the top
            formPanel.add(specialLabel, c);

           
            //special requests text field
            specialField = new JTextArea("Enter additional request here");
            specialField.setRows(5);
            specialField.setColumns(9);
            specialField.setFont(placeholder);
            specialField.setLineWrap(true);
            specialField.setWrapStyleWord(true);
            specialField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(specialField.getText().equals("Enter additional request here")){
                        specialField.setText("");
                        specialField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (specialField.getText().equals("")){
                        specialField.setText("Enter additional request here");
                        specialField.setFont(placeholder);
                    }
                }
            });
            JScrollPane reqScroll = new JScrollPane(specialField);
            reqScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            c.gridx = 1;
            c.gridy = 2;
            formPanel.add(reqScroll, c);

            c.insets = new Insets(35, 5, 15, 5);
            clearButton = new JButton("Clear", null);
            clearButton.setPreferredSize(new Dimension(100, 30));
            c.gridx = 0;
            c.gridy = 4;
            
            formPanel.add(clearButton, c);

            JButton reserveButton = new JButton("Apply", null);
            reserveButton.setPreferredSize(new Dimension(100, 30));
            c.gridx = 1;
            c.gridy = 4;
            formPanel.add(reserveButton, c);
            reserveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JDialog cfmDialog = createReservationForm();
                    cfmDialog.setVisible(true);
                }
            });

            return(formPanel);
        }

        private JDialog createReservationForm() {
            JDialog cfmDialog = new JDialog();
            cfmDialog.setPreferredSize(new Dimension(380, 400));        
            cfmDialog.setMinimumSize(new Dimension(380, 400));
            cfmDialog.setMaximumSize(new Dimension(380, 400));
            cfmDialog.setResizable(false);

            GridBagConstraints f = new GridBagConstraints();  
            cfmDialog.setModal(true);
            f.gridx = 0;
            f.gridy = 0;    
            JLabel nameLabel2 = new JLabel("Name: ");
            cfmDialog.add(nameLabel2);

            JLabel mailLabel2 = new JLabel("E-Mail: ");
            f.gridy = 1;            
            cfmDialog.add(mailLabel2);                                               

            JLabel roomLabel2 = new JLabel("Room Type: ");
            f.gridy = 2;
            cfmDialog.add(roomLabel2);

            JLabel paxLabel2 = new JLabel("Number of guests: ");
            f.gridy = 3;
            cfmDialog.add(paxLabel2);

            JLabel arrivalLabel2 = new JLabel("Date of arrival: ");
            f.gridy = 4;
            cfmDialog.add(arrivalLabel2);

            JLabel departureLabel2 = new JLabel("Date of departure: ");
            f.gridy = 5;
            cfmDialog.add(departureLabel2);

            JLabel specialLabel2 = new JLabel("Additional Request: ");
            f.gridy = 6;
            cfmDialog.add(specialLabel2);
            cfmDialog.setLocationRelativeTo(null);

            return(cfmDialog);
        }
    }
    

    
    // public void actionPerformed(ActionEvent e){       *to change the price label when different option is selected.
    //     if (e.getSource() == roomFieldS){             

    //     }
    //     else if (e.getSource() == roomFieldM){

    //     }
    //     else if (e.getSource() == roomFieldL){

    //     }
                  
    // }




           



    }

        

