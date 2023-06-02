
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import javax.swing.*;

public class Admin extends User {
    public Admin(ArrayList<String> userDetails) {
        super(userDetails);
    }

    private Map<String, ArrayList<String>> getInfo(String file){
        Map<String, ArrayList<String>> info;
        FileHandler fileHandler = new FileHandler(file);
        info = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        return(info);
    }

    private Map<String, ArrayList<String>> getHostels(){
        return getInfo("Hostels.txt");
    }

    private Map<String, ArrayList<String>> getUsers(){
        return getInfo("Users.txt");
    }

    private Map<String, ArrayList<String>> getApplications() {
        return getInfo("Applications.txt");
    }

    private String[] calculateRevenue() {
        int totalApplications = 0;
        int totalPaid = 0;
        float totalPrice = 0;

        Map<String, ArrayList<String>> applications = getApplications();

        for(String key : applications.keySet()) {
            ArrayList<String> application = applications.get(key);
            if (application.get(4).equals("ACCEPTED")) {
                totalApplications++;
            }
            if (application.get(5).equals("PAID")) {
                totalPaid++;
            }
            totalPrice += Float.parseFloat(application.get(6));
        }

        String[] details = {
            Integer.toString(totalApplications), 
            Integer.toString(totalPaid),
            Float.toString(totalPrice)
        };
        
        return details;
        
    }

    public class AdminPanel extends JPanel{
        public AdminPanel() {
    
            JTabbedPane mainPanel = new JTabbedPane();
    
            JPanel hostelPanel = new JPanel();

            JPanel hostelListPanel = new JPanel();
            hostelListPanel.setLayout(new BoxLayout(hostelListPanel, BoxLayout.Y_AXIS));
            loadHostels(hostelListPanel);

            JScrollPane hostelScrollPane = new JScrollPane(hostelListPanel);
            hostelScrollPane.setPreferredSize(new Dimension(480,260));
            hostelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

            hostelPanel.add(hostelScrollPane);

            Image sizedImage = Gui.CreateIcon("Resources\\plus.png", 24, 24);
            FloatingButton floatButton = new FloatingButton(sizedImage, 30) {
                @Override
                public void ButtonClicked() {
                    JDialog addDialog = new AddHostelDialog() {
                        @Override 
                        void Close() {
                            hostelListPanel.removeAll();
                            hostelListPanel.validate();
                            hostelListPanel.repaint();
                            dispose();
                            loadHostels(hostelListPanel);
                        }                       
                    };
                    addDialog.setVisible(true);
                }
            };
            JLayer<Component> HostelLayer = new JLayer<Component>(hostelPanel, floatButton);

            
            JPanel studentsPanel = new JPanel(); 
            
            JPanel studentListPanel = new JPanel();
            studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));
            loadStudents(studentListPanel);
            
            JScrollPane studentScrollPane = new JScrollPane(studentListPanel);
            studentScrollPane.setPreferredSize(new Dimension(480,260));
            studentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

            studentsPanel.add(studentScrollPane);

            JPanel applicationPanel = new JPanel();

            JPanel applicationListPanel = new JPanel();
            applicationListPanel.setLayout(new BoxLayout(applicationListPanel, BoxLayout.Y_AXIS));
            loadApplication(applicationListPanel);


            JScrollPane applicationScrollPane = new JScrollPane(applicationListPanel);
            applicationScrollPane.setPreferredSize(new Dimension(480,260));
            applicationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
            applicationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            applicationPanel.add(applicationScrollPane);

            JPanel reportPanel = new JPanel();
            loadReport(reportPanel);
    
            mainPanel.setMinimumSize(new Dimension(500,280));
            mainPanel.setPreferredSize(new Dimension(500,280));

