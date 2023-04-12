import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileHandler{
    
    static final String SEPERATOR = ";";

    File file;
    
    public FileHandler(String path) {
        file = new File(path);

        try {
            if (file.createNewFile()) {
                System.out.println("File Created.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> read() throws IOException{
        ArrayList<String> data = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String curLine;
        while ((curLine = reader.readLine()) != null) {
            data.add(curLine);
        }
        reader.close();
        
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
        String curStr;

        for(String key : dict.keySet()) {
            curStr = key + SEPERATOR + String.join(SEPERATOR, dict.get(key));
            //data[count] = curStr;
            data += curStr + "\n";
        }
        
        return(data);

        // for(Map.Entry<String, ArrayList<String>> key : dict.entrySet()) {
            
        // }
    }

    public void save(Map<String, ArrayList<String>> dict) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(dictAsString(dict));
        writer.close();
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
            System.out.println("File found!");
            return(true);
        }
        else {
            System.out.println("File not found!");
            return(false);
        }
    }


}