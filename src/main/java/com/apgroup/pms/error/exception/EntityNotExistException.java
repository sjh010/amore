package com.apgroup.pms.error.exception;

public class EntityNotExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotExistException() {
        super();
    }

    public EntityNotExistException(String message) {
        super(message);
    }

    public EntityNotExistException(Throwable cause) {
        super(cause);
    }

    public EntityNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
