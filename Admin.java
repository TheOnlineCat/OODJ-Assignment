
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
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
                    JDialog addDialog = new AddHostelDialog();
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
            
            this.add(mainPanel);
        }

        private void filterPanel(JPanel panel, String search) {
            JScrollPane scrollPane = (JScrollPane)panel.getComponents()[0];
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
                                filterPanel(applicantPanel, username);
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
                String[] details = {key, //Application ID
                    records.get(key).get(0), //Room Type
                    records.get(key).get(1), //Username
                    records.get(key).get(2), //Approved Status
                    records.get(key).get(3) //Date
                };
                JPanel applicationButtonPanel = Gui.createRecordButton(details);
                applicationButtonPanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ApplicationDialog applicationDialog = new ApplicationDialog(key) {
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

        private class AddHostelDialog extends DialogForm {
            
            AddHostelDialog() {

            }

            @Override
            void CreateForm(JPanel dialogPanel) {
                setLocationRelativeTo(null);
            }

            @Override
            void SetDialogProperties() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'SetDialogProperties'");
            }

            @Override
            void DisplayDialog() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'DisplayDialog'");
            }
        }

        private class HostelDialog extends JDialog{
            Hostel hostel;

            Dimension labelSize = new Dimension(90, 20);

            JPanel panel;
            JPanel hostelFormPanel;

            JTextField priceTField;
            JTextField typeTField;

            JPanel buttonPanel;
            JButton saveButton;
            JButton closeButton;
            JButton deleteButton;

            ButtonGroup radioGroup;

            GridBagConstraints c;

            boolean valid = true;

            public HostelDialog(String hostelId) {

                hostel = new Hostel(hostelId);

                panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                hostelFormPanel = new JPanel();
                hostelFormPanel.setLayout(new GridBagLayout());
                hostelFormPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                c = new GridBagConstraints();
                c.insets = new Insets(0, 0, 15, 5);

                c.gridx = 0;
                c.gridy = 0;
                JLabel typeLabel = new JLabel(Hostel.DETAILS[0] + ":", SwingConstants.RIGHT);
                typeLabel.setPreferredSize(labelSize);
                typeLabel.setMinimumSize(labelSize);
                typeLabel.setMaximumSize(labelSize);
                hostelFormPanel.add(typeLabel, c);

                c.gridx = 1;
                typeTField = new JTextField(hostel.getRoomType());
                typeTField.setPreferredSize(labelSize);
                typeTField.setMinimumSize(labelSize);
                typeTField.setMaximumSize(labelSize);
                hostelFormPanel.add(typeTField, c);


                c.gridx = 0;
                c.gridy = 1;
                JLabel priceLabel = new JLabel(Hostel.DETAILS[1] + ":", SwingConstants.RIGHT);
                priceLabel.setPreferredSize(labelSize);
                priceLabel.setMinimumSize(labelSize);
                priceLabel.setMaximumSize(labelSize);
                hostelFormPanel.add(priceLabel, c);

                c.gridx = 1;
                priceTField = new JTextField(Integer.toString(hostel.getPrice()));
                priceTField.setPreferredSize(labelSize);
                priceTField.setMinimumSize(labelSize);
                priceTField.setMaximumSize(labelSize);
                priceTField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        try {
                            Integer.parseInt(priceTField.getText());
                            priceTField.setForeground(Color.BLACK);
                        }catch(NumberFormatException ex) {
                            priceTField.setForeground(Color.RED);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            Integer.parseInt(priceTField.getText());
                            priceTField.setForeground(Color.BLACK);
                            valid = true;
                        }catch(NumberFormatException ex) {
                            priceTField.setForeground(Color.RED);
                            valid = false;
                            priceTField.requestFocus();
                        }
                    }
                    
                });
                hostelFormPanel.add(priceTField, c);

                c.gridx = 0;
                c.gridy = 2;
                JLabel statusLabel = new JLabel(Hostel.DETAILS[2] + ":", SwingConstants.RIGHT);
                statusLabel.setPreferredSize(labelSize);
                statusLabel.setMinimumSize(labelSize);
                statusLabel.setMaximumSize(labelSize);
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

                panel.add(hostelFormPanel);

                buttonPanel = new JPanel();
                
                c.gridx = 0;
                c.gridy = 0;
                saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(save())
                            Close();
                    }
                });
                buttonPanel.add(saveButton, c);
                
                c.gridx = 1;
                closeButton = new JButton("Cancel");
                closeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPanel.add(closeButton, c);

                c.gridx = 2;
                deleteButton = new JButton("Delete");
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hostel.deleteHostel();
                        Close();
                    }
                });
                buttonPanel.add(deleteButton, c);

                panel.add(buttonPanel, BorderLayout.SOUTH);

                this.add(panel);
        
                this.setSize(300, 300);
                this.setResizable(false);
                this.setLocationRelativeTo(null);
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

                hostel.setRoomType(typeTField.getText());
                hostel.setPrice(Integer.parseInt(priceTField.getText()));
                hostel.setStatus(radioGroup.getSelection().getActionCommand());
                hostel.saveHostel();
                return true;
            }

            public void Close() {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
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
            }

            @Override
            void CreateForm(JPanel dialogPanel) {
                studentDetailsPanel = new JPanel();
                studentDetailsPanel.setLayout(new GridBagLayout());
                studentDetailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                Gui.displayLabelGrid(studentDetailsPanel, infoArray, labelArray, 0);
                dialogPanel.add(studentDetailsPanel);
            }

            public void Close() {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }

            public void CheckApplication(String username) {}

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
                    application.getDepartureDate()
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