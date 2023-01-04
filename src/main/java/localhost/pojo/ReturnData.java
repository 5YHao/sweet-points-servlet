package localhost.pojo;

public class ReturnData {
    private int err_code;
    private Object msg;
    private Object data;

    @Override
    public String toString() {
        return "ReturnData{" +
                "err_code=" + err_code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