            JTextField searchField = new JTextField(30);
            searchField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel tabPanel = (JPanel)mainPanel.getSelectedComponent();
                    filterPanel(tabPanel, searchField.getText());
                }
            });
            this.add(searchField);

            mainPanel.addTab("Hostel", HostelLayer);
            mainPanel.addTab("Students", studentsPanel);
            mainPanel.addTab("Applications", applicationPanel);
            mainPanel.addTab("Report", reportPanel);
            
            this.add(mainPanel);
        }

        private void filterPanel(JPanel panel, String search) { // to filter a panel with list records
            JScrollPane scrollPane = null;
            try{
                scrollPane = (JScrollPane)panel.getComponents()[0];
            }
            catch(ClassCastException ex) {
                return;
            }
            JPanel listPanel;
            for(Component comp : scrollPane.getViewport().getComponents()) {
                if(comp instanceof JPanel) {
                    listPanel = (JPanel)comp;
                    for(Component record : listPanel.getComponents()) {
                        JPanel recordPanel = (JPanel)record;
                        JPanel labelPanel = (JPanel)recordPanel.getComponent(0);
                        for(Component label : labelPanel.getComponents()) {
                            JLabel detail = (JLabel)label;
                            if (detail.getText().toUpperCase().contains(search.toUpperCase())) {
                                record.setVisible(true);
                                break;
                            } else record.setVisible(false);
                        }
                    }
                }
            }  
        }

        private void filterPanel(JPanel panel, String search, int index) { // to filter a panel with list records on specific column
            JScrollPane scrollPane = (JScrollPane)panel.getComponents()[0];
            JPanel listPanel;
            for(Component comp : scrollPane.getViewport().getComponents()) {
                if(comp instanceof JPanel) {
                    listPanel = (JPanel)comp;
                    for(Component record : listPanel.getComponents()) {
                        JPanel recordPanel = (JPanel)record;
                        JPanel labelPanel = (JPanel)recordPanel.getComponent(0);
                        JLabel detail = (JLabel)(labelPanel.getComponent(index));
                        if (detail.getText().equals(search)) {
                            record.setVisible(true);
                        } else record.setVisible(false);
                    }
                }
            }
        }  

        private void loadHostels(JPanel panelRef) {
            Map<String, ArrayList<String>> records = getHostels();
            for(String key : records.keySet()) {
                ArrayList<String> hostelInfo = records.get(key);
                String[] details = {
                    key,
                    hostelInfo.get(0), //type
                    hostelInfo.get(1), //price
                    hostelInfo.get(2) //Availability
                };
                JPanel hostelButtonPanel = Gui.createRecordButton(details);
                hostelButtonPanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        HostelDialog hostelDialog = new HostelDialog(key) {
                            @Override
                            public void Close() {
                                panelRef.removeAll();
                                loadHostels(panelRef);
                                panelRef.validate();
                                panelRef.repaint();
                                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                                return;
                            }
                        };
                        hostelDialog.setVisible(true);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JPanel panel = (JPanel)hostelButtonPanel.getComponent(0);
                        for(Component labels : panel.getComponents()) {
                            labels.setForeground(Color.BLUE);
                        }
                    }
        
                    @Override
                    public void mouseExited(MouseEvent e) {
                        JPanel panel = (JPanel)hostelButtonPanel.getComponent(0);
                        for(Component labels : panel.getComponents()) {
                            labels.setForeground(Color.BLACK);
                        }            
                    }
                    
                });
                panelRef.add(hostelButtonPanel);
                panelRef.validate();
                panelRef.repaint();
            }
        }

        private void loadStudents(JPanel panelRef){
            Map<String, ArrayList<String>> records = getUsers();
            records = Student.getStudent(records);           
            for(String key : records.keySet()) {
                Student student = new Student(key);
                ArrayList<String> studentInfo = student.getInfo();
                String[] details =  {
                    key, //username
                    student.getName() //name
                    };
                JPanel studentButtonPanel = Gui.createRecordButton(details);
                studentButtonPanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        StudentDialog studentDialog = new StudentDialog(key, studentInfo) {
                            @Override
                            public void CheckApplication(String username) {
                                Container c = panelRef.getParent();
                                while(c != null && c.getClass() != JTabbedPane.class)
                                    c = c.getParent();
                                JTabbedPane tabbedPane = (JTabbedPane)c;
                                tabbedPane.setSelectedIndex(2);
                                JPanel applicantPanel = (JPanel)tabbedPane.getSelectedComponent();
                                filterPanel(applicantPanel, username, 2);
                            }
                        };
                        studentDialog.setVisible(true);
                    }
        
                    @Override
                    public void mousePressed(MouseEvent e) {}
        
                    @Override
                    public void mouseReleased(MouseEvent e) {}
        
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JPanel panel = (JPanel)studentButtonPanel.getComponent(0);
                        for(Component labels : panel.getComponents()) {
                            labels.setForeground(Color.BLUE);
                        }
                    }
        
                    @Override
                    public void mouseExited(MouseEvent e) {
                        JPanel panel = (JPanel)studentButtonPanel.getComponent(0);
                        for(Component labels : panel.getComponents()) {
                            labels.setForeground(Color.BLACK);
                        }            
                    }
                    });
                panelRef.add(studentButtonPanel);
                panelRef.validate();
                panelRef.repaint();
            }
        }

        private void loadApplication(JPanel panelRef) {
            Map<String, ArrayList<String>> records = getApplications();
            for(String key : records.keySet()) {
                ArrayList<String> values = records.get(key);
                String[] details = {key, //Application ID
                    values.get(0), //Room Type
                    values.get(1), //Username
                    values.get(2), //Start Date
                    values.get(3), //End Date
                    values.get(4) //AppStatus
                };
                JPanel applicationButtonPanel = Gui.createRecordButton(details);
                applicationButtonPanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JDialog applicationDialog = new ApplicationDialog(key) {
                            @Override
                            public void Close() {
                                panelRef.removeAll();
                                loadApplication(panelRef);
                                panelRef.validate();
                                panelRef.repaint();
                                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                                return;
                            }
                        };
                    }
        
                    @Override
                    public void mousePressed(MouseEvent e) {}
        
                    @Override
                    public void mouseReleased(MouseEvent e) {}
        
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JPanel panel = (JPanel)applicationButtonPanel.getComponent(0);
                        for(Component label : panel.getComponents()) {
                            label.setForeground(Color.BLUE);
                        }
                    }
        
                    @Override
                    public void mouseExited(MouseEvent e) {
                        JPanel panel = (JPanel)applicationButtonPanel.getComponent(0);
                        for(Component label : panel.getComponents()) {
                            label.setForeground(Color.BLACK);
                        }                
                    }
                    });
                panelRef.add(applicationButtonPanel, FlowLayout.LEFT);
            }
        }

        private void loadReport(JPanel panelRef) {
            JPanel headerPanel = new JPanel();
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MONTH, c.MONTH - 1);
            c.set(Calendar.DAY_OF_MONTH, 1);  
            String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            JLabel headerLabel = new JLabel("Report for " + month);
            headerLabel.setBorder(BorderFactory.createEtchedBorder());
            headerPanel.add(headerLabel);


            Dimension panelSize = new Dimension(300, 150);
            JPanel reportDetailPanel = new JPanel();
            reportDetailPanel.setPreferredSize(panelSize);
            reportDetailPanel.setMaximumSize(panelSize);
            reportDetailPanel.setMinimumSize(panelSize);

            String[] labelArray = {
                "Accepted Application",
                "Paid Applications",
                "Total Revenue (RM)"
            };

            Gui.displayLabelGrid(reportDetailPanel, new String[] {"0", "0", "0"}, labelArray,0);

            JPanel reportButtonPanel = new JPanel();
            JButton generateButton = new JButton("Generate Report");
            generateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reportDetailPanel.removeAll();
                    Gui.displayLabelGrid(reportDetailPanel, calculateRevenue(), labelArray,0);
                }
            });
            reportButtonPanel.add(generateButton);

            panelRef.add(headerPanel);
            panelRef.add(reportDetailPanel);
            panelRef.add(reportButtonPanel);
        }

        private class AddHostelDialog extends DialogForm {

            ButtonGroup radioGroup;

            AddHostelDialog() {
                infoArray = new String[] {Hostel.GetHighestNullID(), "", ""};
                labelArray = Hostel.DETAILS;

                JPanel dialogPanel = new JPanel();

                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setSize(this.getWidth(), (int)(this.getHeight() * 0.8));
                CreateForm(formPanel);
                dialogPanel.add(formPanel);

                JPanel buttonPanel = new JPanel();
                JButton saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField[] fields = getFields(formPanel);
                        if (valueCheck(fields)) {
                            save(fields);
                            Close();
                        }else {
                            JOptionPane.showMessageDialog(dialogPanel, "Please fill in missing textboxes!", "Error!", JOptionPane.ERROR_MESSAGE, null);
                        }
                    }
                });
                buttonPanel.add(saveButton);

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Close();
                    }
                });
                buttonPanel.add(cancelButton);
                dialogPanel.add(buttonPanel);

                add(dialogPanel);
                
                SetDialogProperties();
                DisplayDialog();
            }

            JTextField[] getFields(JPanel formPanel) {
                ArrayList<JTextField> fields = new ArrayList<JTextField>();
                for(Component comp : formPanel.getComponents()) {
                    if (!(comp instanceof JTextField)) continue;
                    fields.add((JTextField)comp);
                }
                JTextField[] fieldsArray = fields.toArray(new JTextField[fields.size()]);
                return(fieldsArray);
            }

            boolean valueCheck(JTextField[] textFields) {
                for(JTextField textField : textFields) {
                    if (textField.getText().isEmpty()) {
                        textField.setForeground(Color.RED);
                        return(false);
                    }
                }
                return(true);
            }

                

            @Override
            void CreateForm(JPanel dialogPanel) {
                Gui.displayFormGrid(dialogPanel, infoArray, labelArray, 0);

                JTextField[] fields = getFields(dialogPanel);

                for(JTextField field : fields) {
                    switch(field.getName()){
                        case("Hostel ID"): {
                            field.setEditable(false);
                            break;
                        }
                        case("Price"): {
                            field.addFocusListener(new FocusListener() {
                                @Override
                                public void focusGained(FocusEvent e) {
                                    try {
                                        Integer.parseInt(field.getText());
                                        field.setForeground(Color.BLACK);
                                    }catch(NumberFormatException ex) {
                                        if(!(field.getText().isEmpty()))
                                            field.setForeground(Color.RED);
                                    }
                                }
            
                                @Override
                                public void focusLost(FocusEvent e) {
                                    try {
                                        Integer.parseInt(field.getText());
                                        field.setForeground(Color.BLACK);
                                    }catch(NumberFormatException ex) {
                                        field.setForeground(Color.RED);
                                        field.requestFocus();
                                    }
                                }
                            });
                            break;
                        }   
                    }
                }

                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 3;
                JLabel statusLabel = new JLabel(Hostel.DETAILS[3] + ":", SwingConstants.RIGHT);

                dialogPanel.add(statusLabel, c);

                c.gridx = 1;
                JPanel radioPanel = new JPanel();
                radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

                radioGroup = new ButtonGroup();

                JRadioButton availableButton = new JRadioButton("Available", true);
                availableButton.setActionCommand("AVAILABLE");
                radioGroup.add(availableButton);
                radioPanel.add(availableButton);

                JRadioButton unavailableButton = new JRadioButton("Unavailable", false);
                unavailableButton.setActionCommand("UNAVAILABLE");
                radioGroup.add(unavailableButton);
                radioPanel.add(unavailableButton);

                dialogPanel.add(radioPanel, c);
            }

            void save(JTextField[] fields) {
                Hostel newHostel = new Hostel(Hostel.GetHighestNullID()); 
                for(JTextField field : fields) {
                    switch(field.getName()){
                        case("Room Type") -> newHostel.setRoomType(field.getText());
                        case("Price") -> newHostel.setPrice(Integer.parseInt(field.getText()));
                    }
                }
                newHostel.setStatus(radioGroup.getSelection().getActionCommand().toString());
                newHostel.saveHostel();
            }

            @Override
            void SetDialogProperties() {
                this.setSize(300, 200);
                this.setResizable(false);
                this.setLocationRelativeTo(null);
            }

            @Override
            void DisplayDialog() {
                this.setVisible(true);
            }
            @Override
            void Close() {
                dispose();
            }
        }

        private class HostelDialog extends DialogForm{

            Hostel hostel;

            Dimension labelSize = new Dimension(90, 20);

            ButtonGroup radioGroup;

            JPanel hostelFormPanel;

            boolean valid = true;

            public HostelDialog(String hostelId) {

                hostel = new Hostel(hostelId);

                infoArray = new String[] {hostelId, hostel.getRoomType(), Integer.toString(hostel.getPrice())};
                labelArray = Hostel.DETAILS;

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                CreateForm(panel);
                SetDialogProperties();
                DisplayDialog();
                this.add(panel);
            }

            JTextField[] getFields(JPanel formPanel) {
                ArrayList<JTextField> fields = new ArrayList<JTextField>();
                for(Component comp : formPanel.getComponents()) {
                    if (!(comp instanceof JTextField)) continue;
                    fields.add((JTextField)comp);
                }
                JTextField[] fieldsArray = fields.toArray(new JTextField[fields.size()]);
                return(fieldsArray);
            }

            private boolean save() {
                for(Component c : hostelFormPanel.getComponents()) {
                    if (c instanceof JTextField) {
                        JTextField textField = (JTextField)c;
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Invalid fields", "Error", JOptionPane.ERROR_MESSAGE);
                            return(false);
                        }
                    }
                }
                if(!valid) {
                    JOptionPane.showMessageDialog(this, "Invalid fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                for(JTextField field : getFields(hostelFormPanel)) {
                    switch(field.getName()){
                        case("Room Type") -> hostel.setRoomType(field.getText());
                        case("Price") -> hostel.setPrice(Integer.parseInt(field.getText()));
                    }
                }
                hostel.setStatus(radioGroup.getSelection().getActionCommand().toString());

                hostel.saveHostel();
                return true;
            }

            public void Close() {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }

            @Override
            void CreateForm(JPanel dialogPanel) {
                hostelFormPanel = new JPanel(new GridBagLayout());
                hostelFormPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                Gui.displayFormGrid(hostelFormPanel, infoArray, labelArray, 0);

                for(JTextField field : getFields(hostelFormPanel)) {
                    switch(field.getName()){
                        case("Hostel ID"): {
                            field.setEditable(false);
                            break;
                        }
                        case("Price"): {
                            field.addFocusListener(new FocusListener() {
                                @Override
                                public void focusGained(FocusEvent e) {
                                    try {
                                        Integer.parseInt(field.getText());
                                        field.setForeground(Color.BLACK);
                                    }catch(NumberFormatException ex) {
                                        if (!(field.getText().isEmpty()))
                                            field.setForeground(Color.RED);
                                    }
                                }
            
                                @Override
                                public void focusLost(FocusEvent e) {
                                    try {
                                        Integer.parseInt(field.getText());
                                        field.setForeground(Color.BLACK);
                                        valid = true;
                                    }catch(NumberFormatException ex) {
                                        field.setForeground(Color.RED);
                                        valid = false;
                                        field.requestFocus();
                                    }
                                }
                                
                            });
                        }
                    }
                }

                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 3;
                JLabel statusLabel = new JLabel(Hostel.DETAILS[3] + ":", SwingConstants.RIGHT);
                hostelFormPanel.add(statusLabel, c);

                c.gridx = 1;
                JPanel radioPanel = new JPanel();
                radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
                radioGroup = new ButtonGroup();
                JRadioButton availableButton = new JRadioButton("Available", "AVAILABLE".equals(hostel.getStatus()));
                availableButton.setActionCommand("AVAILABLE");
                radioGroup.add(availableButton);
                radioPanel.add(availableButton);
                JRadioButton unavailableButton = new JRadioButton("Unavailable", "UNAVAILABLE".equals(hostel.getStatus()));
                unavailableButton.setActionCommand("UNAVAILABLE");
                radioGroup.add(unavailableButton);
                radioPanel.add(unavailableButton);
                hostelFormPanel.add(radioPanel, c);

                dialogPanel.add(hostelFormPanel);

                JPanel buttonPanel = new JPanel();
                
                c.gridx = 0;
                c.gridy = 0;
                JButton saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(save())
                            Close();
                    }
                });
                buttonPanel.add(saveButton, c);
                
                c.gridx = 1;
                JButton closeButton = new JButton("Cancel");
                closeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPanel.add(closeButton, c);

                c.gridx = 2;
                JButton deleteButton = new JButton("Delete");
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hostel.deleteHostel();
                        Close();
                    }
                });
                buttonPanel.add(deleteButton, c);

                dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
            }

            @Override
            void SetDialogProperties() {
                this.setSize(300, 300);
                this.setResizable(false);
                this.setLocationRelativeTo(null);
            }

            @Override
            void DisplayDialog() {
                setVisible(true);
            }
        }

        private class StudentDialog extends DialogForm{
            JPanel panel;
            JPanel studentDetailsPanel;
            JButton historyButton;
            public StudentDialog (String username, ArrayList<String> studentInfo) {    
                
                infoArray = studentInfo.toArray(new String[studentInfo.size()]);
                labelArray = Student.DETAILS;

                panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
                CreateForm(panel);
        
                historyButton = new JButton("Check Applications");
                historyButton.setSize(new Dimension(80, 30));
                historyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CheckApplication(username);
                        Close();
                    }
                });

                panel.add(historyButton, BorderLayout.SOUTH);

                this.add(panel);

                SetDialogProperties();
            }

            @Override
            void CreateForm(JPanel dialogPanel) {
                studentDetailsPanel = new JPanel();
                studentDetailsPanel.setLayout(new GridBagLayout());
                studentDetailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                Gui.displayLabelGrid(studentDetailsPanel, infoArray, labelArray, 0);
                dialogPanel.add(studentDetailsPanel);
            }

            public void CheckApplication(String username) {}

            @Override
            public void Close() {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }

            @Override
            void SetDialogProperties() {
                this.setSize(300, 300);;
                this.setLocationRelativeTo(null);
            }

            @Override
            void DisplayDialog() {
                this.setVisible(true);
            }
        }

        private class ApplicationDialog extends DialogForm{
            GridBagConstraints c;
    
            Application application;
    
            JPanel applicationPanel;
            JPanel applicationDetailPanel;
            JPanel buttonPanel;
            
            JButton acceptButton;
            JButton rejectButton;
            
    
            public ApplicationDialog(String applicationId) {

                application = new Application(applicationId);
                
                infoArray = new String[] {
                    applicationId, 
                    application.getRoomType(), 
                    application.getUsername(), 
                    application.getArrivalDate(),
                    application.getDepartureDate(),
                    application.getStatus(),
                    application.getPaidStatus(),
                    application.getPrice()
                };

                labelArray = Application.DETAILS;
    

                applicationPanel = new JPanel();

                CreateForm(applicationPanel);
    
                buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridBagLayout());
                c = new GridBagConstraints();
                                
                if (application.getStatus().equals("PENDING")) {
                    c.gridx = 0;
                    c.gridy = 0;
                    acceptButton = new JButton("Accept");
                    acceptButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            application.setStatus("ACCEPTED");
                            application.saveApplication();
                            Close();                        
                        }
                    });
                    buttonPanel.add(acceptButton, c);
    
                    c.gridx = 1;
                    c.gridy = 0;
                    rejectButton = new JButton("Reject");
                    rejectButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            application.setStatus("REJECTED");
                            application.saveApplication();
                            Close();
                        }
                    });
                    buttonPanel.add(rejectButton, c);
                }else {
                    c.gridy = 6;
                    JLabel statusLabel = new JLabel(application.getStatus());
                    statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
                    applicationPanel.add(statusLabel, c);
                }
                applicationPanel.add(buttonPanel);
    
                this.add(applicationPanel);
                SetDialogProperties();
                DisplayDialog();
            }
    
            @Override
            public void Close() {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }

            @Override
            void CreateForm(JPanel dialogPanel) {
                applicationDetailPanel = new JPanel();
                applicationDetailPanel.setLayout(new GridBagLayout());
                Gui.displayLabelGrid(applicationDetailPanel, infoArray, labelArray, 0);
                dialogPanel.add(applicationDetailPanel);
            }

            @Override
            void SetDialogProperties() {
                this.setTitle("Application Details");
                this.validate();
                this.setSize(300, 300);;
                this.setLocationRelativeTo(null);
            }

            @Override
            void DisplayDialog() {
                this.setVisible(true);
            }
        }
    }
}