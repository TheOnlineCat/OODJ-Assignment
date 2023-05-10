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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;


public class Student extends User{ 
    
    private String gender;
    private int age;
    private String mail;
    public static final String[] DETAILS = {
                                            "Name",
                                            "Gender",
                                            "Age",
                                            "Email",
                                            "Username"
                                            };

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Student(ArrayList<String> userDetails) {
        super(userDetails);
    }

    public Student(String username) {
        super(username);
        FileHandler fileHandler = new FileHandler("Users.txt");
        Map<String, ArrayList<String>> userDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        for(String key : userDict.keySet()) {
            if(username.equals(key)) {
                loadInfo(userDict.get(key));
            }
        }
    }

    @Override
    public void loadInfo(ArrayList<String> data) {
        setName(data.get(2));
        gender = data.get(3);
        age = Integer.parseInt( data.get(4) );
        mail = data.get(5);
    }

    public ArrayList<String> getInfo() {
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(
            getName(), 
            gender, 
            Integer.toString(age), 
            mail
            ));
        return list;
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
        JComboBox<String> paxField;
        JTextField nameField, mailField, arrivalDField, arrivalMField, arrivalYField, departureDField, departureMField, departureYField;
        JTextArea specialField;
        JRadioButton roomFieldS, roomFieldM, roomFieldL;
        JButton clearButton;
        String inputName, inputMail, inputRoom, inputGuests, inputArrDDate, inputArrMDate, inputArrYDate, inputDepDDate, inputDepMDate, inputDepYDate;

        String inputAddReq;
        String selectedRoomText;
        String selectedItem;

        ButtonGroup group;

        Map<String, ArrayList<String>> records, history;
        DefaultTableModel model, model2;
        

        Dimension labelSize = new Dimension(120, 20);
        Dimension dateSize = new Dimension(20, 20);
        Dimension dateYearSize = new Dimension(50, 20);

        Font placeholder = new Font("Times New Romans",Font.ITALIC,12); 
        Font text = new Font("Times New Romans",Font.PLAIN,12); 

        private JPanel appHistory(){
            FileHandler fileHandler = new FileHandler("Applications.txt");
            history = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        
            String columns2[] = {"Hostel ID","Room Size", "Username", "Status", "Payment", "Arrival Date", "Departure Date"};
            model2 = createHistoryModel(columns2);
            loadHistory(history, model2);

            JPanel historyPanel = new JPanel();
            JTable appHistoryTable = new JTable(model2);
            appHistoryTable.getTableHeader().setReorderingAllowed(false); // Disable column reordering
            appHistoryTable.getTableHeader().setResizingAllowed(false);
            JScrollPane scrollPane = new JScrollPane(appHistoryTable);
            historyPanel.add(scrollPane);

            return historyPanel;
        }
        
        private void loadHistory(Map<String, ArrayList<String>> history, DefaultTableModel model2){
            model2.setRowCount(0); // Clear existing data in the table
            int i = 0;
            String currentUser = getUsername();
            for (String key : history.keySet()) {
                ArrayList<String> details = history.get(key);
                if (details.size() >= 6 && details.get(1).equals(currentUser)) {
                    String[] rowData = {
                        key, // hostel id
                        details.get(0), // room size
                        details.get(1), // username
                        details.get(2), // status
                        details.get(3), // payment status
                        details.get(4), // date of arrival
                        details.get(5) // date of departure
                        };
                    
                    model2.addRow(rowData);
                    i++;
                }
            }
           
        }

        private JPanel availableRooms() {
            JPanel hostelPanel = new JPanel();
        
            FileHandler fileHandler = new FileHandler("Hostels.txt");
            records = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        
            String columns[] = {"Hostel ID","Room Size","Availability","Price"};
            model = createTableModel(columns);
            loadHostelTable(records, model);
        
            JTable hostelRooms = new JTable(model);
            
            hostelRooms.getTableHeader().setReorderingAllowed(false);// Disable column reordering
            hostelRooms.getTableHeader().setResizingAllowed(false);
        
            JScrollPane scrollPane = new JScrollPane(hostelRooms);
            hostelPanel.add(scrollPane);
        
            return hostelPanel;
        }

