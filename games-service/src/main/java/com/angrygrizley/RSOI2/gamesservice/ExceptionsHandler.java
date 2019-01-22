package com.angrygrizley.RSOI2.gamesservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler {
    private Logger logger;

    public ExceptionsHandler() {
        logger = LoggerFactory.getLogger(ExceptionsHandler.class);
    }

    @ResponseBody
    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String gameNotFoundHandler(GameNotFoundException e){
        logger.error("Error while looking for game: " + e);
        return e.getMessage();
    }
}
