package com.angrygrizley.RSOI2.gamesservice;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(Long id){
        super("User with id= " + id + " was not found!");
    }
}
