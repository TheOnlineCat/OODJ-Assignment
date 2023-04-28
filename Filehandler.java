import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileHandler{
    
    static final String SEPERATOR = ";";
    static final String PATH = "DATA//";

    File file;
    
    public FileHandler(String path) {
        file = new File(PATH + path);

        try {
            if (file.createNewFile()) {
                System.out.println("File Created.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> read(){
        ArrayList<String> data = new ArrayList<String>();

        

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String curLine;
            while ((curLine = reader.readLine()) != null) {
                data.add(curLine);
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return(data);
    }

    public Map<String, ArrayList<String>> parseAsDict(ArrayList<String> data, String split, int keyIdx) {
        Map<String, ArrayList<String>> dict = new HashMap<String, ArrayList<String>>();

        for(int i = 0; i < data.size(); i++) {
            //System.out.println(data.get(i));

            //ArrayList<String> strList = new ArrayList<String>()  data.get(i).split(split);
            String[] strs = data.get(i).split(split);
            
            ArrayList<String> strList = new ArrayList<>(Arrays.asList(strs));

            strList.remove(0);

            dict.put(strs[keyIdx], strList);
        }
        return(dict);
    }

    private String dictAsString(Map<String, ArrayList<String>> dict) {
        //String[] data = new String[dict.size()];
        String data = "";
        

        for(String key : dict.keySet()) {
            data += key + SEPERATOR + stringsAsString(dict.get(key)) + "\n";
        }
        
        return(data);

        // for(Map.Entry<String, ArrayList<String>> key : dict.entrySet()) {
            
        // }
    }

    public static String stringsAsString(ArrayList<String> strs){
        String curStr;
        curStr = String.join(SEPERATOR, strs);
        //data[count] = curStr;
        return(curStr);
    }

    public void save(Map<String, ArrayList<String>> dict) {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(dictAsString(dict));
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to savex");
        }
    }

    

    public static boolean Exists(String path){
        File f = new File(PATH + path);

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
            System.out.println("File found!");
            return(true);
        }
        else {
            System.out.println("File not found!");
            return(false);
        }
    }


}