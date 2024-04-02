package twitchflix.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserData {
    private String id;
    private String name;
    private String email;
    private String password;
    private String message;

    public UserData() { }

    public UserData(String id, String name, String email, String password, String message) {
	this.id = id;
        this.name = name;
        this.email = email;
	this.password = password;
	this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