        private DefaultTableModel createTableModel(String[] columns) {
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells read-only
                }
            };
            for(String each: columns){
                model.addColumn(each);
            }
            return model;
        }

        private DefaultTableModel createHistoryModel(String[] columns2){
            DefaultTableModel model2 = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells read-only
                }
            };
            for(String each : columns2){
                model2.addColumn(each);
            }
            return model2;
        }
        
        
        private void loadHostelTable(Map<String, ArrayList<String>> records, DefaultTableModel model) {
            model.setRowCount(0); // Clear existing data in the table
            int i = 0;
            for (String key : records.keySet()) {
                ArrayList<String> details = records.get(key);
                if (details.size() >= 3) {
                    String[] rowData = {
                        key, // hostel id
                        details.get(0), // room size
                        details.get(1), // availability
                        details.get(2) // price
                    };
                    
                    model.addRow(rowData);
                    i++;
                }
            }
        } 


        public StudentPanel(){  
            //FORM 1 (LEFT)
            JTabbedPane tabbed = new JTabbedPane();
            
            JPanel applicationPanel = new JPanel(); //first tabbed panel
            applicationPanel.setLayout(new BorderLayout());

            JPanel form2Panel = createRightForm();
            JPanel formPanel = createLeftForm();

            applicationPanel.add(formPanel, BorderLayout.WEST);
            applicationPanel.add(form2Panel, BorderLayout.EAST);
   
            JPanel availableRooms = new JPanel(); //second tabbed panel

            tabbed.addChangeListener(new ChangeListener() { //add the Listener
                public void stateChanged(ChangeEvent e) {
                    if(tabbed.getSelectedIndex() == 1){
                        JPanel hostelRooms = availableRooms();
                        availableRooms.removeAll();
                        availableRooms.add(hostelRooms);
                        availableRooms.revalidate();
                        availableRooms.repaint();
                    }
                }
            });

            JPanel applicationHistory = new JPanel(); //third tabbed panel
            applicationHistory.add(appHistory());
            
            tabbed.addTab("Make Hostel Application", applicationPanel);
            tabbed.addTab("Available Rooms", availableRooms);
            tabbed.addTab("Application History", applicationHistory);
            tabbed.setMinimumSize(new Dimension(500,300));
            tabbed.setPreferredSize(new Dimension(500,300));
            tabbed.setVisible(true);

            this.add(tabbed); //adds the tabbed panel to 'StudentPanel'
        }
        

        private JPanel createLeftForm() { //left panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setPreferredSize(new Dimension(250, 280));
            
            GridBagConstraints c = new GridBagConstraints();    
            c.insets = new Insets(0, 5, 20, 5);
            c.weightx = 1;
            c.weighty = 1;
            c.fill=GridBagConstraints.HORIZONTAL;

            JLabel usernameLabel = new JLabel("Username: ");
            usernameLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 0;
            formPanel.add(usernameLabel, c);


            JLabel usernameLabel2 = new JLabel(getUsername());
            usernameLabel2.setMinimumSize(labelSize);
            c.gridx = 1;
            c.gridy = 0;
            formPanel.add(usernameLabel2, c);
            
            JLabel nameLabel = new JLabel("Name: ");
            nameLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 1;
            formPanel.add(nameLabel, c);

            //name textfield
            nameField = new JTextField("Enter name here");
            nameField.setColumns(10);
            nameField.setFont(placeholder);
            nameField.setForeground(Color.DARK_GRAY);
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

            JLabel mailLabel = new JLabel("E-mail: ");
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

            
            JLabel roomLabel = new JLabel("Room Type: ");
            roomLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 3;
            formPanel.add(roomLabel,c);


            //radio buttons under another panel(integrated onto left panel)
            JPanel radioPanel = new JPanel(new GridLayout(3,1));
            roomFieldS = new JRadioButton("Small");
            roomFieldM = new JRadioButton("Medium");
            roomFieldL = new JRadioButton("Large");

            
            group = new ButtonGroup();
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
            
            JLabel paxLabel = new JLabel("Number of guests: ");
            paxLabel.setMinimumSize(labelSize);
            c.gridx = 0;
            c.gridy = 4;
            formPanel.add(paxLabel, c);

            String[] guestNum = {"1", "2", "3", "3+"}; //feature: 3+ cannot book small room
            paxField = new JComboBox<>(guestNum);
            //paxField.setMinimumSize(new Dimension(100,20));
            c.gridx = 1;
            c.gridy = 4;
            formPanel.add(paxField, c);
         


            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    nameField.setText("Enter name here"); //clears textfield
                    nameField.setFont(text); //set text back to plain instead of italics
    
                    mailField.setText("Enter e-mail here");
                    mailField.setFont(text);

                    arrivalDField.setText("DD");
                    arrivalMField.setText("MM");
                    arrivalYField.setText("YYYY");

                    arrivalDField.setFont(text);
                    arrivalMField.setFont(text);
                    arrivalYField.setFont(text);

                    departureDField.setText("DD");
                    departureMField.setText("MM");
                    departureYField.setText("YYYY");
                    
                    departureDField.setFont(text);
                    departureMField.setFont(text);
                    departureYField.setFont(text);
    
                    specialField.setText("Enter additional request here ");
                    specialField.setFont(text);
    
                    group.clearSelection();
                    

    
                    paxField.setSelectedIndex(0);
                }});


            return(formPanel);
        }

        private JPanel createRightForm() { //right panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridBagLayout());
            formPanel.setPreferredSize(new Dimension(250, 280));
            GridBagConstraints c = new GridBagConstraints();
            //c.insets = new Insets(0, 5, 20, 5);
            c.weightx = 1;
            c.weighty = 1;
            

            JLabel arrivalLabel = new JLabel("Date of arrival: ");
            arrivalLabel.setMinimumSize(labelSize);
            arrivalLabel.setPreferredSize(labelSize);
            c.gridx = 0;
            c.gridy = 0;
            formPanel.add(arrivalLabel, c);

            JPanel datePanel = new JPanel(new GridBagLayout());
            arrivalDField = new JTextField("DD", 2);                  //change to three text box in panel
            arrivalDField.setMinimumSize(dateSize);
            arrivalDField.setPreferredSize(dateSize);
            arrivalDField.setFont(placeholder);
            arrivalDField.setForeground(Color.DARK_GRAY);
            c.gridx = 0;
            datePanel.add(arrivalDField, c);

            arrivalDField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(arrivalDField.getText().equals("DD")){
                        arrivalDField.setText("");
                        arrivalDField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (arrivalDField.getText().equals("")){
                        arrivalDField.setText("DD");
                        arrivalDField.setFont(placeholder);
                    }
                }
            });

            arrivalMField = new JTextField("MM", 2);     
            arrivalMField.setMinimumSize(dateSize);
            arrivalMField.setPreferredSize(dateSize);
            arrivalMField.setFont(placeholder);
            arrivalMField.setForeground(Color.DARK_GRAY);

            c.gridx = 1;
            datePanel.add(arrivalMField, c);
            
            arrivalMField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(arrivalMField.getText().equals("MM")){
                        arrivalMField.setText("");
                        arrivalMField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (arrivalMField.getText().equals("")){
                        arrivalMField.setText("MM");
                        arrivalMField.setFont(placeholder);
                    }
                }
            });


            arrivalYField  = new JTextField("YYYY", 4);
            arrivalYField.setMinimumSize(dateYearSize);
            arrivalYField.setPreferredSize(dateYearSize);
            arrivalYField.setFont(placeholder);
            arrivalYField.setForeground(Color.DARK_GRAY);
            c.gridx = 2;
            datePanel.add(arrivalYField, c);
            c.gridx = 1;
            formPanel.add(datePanel, c);



            arrivalYField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(arrivalYField.getText().equals("YYYY")){
                        arrivalYField.setText("");
                        arrivalYField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (arrivalYField.getText().equals("")){
                        arrivalYField.setText("YYYY");
                        arrivalYField.setFont(placeholder);
                    }
                }
            });
            
            JLabel departureLabel = new JLabel("Date of departure: ");
            departureLabel.setMinimumSize(labelSize);
            departureLabel.setPreferredSize(labelSize);
            c.gridx = 0;
            c.gridy = 1;
            formPanel.add(departureLabel, c);

            JPanel departureDatePanel = new JPanel(new GridBagLayout());
            departureDField = new JTextField("DD", 2);
            departureDField.setMinimumSize(dateSize);
            departureDField.setPreferredSize(dateSize);
            departureDField.setFont(placeholder);
            departureDField.setForeground(Color.DARK_GRAY);
            c.gridx = 0;
            c.gridy = 1;
            departureDatePanel.add(departureDField, c);
            departureDField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(departureDField.getText().equals("DD")){
                        departureDField.setText("");
                        departureDField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (departureDField.getText().equals("")){
                        departureDField.setText("DD");
                        departureDField.setFont(placeholder);
                    }
                }
            });

            departureMField = new JTextField("MM", 2);
            departureMField.setMinimumSize(dateSize);
            departureMField.setPreferredSize(dateSize);
            departureMField.setFont(placeholder);
            departureMField.setForeground(Color.DARK_GRAY);
            c.gridx = 1;
            departureDatePanel.add(departureMField, c);
            departureMField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(departureMField.getText().equals("MM")){
                        departureMField.setText("");
                        departureMField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (departureMField.getText().equals("")){
                        departureMField.setText("MM");
                        departureMField.setFont(placeholder);
                    }
                }
            });

            departureYField = new JTextField("YYYY", 4);
            departureYField.setMinimumSize(dateYearSize);
            departureYField.setPreferredSize(dateYearSize);
            departureYField.setFont(placeholder);
            departureYField.setForeground(Color.DARK_GRAY);
            c.gridx = 2;
            departureDatePanel.add(departureYField, c);
            departureDField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(departureDField.getText().equals("YYYY")){
                        departureDField.setText("");
                        departureDField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (departureDField.getText().equals("")){
                        departureDField.setText("YYYY");
                        departureDField.setFont(placeholder);
                    }
                }
            });
            c.gridx = 1;
            formPanel.add(departureDatePanel, c);

            JLabel specialLabel = new JLabel("Additional Request: ");
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
            reserveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    inputName = nameField.getText();
                    inputMail = mailField.getText();
                    for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
                        AbstractButton button = buttons.nextElement();
                        if (button.isSelected()) {
                            selectedRoomText = button.getText();
                            break;
                        }else selectedRoomText = null;
                    }
                    if (paxField != null){
                        selectedItem = (String) paxField.getSelectedItem();
                    }
                    inputArrDDate = arrivalDField.getText();
                    inputArrMDate = arrivalMField.getText();
                    inputArrYDate = arrivalYField.getText();

                    inputDepDDate = departureDField.getText();
                    inputDepMDate = departureMField.getText();
                    inputDepYDate = departureYField.getText();


                    inputAddReq = specialField.getText();

                    if (inputName == "Enter name Here" || inputName.isEmpty() ||
                        inputMail == "Enter" || inputMail.isEmpty() ||
                        selectedItem == null ||
                        selectedRoomText == null ||
                        inputArrDDate == null || inputArrDDate.isEmpty() ||
                        inputArrMDate == null || inputArrMDate.isEmpty() ||
                        inputArrYDate == null || inputArrYDate.isEmpty() ||
                        inputDepDDate == null || inputDepDDate.isEmpty() ||
                        inputDepMDate == null || inputDepMDate.isEmpty() ||
                        inputDepYDate == null || inputDepYDate.isEmpty()
                        ) {
                        JOptionPane.showMessageDialog(formPanel, "Please fill in all fields!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JDialog cfmDialog = createReservationForm();
                        cfmDialog.setVisible(true);
                       
                    }
                }

            });
            formPanel.add(reserveButton, c);

            return(formPanel);
        }

        private JDialog createReservationForm() {
            
            JDialog cfmDialog = new JDialog();
            cfmDialog.setTitle("Confirm Reservation");
            cfmDialog.setPreferredSize(new Dimension(380, 400));        
            cfmDialog.setMinimumSize(new Dimension(380, 400));
            cfmDialog.setMaximumSize(new Dimension(380, 400));
            cfmDialog.setLocationRelativeTo(null);
            cfmDialog.setResizable(true);
            cfmDialog.setModal(true);


            JPanel cfmPanel = new JPanel(new GridBagLayout());  // set layout manager to GridBagLayout
            cfmPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            cfmPanel.setLayout(new GridBagLayout());
            cfmDialog.add(cfmPanel);  // add panel to dialog

            GridBagConstraints f = new GridBagConstraints();
            f.insets = new Insets(0, 5, 20, 5);
            JLabel nameLabel2 = new JLabel("Name: ");
            f.gridx = 0;
            f.gridy = 0;    
            nameLabel2.setMinimumSize(labelSize);
            nameLabel2.setPreferredSize(labelSize);
            cfmPanel.add(nameLabel2, f);

            JLabel nameField2 = new JLabel(inputName);
            f.gridx = 1;
            f.gridy = 0;    
            nameField2.setMinimumSize(labelSize);
            nameField2.setPreferredSize(labelSize);
            cfmPanel.add(nameField2, f);

            JLabel mailLabel2 = new JLabel("E-Mail: ");
            f.gridx = 0;            
            f.gridy = 1;           
            mailLabel2.setMinimumSize(labelSize);
            mailLabel2.setPreferredSize(labelSize); 
            cfmPanel.add(mailLabel2, f);        

            JLabel mailField2 = new JLabel(inputMail);
            f.gridx = 1;
            f.gridy = 1;    
            mailField2.setMinimumSize(labelSize);
            mailField2.setPreferredSize(labelSize);
            cfmPanel.add(mailField2, f);

            JLabel roomLabel2 = new JLabel("Room Type: ");
            f.gridx = 0;
            f.gridy = 2;
            roomLabel2.setMinimumSize(labelSize);
            roomLabel2.setPreferredSize(labelSize);
            cfmPanel.add(roomLabel2, f);

            JLabel selectedRoomLabel = new JLabel(selectedRoomText);
            f.gridx = 1;
            f.gridy = 2;    
            selectedRoomLabel.setMinimumSize(labelSize);
            selectedRoomLabel.setPreferredSize(labelSize);
            cfmPanel.add(selectedRoomLabel, f);

            JLabel paxLabel2 = new JLabel("Number of guests: ");
            f.gridx = 0;
            f.gridy = 3;
            paxLabel2.setMinimumSize(labelSize);
            paxLabel2.setPreferredSize(labelSize);
            cfmPanel.add(paxLabel2, f);

            JLabel paxField2 = new JLabel(selectedItem);
            f.gridx = 1;
            f.gridy = 3;
            paxField2.setMinimumSize(labelSize);
            paxField2.setPreferredSize(labelSize);
            cfmPanel.add(paxField2, f);

            JLabel arrivalLabel2 = new JLabel("Date of arrival: ");
            f.gridx = 0;
            f.gridy = 4;
            arrivalLabel2.setMinimumSize(labelSize);
            arrivalLabel2.setPreferredSize(labelSize);
            cfmPanel.add(arrivalLabel2, f);

            JLabel arrivalDField2 = new JLabel(inputArrDDate);
            f.gridx = 1;
            f.gridy = 4;    
            arrivalDField2.setMinimumSize(dateSize);
            arrivalDField2.setPreferredSize(dateSize);
            cfmPanel.add(arrivalDField2, f);

            JLabel arrivalMField2 = new JLabel(inputArrMDate);
            f.gridx = 2;
            f.gridy = 4;    
            arrivalMField2.setMinimumSize(dateSize);
            arrivalMField2.setPreferredSize(dateSize);
            cfmPanel.add(arrivalMField2, f);

            JLabel arrivalYField2 = new JLabel(inputArrYDate);
            f.gridx = 3;
            f.gridy = 4;    
            arrivalYField2.setMinimumSize(dateYearSize);
            arrivalYField2.setPreferredSize(dateYearSize);
            cfmPanel.add(arrivalYField2, f);

            JLabel departureLabel2 = new JLabel("Date of departure: ");
            f.gridx = 0;
            f.gridy = 5;
            departureLabel2.setMinimumSize(labelSize);
            departureLabel2.setPreferredSize(labelSize);
            cfmPanel.add(departureLabel2, f);

            JLabel departureDField2 = new JLabel(inputDepDDate);
            f.gridx = 1;
            f.gridy = 5;    
            departureDField2.setMinimumSize(dateSize);
            departureDField2.setPreferredSize(dateSize);
            cfmPanel.add(departureDField2, f);

            JLabel departureMField2 = new JLabel(inputDepMDate);
            f.gridx = 2;
            f.gridy = 5;    
            departureMField2.setMinimumSize(dateSize);
            departureMField2.setPreferredSize(dateSize);
            cfmPanel.add(departureMField2, f);

            JLabel departureYField2 = new JLabel(inputDepYDate);
            f.gridx = 3;
            f.gridy = 5;    
            departureYField2.setMinimumSize(dateYearSize);
            departureYField2.setPreferredSize(dateYearSize);
            cfmPanel.add(departureYField2, f);

            JLabel specialLabel2 = new JLabel("Additional Request: ");
            f.gridx = 0;
            f.gridy = 6;
            specialLabel2.setMinimumSize(labelSize);
            specialLabel2.setPreferredSize(labelSize);
            cfmPanel.add(specialLabel2, f);

            JLabel specialField2 = new JLabel(inputAddReq);
            f.gridx = 1;
            f.gridy = 6;    
            specialField2.setMinimumSize(labelSize);
            specialField2.setPreferredSize(labelSize);
            cfmPanel.add(specialField2, f);

            JButton cancelButton = new JButton("Cancel", null);
            f.gridx = 0;
            f.gridy = 7; 
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Window window = SwingUtilities.getWindowAncestor(cfmPanel);
                    window.dispose();
                }
            });
            cfmPanel.add(cancelButton, f);

            JButton cfmButton = new JButton("Confirm", null);
            f.gridx = 1;
            f.gridy = 7; 
            cfmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                }
            });
            cfmPanel.add(cfmButton, f);

            return(cfmDialog);
        }
    }
        

        
            
            

        

        

        private void saveApplication(){
            FileHandler fileHandler = new FileHandler("myFile.txt");
        Map<String, ArrayList<String>> myData = new HashMap<>();
        // add data to myData map
        fileHandler.save(myData);
        }

    }


        

