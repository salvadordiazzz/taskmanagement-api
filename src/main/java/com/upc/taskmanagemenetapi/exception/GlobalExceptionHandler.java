package com.upc.taskmanagemenetapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource Not Found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", request.getDescription(false));
        return pd;
    }
    @ExceptionHandler(DuplicateResurceException.class)
    public ProblemDetail handleResourceDuplicateException(DuplicateResurceException ex,WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Resource Already Exists");
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", request.getDescription(false));
        return pd;
    }
    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(BusinessRuleException ex) {
        ProblemDetail pd= ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Business Rule Violation");
        pd.setDetail(ex.getMessage());
        return pd;
    }
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal Server Error");
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", request.getDescription(false));
        return pd;
    }
}
