
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.*;

public class Admin extends User {
    public Admin(ArrayList<String> userDetails) {
        super(userDetails);
    }


    private Map<String, ArrayList<String>> getUsers(){
        Map<String, ArrayList<String>> users;
        FileHandler fileHandler = new FileHandler("Users.txt");
        users = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        return(users);
    }

    private Map<String, ArrayList<String>> getApplications() {
        Map<String, ArrayList<String>> applications;
        FileHandler fileHandler = new FileHandler("Applications.txt");
        applications = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        return(applications);
    }

    public class AdminPanel extends JPanel{
        public AdminPanel() {
    
            JTabbedPane mainPanel = new JTabbedPane();
    
            JPanel hostelPanel = new JPanel();
            
            
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

            JTextField searchField = new JTextField(30);
            searchField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel tabPanel = (JPanel)mainPanel.getSelectedComponent();
                    filterPanel(tabPanel, searchField.getText());
                }
            });
            this.add(searchField);

            applicationPanel.add(applicationScrollPane);

    
    
            mainPanel.setMinimumSize(new Dimension(500,280));
            mainPanel.setPreferredSize(new Dimension(500,280));
            mainPanel.addTab("Hostel", hostelPanel);
            mainPanel.addTab("Students", studentsPanel);
            mainPanel.addTab("Applications", applicationPanel);
            
            this.add(mainPanel);
        }

        private void filterPanel(JPanel Panel, String search) {
            JScrollPane scrollPane = (JScrollPane)Panel.getComponents()[0];
            JPanel listPanel;
            for(Component comp : scrollPane.getViewport().getComponents()) {
                if(comp instanceof JPanel) {
                    listPanel = (JPanel)comp;
                    for(Component record : listPanel.getComponents()) {
                        JPanel recordPanel = (JPanel)record;
                        JPanel labelPanel = (JPanel)recordPanel.getComponent(0);
                        for(Component label : labelPanel.getComponents()) {
                            JLabel detail = (JLabel)label;
                            //if (!username.getName().equals("2")) continue;
                            if (detail.getText().toUpperCase().contains(search.toUpperCase())) {
                                record.setVisible(true);
                                break;
                            } else record.setVisible(false);
                        }
                    }
                }
            }  
        }

        private void loadStudents(JPanel panelRef){
            Map<String, ArrayList<String>> records = getUsers();
            records = Student.getStudent(records);           
            for(String key : records.keySet()) {
                ArrayList<String> studentInfo = records.get(key);
                String[] details =  {
                    key, //username
                    studentInfo.get(2) //name
                    };
                JPanel studentButtonPanel = createRecordButton(details);
                studentButtonPanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        StudentDialog studentDialog = new StudentDialog(panelRef, key, studentInfo);
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
                JPanel applicationButtonPanel = createRecordButton(details);
                applicationButtonPanel.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ApplicationDialog applicationDialog = new ApplicationDialog(key);
                        applicationDialog.addWindowListener(new WindowListener() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                panelRef.removeAll();
                                loadApplication(panelRef);
                                panelRef.validate();
                                panelRef.repaint();
                            }
                            
                            @Override
                            public void windowOpened(WindowEvent e) {}

                            @Override
                            public void windowClosed(WindowEvent e) {}

                            @Override
                            public void windowIconified(WindowEvent e) {}

                            @Override
                            public void windowDeiconified(WindowEvent e) {}

                            @Override
                            public void windowActivated(WindowEvent e) {}

                            @Override
                            public void windowDeactivated(WindowEvent e) {}
                        });
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

        private JPanel createRecordButton(String[] details) {
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

        private void displayLabelGrid(JPanel panelRef, ArrayList<String> info, String[] labels, int start) {
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

        private class StudentDialog extends JDialog{
            public StudentDialog (JPanel parentPanel, String username, ArrayList<String> studentInfo) {        
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
                JPanel studentDetailsPanel = new JPanel();
                studentDetailsPanel.setLayout(new GridBagLayout());
                studentDetailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                
                displayLabelGrid(studentDetailsPanel, studentInfo, Student.DETAILS, 2);

                panel.add(studentDetailsPanel);
        
                JButton historyButton = new JButton("Check Applications");
                historyButton.setSize(new Dimension(80, 30));
                historyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Container c = parentPanel.getParent();
                        while(c != null && c.getClass() != JTabbedPane.class)
                            c = c.getParent();
                        JTabbedPane tabbedPane = (JTabbedPane)c;
                        tabbedPane.setSelectedIndex(2);
                        JPanel applicantPanel = (JPanel)tabbedPane.getSelectedComponent();
                        filterPanel(applicantPanel, username);
                        close();
                    }
                });

                panel.add(historyButton, BorderLayout.SOUTH);

                this.add(panel);
        
                this.setSize(300, 300);;
                this.setLocationRelativeTo(null);
            }

            private void close() {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }

        private class ApplicationDialog extends JDialog {
            GridBagConstraints c;
    
            Application application;
    
            JPanel applicationPanel;
            JPanel applicationDetailPanel;
            JPanel buttonPanel;
            
            JButton acceptButton;
            JButton rejectButton;
            
    
            public ApplicationDialog(String applicationId) {
    
                application = new Application(applicationId);
    
                applicationPanel = new JPanel();

                applicationDetailPanel = new JPanel();
                applicationDetailPanel.setLayout(new GridBagLayout());
                
                String[] infoArray = {
                    applicationId, 
                    application.getRoomType(), 
                    application.getUsername(), 
                    application.getDate()
                };
                ArrayList<String> applicationInfo = new ArrayList<String>(Arrays.asList(infoArray));

                displayLabelGrid(applicationDetailPanel, applicationInfo, Application.DETAILS, 0);
                applicationPanel.add(applicationDetailPanel);
    
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
                this.setTitle("Application Details");
                this.validate();
                this.setSize(300, 300);;
                this.setLocationRelativeTo(null);
                this.setVisible(true);
            }
    
            private void Close() {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
}


    
    