package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.config;

public class UserServiceException extends RuntimeException {
    /**
     * Constructs a new UserServiceException with the specified error message.
     *
     * @param message the detail message describing the specific user service error
     */
    public UserServiceException(String message) {
        super(message);
    }
}
