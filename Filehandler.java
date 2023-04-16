import java.io.*;

public class FileHandler{
    
    File file;

    public FileHandler() {

    }

    public static boolean Exists(String path){
        File f = new File(path);

        if(f.exists()) {
            System.out.println(path + " found!");
            return(true);
        }
        else {
            System.out.println(path + " not found!");
            return(false);
        }
    }

    public boolean Exists() {
        if(file.exists()) {
            System.out.println("Image file found!");
            return(true);
        }
        else {
            System.out.println("Image file not found!");
            return(false);
        }
    }


}