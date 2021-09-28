package models;

public class Response {
    private String message;
    private Boolean succeeded;
    private Object data;

    public Response(){}

    public Response(String message, Boolean succeeded, Object data) {
        this.message = message;
        this.succeeded = succeeded;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(Boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", succeeded=" + succeeded +
                ", data=" + data +
                '}';
    }
}
