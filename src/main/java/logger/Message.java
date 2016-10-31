package logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author nilstes
 */
public class Message {

    private String message;
    private Date createdAt;
    private int sentiment;
    private GeoPoint location; 

    public Message() {
    }

    public Message(String message, Date createdAt, int sentiment) {
        this.message = message;
        this.createdAt = createdAt;
        this.sentiment = sentiment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(double lat, double lon) {
        location = new GeoPoint(lat, lon);
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getSentiment() {
        return sentiment;
    }

    public void setSentiment(int sentiment) {
        this.sentiment = sentiment;
    }

    
    class GeoPoint {
        private Double lat;
        private Double lon;

        public GeoPoint() {
        }

        public GeoPoint(Double lat, Double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }
    }
}
