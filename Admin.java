import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

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
            studentListPanel.setLayout(new BoxLayout(applicationListPanel, BoxLayout.Y_AXIS));
            loadStudents(applicationListPanel);

            JScrollPane applicationScrollPane = new JScrollPane(applicationListPanel);
            applicationScrollPane.setPreferredSize(new Dimension(480,260));
            applicationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

            applicationPanel.add(applicationScrollPane);

    
    
            mainPanel.setMinimumSize(new Dimension(500,300));
            mainPanel.setPreferredSize(new Dimension(500,300));
            mainPanel.addTab("Hostel", hostelPanel);
            mainPanel.addTab("Students", studentsPanel);
            mainPanel.addTab("Applications", applicationPanel);
            
            this.add(mainPanel);
        }
    
    }

    private void loadApplication(JPanel panelRef) {
        Map<String, ArrayList<String>> records;

        FileHandler fileHandler = new FileHandler("Data//Records.txt");
        records = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        records = Student.getStudent(records);
        for(String key : records.keySet()) {
            String[] details = {key};
            JPanel applicationButtonPanel = createRecordButton(details);
            applicationButtonPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                }
    
                @Override
                public void mousePressed(MouseEvent e) {}
    
                @Override
                public void mouseReleased(MouseEvent e) {}
    
                @Override
                public void mouseEntered(MouseEvent e) {
                    for(Component comp : applicationButtonPanel.getComponents()) {
                        comp.setForeground(Color.BLUE);
                    }
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    for(Component comp : applicationButtonPanel.getComponents()) {
                        comp.setForeground(Color.BLACK);
                    }
                    
                }
                });
            panelRef.add(applicationButtonPanel);
        }

    }

    private void loadStudents(JPanel panelRef){
        
        Map<String, ArrayList<String>> records;

        FileHandler fileHandler = new FileHandler("Data//Users.txt");
        records = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        records = Student.getStudent(records);
        for(String key : records.keySet()) {
            String[] details = {key};
            JPanel studentButtonPanel = createRecordButton(details);
            studentButtonPanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showStudentRecord();
                }
    
                @Override
                public void mousePressed(MouseEvent e) {}
    
                @Override
                public void mouseReleased(MouseEvent e) {}
    
                @Override
                public void mouseEntered(MouseEvent e) {
                    for(Component comp : studentButtonPanel.getComponents()) {
                        comp.setForeground(Color.BLUE);
                    }
                }
    
                @Override
                public void mouseExited(MouseEvent e) {
                    for(Component comp : studentButtonPanel.getComponents()) {
                        comp.setForeground(Color.BLACK);
                    }
                    
                }
                });
            panelRef.add(studentButtonPanel);
        }
    }
    

    private JPanel createRecordButton(String[] details) {
        JPanel recordPanel = new JPanel();
        recordPanel.setBorder(BorderFactory.createEmptyBorder());
        recordPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        for(String str : details) {
            JLabel label = new JLabel(str);
            recordPanel.add(label, c);
            c.gridx += 1;
        }
        return(recordPanel);
    }

    private void showStudentRecord() {
        JDialog studentDialog = new JDialog();
        JPanel studentDetailsPanel = new JPanel();
        JLabel hostelsLabel = new JLabel("hostels of student");
        studentDetailsPanel.add(hostelsLabel);
        studentDialog.add(studentDetailsPanel);
        

        studentDialog.setSize(300, 300);;
        studentDialog.setLocationRelativeTo(null);
        studentDialog.setVisible(true);
    }

}
