package twitchflix.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StreamData {
    private String id;
    private String userId;
    private String title;
    private String img;
    private String message;

    public StreamData() { }

    public StreamData(String id, String userId, String title, String img, String message) {
	this.id = id;
	this.userId = userId;
        this.title = title;
        this.img = img;
	this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() { return img; }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

