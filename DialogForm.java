import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public abstract class DialogForm extends JDialog{
    
    String[] infoArray;
    String[] labelArray;

    abstract void CreateForm(JPanel dialogPanel);

    abstract void SetDialogProperties();

    abstract void DisplayDialog();

    abstract void Close();

    //abstract void createButton();

}
