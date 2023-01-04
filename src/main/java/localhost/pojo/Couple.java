package localhost.pojo;

public class Couple {
    private Integer id;
    private  Integer boy_id;
    private  Integer girl_id;

    @Override
    public String toString() {
        return "Couple{" + "id=" + id + ", boy_id=" + boy_id + ", girl_id=" + girl_id + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getBoy_id() {
        return boy_id;
    }

    public void setBoy_id(int boy_id) {
        this.boy_id = boy_id;
    }

    public Integer getGirl_id() {
        return girl_id;
    }

    public void setGirl_id(int girl_id) {
        this.girl_id = girl_id;
    }
}
