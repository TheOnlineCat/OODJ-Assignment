import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class Application implements Saveable{

    public static final String[] DETAILS = {
        "Application ID",
        "Room Type",
        "Occupant",
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
    private LocalDate arrivalDate;
    private LocalDate departureDate;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String strDate = this.arrivalDate.format(formatter);
        return strDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String strDate2 = this.departureDate.format(formatter);
        return strDate2;
    }

    public void setDepartureDate(LocalDate departureDate) {
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

    public String getOccupant() {
        return username;
    }

    public void setOccupant(String username){
        this.username = username;
    }

    public Application(){
        this.applicationID = GetHighestNullID();
    }

    public Application(String applicationID){
        if (Load(applicationID));
            this.applicationID = applicationID;
    }

    @Override
    public boolean Load(String applicationID) {
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        for(String key : applicationDict.keySet()) {
            if (applicationID.equals(key)) {
                roomType = applicationDict.get(key).get(0);
                username = applicationDict.get(key).get(1);
    
                try {
                    arrivalDate = LocalDate.parse(applicationDict.get(key).get(2), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                } catch (DateTimeParseException e) {
                    System.out.println("Unable to parse date");
                    arrivalDate = LocalDate.MIN;
                }
                try {
                    departureDate = LocalDate.parse(applicationDict.get(key).get(3), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                } catch (DateTimeParseException e) {
                    System.out.println("Unable to parse date");
                    departureDate = LocalDate.MIN;
                }
    
                status = applicationDict.get(key).get(4);
                paidStatus = applicationDict.get(key).get(5);
                price = applicationDict.get(key).get(6);
    
                return true;
            }
        }
        return(false);
    }

    @Override
    public void Delete () {
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        applicationDict.remove(applicationID);
        fileHandler.save(applicationDict);
    }

    public static void Delete (String ID) {
        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        applicationDict.remove(ID);
        fileHandler.save(applicationDict);
    }

    @Override
    public void Save(){
        List<String> newInfo = Arrays.asList(roomType, username, this.getArrivalDate(), this.getDepartureDate(), status, paidStatus, price);

        FileHandler fileHandler = new FileHandler("Applications.txt");
        Map<String, ArrayList<String>> applicationDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        applicationDict.put(getApplicationID(), new ArrayList<String>(newInfo));
        fileHandler.save(applicationDict);
        return;
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
