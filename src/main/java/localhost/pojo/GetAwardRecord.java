package localhost.pojo;

import java.sql.Timestamp;

public class GetAwardRecord {
    private  int id;
    private  int award_id;
    private  int user_id;
    private String get_datetime;
    private int status;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "GetAwardRecord{" + "id=" + id + ", award_id=" + award_id + ", user_id=" + user_id + ", get_datetime" +
                "='" + get_datetime + '\'' + ", status=" + status + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
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



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAward_id() {
        return award_id;
    }

    public void setAward_id(int award_id) {
        this.award_id = award_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGet_datetime() {
        return get_datetime;
    }

    public void setGet_datetime(String get_datetime) {
        this.get_datetime = get_datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
