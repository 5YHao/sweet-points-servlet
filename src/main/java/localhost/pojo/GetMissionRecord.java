package localhost.pojo;

import java.sql.Timestamp;

public class GetMissionRecord {
    private int id;
    private int mission_id;
    private int user_id;
    private int status;
    private String get_datetime;
    private String name;
    private String description;
    private int points;

    @Override
    public String toString() {
        return "GetMissionRecord{" + "id=" + id + ", mission_id=" + mission_id + ", user_id=" + user_id + ", status=" + status + ", get_datetime='" + get_datetime + '\'' + ", name='" + name + '\'' + ", description='" + description + '\'' + ", points=" + points + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGet_datetime() {
        return get_datetime;
    }

    public void setGet_datetime(String get_datetime) {
        this.get_datetime = get_datetime;
    }
}
