import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student { 

    public static Map<String, ArrayList<String>> getStudent(Map<String, ArrayList<String>> usersDict) {
        Map<String, ArrayList<String>> studentMap = new HashMap<>();
        for(String key : usersDict.keySet()) {
            if(usersDict.get(key).get(1).equals("STUDENT")) {
                studentMap.put(key, usersDict.get(key));
            }
        }
        return(studentMap);        
    }
    
}
