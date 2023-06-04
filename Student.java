import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public Student(String username) {  //store the relevant info depending on the user that logged in
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
    public void loadInfo(ArrayList<String> data) { //override the admin 'info' to get all details of students (admin only gets username)
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
            mail,
            getUsername()
            ));
        return list;
    }

    public static Map<String, ArrayList<String>> getStudent(Map<String, ArrayList<String>> usersDict) { //gets the student specifically from a map of users
        Map<String, ArrayList<String>> studentMap = new HashMap<>();
        for(String key : usersDict.keySet()) {
            if(usersDict.get(key).get(1).equals("STUDENT")) {
                studentMap.put(key, usersDict.get(key));
            }
        }
        return(studentMap);        
    }

    public class StudentPanel extends JPanel{

        JPanel jPanel;
        JPanel paymentListPanel;
        JPanel historyListPanel;
        JLabel jlabel;
        JComboBox<String> paxField;
        JTextField nameField, mailField, arrivalDField, arrivalMField, arrivalYField, departureDField, departureMField, departureYField;
        JTextArea specialField;
        JRadioButton roomFieldS, roomFieldM, roomFieldL;
        JButton clearButton;
        String inputName, inputMail, inputRoom, inputGuests, inputArrDDate, inputArrMDate, inputArrYDate, inputDepDDate, inputDepMDate, inputDepYDate,ArrDate, DepDate;

        String inputAddReq;
        String selectedRoomText;
        String selectedItem;

        LocalDate arrDateParsed = null;
        LocalDate DepDateParsed = null;

        ButtonGroup group;

        Map<String, ArrayList<String>> records, paid;
        DefaultTableModel model, model2;
        

        Dimension labelSize = new Dimension(120, 20);
        Dimension dateSize = new Dimension(50, 20);
        Dimension dateYearSize = new Dimension(50, 20);


        Font placeholder = new Font("Times New Romans",Font.ITALIC,12); 
        Font text = new Font("Times New Romans",Font.PLAIN,12); 

        private JPanel payment(){
            
            JPanel paymentPanel = new JPanel();

            paymentListPanel = new JPanel();
            paymentListPanel.setLayout(new BoxLayout(paymentListPanel, BoxLayout.Y_AXIS));
            loadPayment(paymentListPanel);

            

            JScrollPane paymentScroll = new JScrollPane(paymentListPanel);
            paymentScroll.setPreferredSize(new Dimension(480, 260));
            paymentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            paymentPanel.add(paymentScroll);

            return paymentPanel;
        }

        private void loadPayment(JPanel panelRef){
            FileHandler fileHandler = new FileHandler("Applications.txt");
            Map<String, ArrayList<String>> payment;
            payment = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);

            String currentUser = getUsername();
            
            for (String key : payment.keySet()) {
                ArrayList<String> details = payment.get(key);

                if (details.get(1).equals(currentUser)) {
                    String[] paymentData = {
                        key, // hostel id
                        details.get(0), // room type
                        details.get(3), // username
                        details.get(4), // request status
                        details.get(5)}; // payment status

                    String[] paymentDetails = {
                        details.get(2),   
                        key,
                        details.get(5),
                        details.get(6)
                    };
        
                    Application application = new Application(key);
                    JPanel paymentButtonPanel = Gui.createRecordButton(paymentData);
                    paymentButtonPanel.addMouseListener(new MouseListener() {   
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            ArrayList<String> tempDetails = new ArrayList<String>();
                            tempDetails.add(key);
                            tempDetails.addAll(details);
                            
                            JDialog payDialog = new PaymentDialog(tempDetails) {
                                @Override
                                public void CreateConfirmation() {
                                    System.out.println();
                                    JDialog payConfirmDialog = new PaymentDialog(paymentDetails) {
                                        @Override
                                        public void CreateDialog() {
                                            JPanel paymentCheckDialogPanel = new JPanel();
                                            CreateForm(paymentCheckDialogPanel);
                                            JButton cfmPayButton = new JButton("Confirm Payment");
                                            cfmPayButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    FileHandler fileHandler = new FileHandler("Applications.txt");
                                                    paid = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
                                                    application.setPaidStatus("PAID");
                                                    application.Save();
                                                    refreshPayment(panelRef);
                                                    Close();
                                                }
                                            });
                                            paymentCheckDialogPanel.add(cfmPayButton);
                                            add(paymentCheckDialogPanel);
                                        }
                                        @Override
                                        void CreateForm(JPanel dialogPanel) {
                                            Gui.displayLabelGrid(dialogPanel, infoArray, labelArray, 0); 
                                        }
                                    };
                                }
                            };
                        }
                        @Override
                        public void mousePressed(MouseEvent e) {}

                        @Override
                        public void mouseReleased(MouseEvent e) {}

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            JPanel panel = (JPanel)paymentButtonPanel.getComponent(0);
                            for(Component labels : panel.getComponents()) {
                                labels.setForeground(Color.BLUE);
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            JPanel panel = (JPanel)paymentButtonPanel.getComponent(0);
                            for(Component labels : panel.getComponents()) {
                                labels.setForeground(Color.BLACK);
                            }
                        }
                    });

                    panelRef.add(paymentButtonPanel);
                }
            }
           
        }

        private class PaymentDialog extends DialogForm{
            PaymentDialog(ArrayList<String> details){
                infoArray = details.toArray(new String[details.size()]);
                labelArray = Application.DETAILS;
                CreateDialog();
                SetDialogProperties();
                DisplayDialog();
            }

            PaymentDialog(String[] paymentDetails){
                infoArray = paymentDetails;
                labelArray = new String[] {"Username", "Application ID", "Payment Status", "Price"};
                CreateDialog();
                SetDialogProperties();
                DisplayDialog();
            }

            @Override
            public void Close(){
                dispose();
            }

            public void CreateDialog() {
                JPanel paymentDialogPanel = new JPanel();
                CreateForm(paymentDialogPanel);
                if (infoArray[6].equals("UNPAID")){
                    JButton payButton = new JButton("Make Payment");
                    payButton.addActionListener(new ActionListener() {
                        @Override   
                        public void actionPerformed(ActionEvent e) {
                            CreateConfirmation();
                            Close();
                        }
                    });
                    paymentDialogPanel.add(payButton);
                }
                //DisplayDialog(); 
                add(paymentDialogPanel);
            }

            public void CreateConfirmation() { 
            }

            @Override
            void CreateForm(JPanel dialogPanel) {
                Gui.displayLabelGrid(dialogPanel, infoArray, labelArray, 0); //ref, content, headers, start index    
            }
            @Override
            void SetDialogProperties() {
                setPreferredSize(new Dimension(300, 280));
                setMinimumSize(new Dimension(300, 280));
                setLocationRelativeTo(null);
                setResizable(false);
            }
            @Override
            void DisplayDialog() {
                setVisible(true);
            }
        
        }

        private JPanel appHistory(){

            JPanel historyPanel = new JPanel();

            historyListPanel = new JPanel();
            historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
            loadHistory(historyListPanel);

            JScrollPane scrollPane = new JScrollPane(historyListPanel);
            scrollPane.setPreferredSize(new Dimension(480, 260));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            historyPanel.add(scrollPane);

            return historyPanel;
        }



       private class HistoryDialog extends DialogForm{
            HistoryDialog(String[] info){
                infoArray = info;
                labelArray = Application.DETAILS;
                JPanel dialogPanel = new JPanel();
                add(dialogPanel);
                CreateForm(dialogPanel);
                SetDialogProperties();
                DisplayDialog();
            }

            @Override
            void CreateForm(JPanel dialogPanel){
                Gui.displayLabelGrid(dialogPanel, infoArray, labelArray, 0);
                
            }
            @Override
            void SetDialogProperties() {
                setPreferredSize(new Dimension(300, 280));
                setMinimumSize(new Dimension(300, 280));
                setLocationRelativeTo(null);
                setResizable(false);
            }
            @Override
            void DisplayDialog() {
                setVisible(true);
            }

            @Override
            void Close() {
                dispose();
            }
        
        }
        
        
        private void loadHistory(JPanel panelRef) {
            FileHandler fileHandler = new FileHandler("Applications.txt");
            Map<String, ArrayList<String>> history;
            history = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
            String currentUser = getUsername();
            for (String key : history.keySet()) {
                ArrayList<String> details = history.get(key);
                if (details.get(1).equals(currentUser)) {
                    String[] rowData = {
                        key, // hostel id
                        details.get(1), // username
                        details.get(2), // status
                        details.get(3), // payment status
                    };
        
                    JPanel historyButtonPanel = Gui.createRecordButton(rowData);
                    details.add(0, key); // index=0 is the username, then the rest is the subsequent data
        
                    // Convert ArrayList<String> to String[]
                    String[] info = details.toArray(new String[0]);
        
                    historyButtonPanel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            DialogForm historyDialog = new HistoryDialog(info) {
                            };
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {}

                        @Override
                        public void mouseReleased(MouseEvent e) {}

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            JPanel panel = (JPanel)historyButtonPanel.getComponent(0);
                            for(Component labels : panel.getComponents()) {
                                labels.setForeground(Color.BLUE);
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            JPanel panel = (JPanel)historyButtonPanel.getComponent(0);
                            for(Component labels : panel.getComponents()) {
                                labels.setForeground(Color.BLACK);
                            }
                        }
                    });
                    panelRef.add(historyButtonPanel);
                }
            }
           
        }

        private void refreshPayment(JPanel panelRef) {
            panelRef.removeAll();
            loadPayment(panelRef);
            panelRef.validate();
            panelRef.repaint();
        }

        private void refreshHistory(JPanel panelRef) {
            panelRef.removeAll();
            loadHistory(panelRef);
            panelRef.validate();
            panelRef.repaint();
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


        
        private void loadHostelTable(Map<String, ArrayList<String>> records, DefaultTableModel model) {
            model.setRowCount(0); // Clear existing data in the table
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

            JPanel paymentTab = new JPanel(); //fourth tabbed panel
            paymentTab.add(payment());

            tabbed.addTab("Make Hostel Application", applicationPanel);
            tabbed.addTab("Available Rooms", availableRooms);
            tabbed.addTab("Application History", applicationHistory);
            tabbed.addTab("Payment", paymentTab);
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
            c.insets = new Insets(0, 5, 25, 5);
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

            String[] guestNum = {"1", "2", "3", "3+"}; 
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
            departureYField.addFocusListener(new FocusListener() {   
                @Override
                public void focusGained(FocusEvent event){
                    if(departureYField.getText().equals("YYYY")){
                        departureYField.setText("");
                        departureYField.setFont(text);
                    }
                }
                @Override
                public void focusLost(FocusEvent event){
                    if (departureYField.getText().equals("")){
                        departureYField.setText("YYYY");
                        departureYField.setFont(placeholder);
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
            DateTimeFormatter zdrFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); 
            LocalDate todayDate = LocalDate.parse(today,zdrFormatter);

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
                    String ArrDate = String.format("%02d-%02d-%04d", Integer.parseInt(inputArrDDate), Integer.parseInt(inputArrMDate), Integer.parseInt(inputArrYDate));
                    try {     //try catch to parse string to date
                        arrDateParsed = LocalDate.parse(ArrDate, zdrFormatter);
                        arrivalDField.setForeground(Color.BLACK);
                        arrivalMField.setForeground(Color.BLACK);
                        arrivalYField.setForeground(Color.BLACK);
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(formPanel, "Invalid date entered", "Error Message", JOptionPane.ERROR_MESSAGE);
                        arrivalDField.setForeground(Color.RED);
                        arrivalMField.setForeground(Color.RED);
                        arrivalYField.setForeground(Color.RED);
                        return;
                    }
                    inputDepDDate = departureDField.getText();
                    inputDepMDate = departureMField.getText();
                    inputDepYDate = departureYField.getText();
                    
                    String DepDate = String.format("%02d-%02d-%04d", Integer.parseInt(inputDepDDate), Integer.parseInt(inputDepMDate), Integer.parseInt(inputDepYDate));
                    try {
                        DepDateParsed = LocalDate.parse(DepDate, zdrFormatter);
                        departureDField.setForeground(Color.BLACK);
                        departureMField.setForeground(Color.BLACK);
                        departureYField.setForeground(Color.BLACK);
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(formPanel, "Invalid date entered", "Error Message", JOptionPane.ERROR_MESSAGE);
                        departureDField.setForeground(Color.RED);
                        departureMField.setForeground(Color.RED);
                        departureYField.setForeground(Color.RED);
                        return;
                    }

                    
                    if (arrDateParsed != null) {
                        if (arrDateParsed.compareTo(todayDate) < 0){
                            JOptionPane.showMessageDialog(formPanel, "Please enter a valid date (cannot enter an arrival date before the current date)", "Error Message", JOptionPane.ERROR_MESSAGE);
                            arrivalDField.setForeground(Color.RED);
                            arrivalMField.setForeground(Color.RED);
                            arrivalYField.setForeground(Color.RED);
                            return;
                        } else if (DepDateParsed != null) {
                            if (DepDateParsed.compareTo(todayDate) < 0) {
                                JOptionPane.showMessageDialog(formPanel, "Please enter a valid date (cannot enter a departure date before the current date)", "Error Message", JOptionPane.ERROR_MESSAGE);
                                departureDField.setForeground(Color.RED);
                                departureMField.setForeground(Color.RED);
                                departureYField.setForeground(Color.RED);
                                return;
                            } else if (DepDateParsed.compareTo(arrDateParsed) < 0) {
                                JOptionPane.showMessageDialog(formPanel, "Please enter a valid date (cannot enter a departure date before the arrival date)", "Error Message", JOptionPane.ERROR_MESSAGE);
                                departureDField.setForeground(Color.RED);
                                departureMField.setForeground(Color.RED);
                                departureYField.setForeground(Color.RED);
                                return;
                            }
                        }
                    }

                    inputAddReq = specialField.getText();
                    
                    if (inputName == "Enter name here" || inputName.isEmpty() || 
                        inputMail == "Enter e-mail here" || inputMail.isEmpty() ||
                        selectedItem == null ||
                        selectedRoomText == null ||
                        specialField.getText() == "Enter additional request here")
                        JOptionPane.showMessageDialog(formPanel, "Please fill in all fields!", "Error Message", JOptionPane.ERROR_MESSAGE);
                     else {
                        JDialog cfmDialog = createReservationForm();
                        cfmDialog.setVisible(true);
                    }    
                }});

            formPanel.add(reserveButton, c);
            
            return formPanel;
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

            JLabel arrivalField2 = new JLabel(inputArrDDate + "-" + inputArrMDate + "-" + inputArrYDate);
            f.gridx = 1;
            f.gridy = 4;    
            arrivalField2.setMinimumSize(labelSize);
            arrivalField2.setPreferredSize(labelSize);
            cfmPanel.add(arrivalField2, f);

            JLabel departureLabel2 = new JLabel("Date of departure: ");
            f.gridx = 0;
            f.gridy = 5;
            departureLabel2.setMinimumSize(labelSize);
            departureLabel2.setPreferredSize(labelSize);
            cfmPanel.add(departureLabel2, f);

            JLabel departureField2 = new JLabel(inputDepDDate + "-" + inputDepMDate + "-" + inputDepYDate);
            f.gridx = 1;
            f.gridy = 5;    
            departureField2.setMinimumSize(labelSize);
            departureField2.setPreferredSize(labelSize);
            cfmPanel.add(departureField2, f);

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

            JLabel priceLabel = new JLabel("Price: ");
            f.gridx = 0;
            f.gridy = 7;    
            priceLabel.setMinimumSize(labelSize);
            priceLabel.setPreferredSize(labelSize);
            cfmPanel.add(priceLabel, f);

            JLabel priceLabel2 = new JLabel();
            f.gridx = 1;
            f.gridy = 7; 
            switch (selectedRoomText) {
                case "Large":
                    priceLabel2.setText("1200");
                    break;
                case "Medium":
                    priceLabel2.setText("800");
                    break;
                case "Small":
                    priceLabel2.setText("500");
                    break;
                default:
                    System.out.println("selected room error");
                    break;
            }

            priceLabel2.setMinimumSize(labelSize);
            priceLabel2.setPreferredSize(labelSize);
            cfmPanel.add(priceLabel2, f);

            JButton cancelButton = new JButton("Cancel", null);
            f.gridx = 0;
            f.gridy = 8; 
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Window window = SwingUtilities.getWindowAncestor(cfmPanel);
                    window.dispose();
                }
            });
            cfmPanel.add(cancelButton, f);

        
            JButton payLaterButton = new JButton("Pay Later", null);
            f.gridx = 1;
            f.gridy = 8; 
            payLaterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Application application = new Application();


                    application.setRoomType(selectedRoomText);
                    application.setOccupant(getUsername());
                    application.setArrivalDate(arrDateParsed); //can only accept 'date'
                    application.setDepartureDate(DepDateParsed);
                    application.setStatus("PENDING");
                    application.setPaidStatus("UNPAID");
                    application.setPrice(priceLabel2.getText());

                    application.Save();

                    JOptionPane.showMessageDialog(cfmDialog, "Reservation made successfully.");
                    refreshPayment(paymentListPanel);
                    refreshHistory(historyListPanel);


                    Window window = SwingUtilities.getWindowAncestor(cfmPanel);
                    window.dispose();
                }
            });
            cfmPanel.add(payLaterButton, f);

            JButton cfmButton = new JButton("Pay Now", null);
            f.gridx = 3;
            f.gridy = 8; 
            cfmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Application application = new Application();

                    application.setRoomType(selectedRoomText);
                    application.setOccupant(getUsername());
                    application.setArrivalDate(arrDateParsed); //can only accept 'date'
                    application.setDepartureDate(DepDateParsed);
                    application.setStatus("PENDING");
                    application.setPaidStatus("UNPAID");
                    application.setPrice(priceLabel2.getText());

                    application.Save();

                    JOptionPane.showMessageDialog(cfmDialog, "Paid successfully.");
                    refreshPayment(paymentListPanel);
                    refreshHistory(historyListPanel);
                    Window window = SwingUtilities.getWindowAncestor(cfmPanel);
                    window.dispose();
                }
            });
            cfmPanel.add(cfmButton, f);
            return(cfmDialog);
        }  
    }
}