import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class Application {

    public static final String[] DETAILS = {
        "Application ID",
        "Room Type",
        "Occupied",
        "Arrival",
        "Departure",
        "Apply Status",
        "Payment Status",
        "Price"

    };



    
    private String applicationID;
    private String roomType;
    private String username;
    private String status;
    private String paidStatus;
    private Date arrivalDate;
    private Date departureDate;
    private String price;




    public String getApplicationID() {
        return applicationID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getUsername() {
        return username;
    }


    public String getArrivalDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String strDate = dateFormat.format(this.arrivalDate);
        return(strDate);
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String strDate2 = dateFormat.format(this.departureDate);
        return(strDate2);
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public Application(){
        this.applicationID = GetHighestNullID();
    }


    public Application(String applicationID){
        if (loadApplication(applicationID));
            this.applicationID = applicationID;
    }

    public void setOccupant(String username){
        this.username = username;
    }

    private boolean loadApplication(String applicationID) {
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        for(String key : applicationDict.keySet()) {
            if(applicationID.equals(key)) {
                roomType = applicationDict.get(key).get(0);
                username = applicationDict.get(key).get(1);
                
                try {
                    arrivalDate = new SimpleDateFormat("dd-mm-yyyy").parse(applicationDict.get(key).get(2));
                } catch (ParseException e) {
                    System.out.println("Unable to parse date");
                    arrivalDate = new Date(0);
                }
                try {
                    departureDate = new SimpleDateFormat("dd-mm-yyyy").parse(applicationDict.get(key).get(3));
                } catch (ParseException e) {
                    System.out.println("Unable to parse date");
                    departureDate = new Date(0);
                }


                status = applicationDict.get(key).get(4);
                paidStatus = applicationDict.get(key).get(5);
                price = applicationDict.get(key).get(6);

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
        List<String> newInfo = Arrays.asList(roomType, username, this.getArrivalDate(), this.getDepartureDate(), status, paidStatus, price);

        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        applicationDict.put(applicationID, new ArrayList<String>(newInfo));
        fileHandler.save(applicationDict);
        return;
        // for(String key : applicationDict.keySet()) {
        //     if(applicationID.equals(key)) {
                
        //     }
        // }
    }

    public static String GetHighestNullID(){
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        String highestID = Collections.max(applicationDict.keySet());
        int id = Integer.parseInt(highestID.substring(3)) + 1;
        String nullID = highestID.substring(0, 3) + String.format("%03d", id);
        return nullID;
    }
}
