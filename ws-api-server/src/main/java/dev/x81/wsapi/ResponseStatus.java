package dev.x81.wsapi;

public enum ResponseStatus {
    CONTENT,
    ERROR;

    @Override
    public String toString () {
        return this.name();
    }
}
