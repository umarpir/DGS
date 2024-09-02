package com.dgs.dgs_backend.Exceptions;

import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationMissingParametersException;
import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationNameExistsException;
import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationNotFoundException;
import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationInvalidDateFormatException;
import com.dgs.dgs_backend.Exceptions.Personnel.PersonnelInvalidInformationException;
import com.dgs.dgs_backend.Exceptions.Personnel.PersonnelNotFoundException;
import com.dgs.dgs_backend.Exceptions.Personnel.PersonnelUsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientOrganisationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleClientOrganisationNotFoundException(ClientOrganisationNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClientOrganisationNameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleClientOrganisationNameExistsException(ClientOrganisationNameExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ClientOrganisationMissingParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleClientOrganisationMissingNameException(ClientOrganisationMissingParametersException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PersonnelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlePersonnelNotFoundException(PersonnelNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PersonnelUsernameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handlePersonnelUsernameExistsException(PersonnelUsernameExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(PersonnelInvalidInformationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePersonnelInvalidInformationException(PersonnelInvalidInformationException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ClientOrganisationInvalidDateFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleClientOrganisationInvalidDateParseException(ClientOrganisationInvalidDateFormatException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
