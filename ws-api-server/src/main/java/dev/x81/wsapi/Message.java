package dev.x81.wsapi;

import java.util.Arrays;

public class Message {
    private String type;
    private String method;
    private Object[] params;

    public Message () {}

    @Override
    public String toString () {
        String prefix = switch (type) {
            case "i" -> "INTERNAL::";
            case "m" -> "METHOD::";
            default -> "UNKNOWN::";
        };
        String params = this.params == null ? "[]" : Arrays.toString(this.params);

        return prefix + method + params;
    }

    public String getType () {
        return type;
    }

    public String getMethod () {
        return method;
    }

    public Object[] getParams () {
        return params;
    }
}
