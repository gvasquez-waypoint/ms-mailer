package cl.waypoint.ms.mailer.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerController {

	private static final Logger LOGGER = Logger.getLogger(ExceptionHandlerController.class.getName());

	@ExceptionHandler(Exception.class)
	public ResponseEntity genericHandler(HandlerMethod method, Exception ex) {
		return responseError(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity handleValidation(HandlerMethod method, ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

		Set<String> messages = new HashSet<>(constraintViolations.size());
		messages.addAll(constraintViolations.stream()
				.map(constraintViolation -> String.format("%s value '%s' %s", constraintViolation.getPropertyPath(),
						constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
				.collect(Collectors.toList()));
		return responseError(new IllegalArgumentException("JSON con datos incorrectos", ex), HttpStatus.BAD_REQUEST,
				messages.toString());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity handleWrongContentType(HandlerMethod method, HttpMediaTypeNotSupportedException ex) {
		return responseError(new IllegalArgumentException("Content-Type no adecuado, debe ser application/json", ex),
				HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity handleEmptyBody(HandlerMethod method, HttpMessageNotReadableException ex) {
		return responseError(new IllegalArgumentException("Cuerpo de la petición vacío o mal formado", ex),
				HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	private ResponseEntity responseError(Exception ex, HttpStatus status, String detail) {
		LOGGER.log(Level.WARNING, ex.getMessage(), ex);
		ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
		HttpStatus nstatus;
		if (status != null) {
			nstatus = status;
		} else if (responseStatus != null) {
			nstatus = responseStatus.value();
		} else {
			nstatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return responseRestError(ex, nstatus, detail);
	}

	private ResponseEntity responseRestError(Exception ex, HttpStatus status, String detail) {
		RestMessage body = new RestMessage(status.value(), ex.getMessage(), detail);
		return new ResponseEntity<>(body, status);
	}

}
