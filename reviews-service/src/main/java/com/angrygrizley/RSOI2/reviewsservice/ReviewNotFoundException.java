package com.angrygrizley.RSOI2.reviewsservice;

public class ReviewNotFoundException extends Exception{
    public ReviewNotFoundException(Long id){
            super("Review with id= " + id + " was not found!");
        }

}
