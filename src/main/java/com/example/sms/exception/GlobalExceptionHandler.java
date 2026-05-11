package com.example.sms.exception;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestControllerAdvice(basePackages = "com.example.sms.controller.api")
class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> nf(ResourceNotFoundException e){
        return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> v(MethodArgumentNotValidException e){
        Map<String,String> errs = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> errs.put(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("errors", errs));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> ex(Exception e){
        log.error("API error", e);
        return ResponseEntity.status(500).body(Map.of("error","Internal server error"));
    }
}

@ControllerAdvice(basePackages = "com.example.sms.controller")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView nf(ResourceNotFoundException e, HttpServletRequest req){
        ModelAndView mv = new ModelAndView("error/404"); mv.addObject("message", e.getMessage());
        mv.setStatus(HttpStatus.NOT_FOUND); return mv;
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView ex(Exception e){
        log.error("Web error", e);
        ModelAndView mv = new ModelAndView("error/500"); mv.addObject("message", e.getMessage());
        mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR); return mv;
    }
}
