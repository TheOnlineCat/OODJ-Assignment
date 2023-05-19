import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Application {

    public static final String[] DETAILS = {
        "Application ID",
        "Room Type",
        "Occupied",
        "Date applied",
        "Status",
    };
    
    private String applicationID;
    private String roomType;
    private String username;
    private String status;
    private Date date;



    public String getApplicationID() {
        return applicationID;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String strDate = dateFormat.format(this.date);
        return(strDate);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

    public Application(String applicationID){
        if (loadApplication(applicationID));
            this.applicationID = applicationID;
    }

    private boolean loadApplication(String applicationID) {
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        for(String key : applicationDict.keySet()) {
            if(applicationID.equals(key)) {
                roomType = applicationDict.get(key).get(0);
                username = applicationDict.get(key).get(1);
                
                try {
                    date = new SimpleDateFormat("dd-mm-yyyy").parse(applicationDict.get(key).get(2));
                } catch (ParseException e) {
                    System.out.println("Unable to parse date");
                    date = new Date(0);
                }

                status = applicationDict.get(key).get(3);

                return(true);
            }
        }
        return(false);
    }

    public void deleteApplication () {
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        applicationDict.remove(applicationID);
        fileHandler.save(applicationDict);
    }

    public static void deleteApplication (String appID) {
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        applicationDict.remove(appID);
        fileHandler.save(applicationDict);
    }


    public void saveApplication(){
        List<String> newInfo = Arrays.asList(roomType, username, this.getDate(), status);

        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        applicationDict.put(applicationID, new ArrayList<String>(newInfo));
        fileHandler.save(applicationDict);  
        return;
    }
}
