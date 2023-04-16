import java.io.*;

public class Filehandler {

    File file;
    FileReader reader;
    FileWriter writer;

    public Filehandler(String filename){
        file = new File(filename);
    }

    public void readData(){
        try{
            FileReader reader = new FileReader(file);        
        }catch (FileNotFoundException ex){
        }
    }

    public static void createFile(String filename){
        try{
            FileWriter writer = new FileWriter(filename);
        }catch (IOException ex){
        }

    }

    public void rename(String newFilename){
        file.renameTo(new File(newFilename));
    }
}
