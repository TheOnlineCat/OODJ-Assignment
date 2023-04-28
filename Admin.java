
import java.awt.Color;
import java.awt.Component;
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

import java.util.Map;

import javax.swing.*;

public class Admin extends User {
    public Admin(ArrayList<String> userDetails) {
        super(userDetails);
    }

    public class AdminPanel extends JPanel{
        public AdminPanel() {
    
            JLabel title = new JLabel(this.getName());
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
                    for(Component comp : applicationListPanel.getComponents()) {
                        JPanel record = (JPanel)comp;
                        for(Component comp2 : record.getComponents()) {
                            JPanel panel = (JPanel)comp2;
                            for(Component label : panel.getComponents()) {
                                JLabel detail = (JLabel)label;
                                //if (!username.getName().equals("2")) continue;
                                if (detail.getText().contains(searchField.getText())) {
                                    record.setVisible(true);
                                    break;
                                } else record.setVisible(false);
                            }
                        }
                    }
                }
                
            });
            this.add(searchField);

            applicationPanel.add(applicationScrollPane);

    
    
            mainPanel.setMinimumSize(new Dimension(500,300));
            mainPanel.setPreferredSize(new Dimension(500,300));
            mainPanel.addTab("Hostel", hostelPanel);
            mainPanel.addTab("Students", studentsPanel);
            mainPanel.addTab("Applications", applicationPanel);
            
            this.add(mainPanel);
        }
    
    }

    
    private void loadStudents(JPanel panelRef){
        
        Map<String, ArrayList<String>> records;

        FileHandler fileHandler = new FileHandler("Users.txt");
        records = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        records = Student.getStudent(records);
        for(String key : records.keySet()) {
            String[] details = {key};
            JPanel studentButtonPanel = createRecordButton(details);
            studentButtonPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(Component comp : studentButtonPanel.getComponents()) {
                        JPanel panel = (JPanel)comp;
                        for(Component label : panel.getComponents()) {
                            if(label.getName().equals("0")) {
                                JLabel userLabel = (JLabel)label;
                                showStudentDetail(userLabel.getText());
                                return;
                            }
                        }
                    }
                }
    
                @Override
                public void mousePressed(MouseEvent e) {}
    
                @Override
                public void mouseReleased(MouseEvent e) {}
    
                @Override
                public void mouseEntered(MouseEvent e) {
                    for(Component comp : studentButtonPanel.getComponents()) {
                        JPanel panel = (JPanel)comp;
                        for(Component labels : panel.getComponents()) {
                            labels.setForeground(Color.BLUE);
                        }
                    }    
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    for(Component comp : studentButtonPanel.getComponents()) {
                        JPanel panel = (JPanel)comp;
                        for(Component labels : panel.getComponents()) {
                            labels.setForeground(Color.BLACK);
                        }
                    }                    
                }
                });
            panelRef.add(studentButtonPanel);
            panelRef.validate();
            panelRef.repaint();
        }
    }

    private void loadApplication(JPanel panelRef) {
        Map<String, ArrayList<String>> records;

        FileHandler fileHandler = new FileHandler("Applications.txt");
        records = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
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
                    for(Component comp : applicationButtonPanel.getComponents()) {
                        JPanel panel = (JPanel)comp;
                        for(Component label : panel.getComponents()) {
                            if(label.getName().equals("0")) {
                                JLabel idLabel = (JLabel)label;
                                ApplicationDialog applicationDialog = new ApplicationDialog(idLabel.getText());
                                applicationDialog.addWindowListener(new WindowListener() {
                                    @Override
                                    public void windowOpened(WindowEvent e) {}

                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        panelRef.removeAll();
                                        loadApplication(panelRef);
                                        panelRef.validate();
                                        panelRef.repaint();
                                    }

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
                                return;
                            }
                        }
                    }
                }
    
                @Override
                public void mousePressed(MouseEvent e) {}
    
                @Override
                public void mouseReleased(MouseEvent e) {}
    
                @Override
                public void mouseEntered(MouseEvent e) {
                    for(Component comp : applicationButtonPanel.getComponents()) {
                        JPanel panel = (JPanel)comp;
                        for(Component label : panel.getComponents()) {
                            label.setForeground(Color.BLUE);
                        }
                    }    
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    for(Component comp : applicationButtonPanel.getComponents()) {
                        JPanel panel = (JPanel)comp;
                        for(Component labels : panel.getComponents()) {
                            labels.setForeground(Color.BLACK);
                        }
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

    private void showStudentDetail(String username) {
        JDialog studentDialog = new JDialog();
        JPanel studentDetailsPanel = new JPanel();
        JLabel hostelsLabel = new JLabel("hostels of student " + username);
        studentDetailsPanel.add(hostelsLabel);
        studentDialog.add(studentDetailsPanel);
        

        studentDialog.setSize(300, 300);;
        studentDialog.setLocationRelativeTo(null);
        studentDialog.setVisible(true);
    }

    private class ApplicationDialog extends JDialog {

        Dimension labelSize;
        GridBagConstraints c;

        Application application;

        JPanel applicationPanel;
        JLabel applicationLabel;
        JLabel typeLabel;
        JLabel usernameLabel;
        JLabel dateLabel;
        JPanel buttonPanel;
        
        JButton acceptButton;
        JButton rejectButton;
        

        public ApplicationDialog(String applicationId) {

            labelSize = new Dimension(200, 20);

            application = new Application(applicationId);

            applicationPanel = new JPanel();
            applicationPanel.setLayout(new GridBagLayout());
            
            c = new GridBagConstraints();
            c.insets = new Insets(0, 0, 20, 0);
            c.gridx = 0;
            c.gridy = 0;
            applicationLabel = new JLabel("Application " + applicationId);
            applicationLabel.setPreferredSize(labelSize);
            applicationPanel.add(applicationLabel, c);

            c.insets = new Insets(0, 0, 5, 0);
            c.gridy = 2;
            typeLabel = new JLabel("Room Type: " + application.getRoomType());
            typeLabel.setPreferredSize(labelSize);
            applicationPanel.add(typeLabel, c);
            
            c.gridy = 3;
            usernameLabel = new JLabel("Occupied by Username: " + application.getUsername());
            usernameLabel.setPreferredSize(labelSize);
            applicationPanel.add(usernameLabel, c);
            
            c.gridy = 4;
            dateLabel = new JLabel("Date applied: " + application.getDate());
            dateLabel.setPreferredSize(labelSize);
            applicationPanel.add(dateLabel, c);

            c.insets = new Insets(50, 0, 0, 0);
            c.gridy = 6;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            applicationPanel.add(buttonPanel, c);


            if (application.getStatus().equals("PENDING")) {
                c.gridx = 0;
                c.gridy = 0;
                acceptButton = new JButton("Accept");
                acceptButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        application.setStatus("ACCEPTED");
                        application.saveApplication();
                        close();                        
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
                        close();
                    }
                });
                buttonPanel.add(rejectButton, c);
            }else {
                c.gridy = 6;
                JLabel statusLabel = new JLabel(application.getStatus());
                statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
                applicationPanel.add(statusLabel, c);
            }


            this.add(applicationPanel);
            this.setTitle("Application Details");
            this.validate();
            this.setSize(300, 300);;
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        private void close() {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            dispose();
        }
    }
}
