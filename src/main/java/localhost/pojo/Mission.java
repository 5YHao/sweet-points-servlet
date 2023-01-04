package localhost.pojo;

import java.sql.Timestamp;

public class Mission {
    private int id;
    private  String name;
    private int publisher_id;
    private int points;
    private String description;
    private String publish_datetime;

    @Override
    public String toString() {
        return "Mission{" + "id=" + id + ", name='" + name + '\'' + ", publisher_id='" + publisher_id + '\'' + ", " +
                "points=" + points + ", description='" + description + '\'' + ", publish_datetime=" + publish_datetime + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublish_datetime() {
        return publish_datetime;
    }

    public void setPublish_datetime(String publish_datetime) {
        this.publish_datetime = publish_datetime;
    }
}
