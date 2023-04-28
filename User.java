import java.util.ArrayList;

public class User {
    private String username;
    private String name; 

    public User(ArrayList<String> userDetails) {
        loadInfo(userDetails);
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void loadInfo(ArrayList<String> data) {
        this.username = data.get(3);
        this.name = data.get(2);
    }
}
