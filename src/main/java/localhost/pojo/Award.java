package localhost.pojo;

import java.sql.Timestamp;

public class Award {
    private int id;
    private String name;
    private int price;
    private int publisher_id;
    private String publish_datetime;
    private String description;

    @Override
    public String toString() {
        return "Award{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + ", publisher_id=" + publisher_id + ", publish_datetime=" + publish_datetime + ", description='" + description + '\'' + '}';
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int pulisher_id) {
        this.publisher_id = pulisher_id;
    }

    public String getPublish_datetime() {
        return publish_datetime;
    }

    public void setPublish_datetime(String publish_datetime) {
        this.publish_datetime = publish_datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
