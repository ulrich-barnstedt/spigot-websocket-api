package dev.x81.wsapi;

import com.google.gson.Gson;

public class Response {
    private final ResponseStatus status;
    private final Object body;
    private static Gson gson = new Gson();

    public Response (ResponseStatus status, Object body) {
        this.status = status;
        this.body = body;
    }

    public Response (ResponseStatus status) {
        this(status, null);
    }

    public String toJSON () {
        return gson.toJson(this);
    }
}
