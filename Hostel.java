import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Hostel {

    private String hostelID;
    private String roomType;
    private int price;
    private String status;

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final String[] DETAILS = {
                                "Room Type",
                                "Price",
                                "Room Status"
                            };

    public Hostel(String id) {
        hostelID = id;
        loadHostel(id);
    }

    private boolean loadHostel(String hostelID) {
        FileHandler fileHandler = new FileHandler("Hostels.txt");
        Map<String, ArrayList<String>> hostelDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        for(String key : hostelDict.keySet()) {
            if(hostelID.equals(key)) {
                roomType = hostelDict.get(key).get(0);
                price = Integer.parseInt(hostelDict.get(key).get(1));
                status = hostelDict.get(key).get(2);
                return(true);
            }
        }
        return(false);
    }

    public void deleteHostel () {
        FileHandler fileHandler = new FileHandler("Hostels.txt");
        Map<String, ArrayList<String>> HostelDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        HostelDict.remove(this.hostelID);
        fileHandler.save(HostelDict);
    }

    public static void deleteHostel (String hostelID) {
        FileHandler fileHandler = new FileHandler("Hostels.txt");
        Map<String, ArrayList<String>> HostelDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        HostelDict.remove(hostelID);
        fileHandler.save(HostelDict);
    }

    public void saveHostel(){
        List<String> newInfo = Arrays.asList(roomType, Integer.toString(price), status);

        FileHandler fileHandler = new FileHandler("Hostels.txt");
        Map<String, ArrayList<String>> hostelDict = fileHandler.parseAsDict(fileHandler.read(), FileHandler.SEPERATOR, 0);
        
        hostelDict.put(hostelID, new ArrayList<String>(newInfo));
        fileHandler.save(hostelDict);
        return;
    }

}