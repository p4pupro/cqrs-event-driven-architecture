package es.dperez.query.domain.exception;

public class DeviceNotFoundException extends Exception {

    private String message;
    private String name;

    public DeviceNotFoundException(final String name, final String message) {
        super(message);
        this.name = name;
    }
}
