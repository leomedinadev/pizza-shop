package ec.com.leodev.pizzashop.service.exception;

public class EmailApiException extends RuntimeException {
    public EmailApiException() {
        super("Error sending email...");
    }
}
