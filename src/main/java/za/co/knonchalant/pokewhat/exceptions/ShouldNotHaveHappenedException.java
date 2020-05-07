package za.co.knonchalant.pokewhat.exceptions;

public class ShouldNotHaveHappenedException extends RuntimeException {

    public ShouldNotHaveHappenedException(String message) {
        super(message);
    }

    public ShouldNotHaveHappenedException(String message, Throwable cause) {
        super(message, cause);
    }
}


