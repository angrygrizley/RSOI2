package com.angrygrizley.RSOI2.gamesservice;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;


@Entity
@Table(name = "GAMES")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   // TODO: add ISBN to entity

    @Column(name = "NAME")
    private String name;

    @Column(name = "DEVELOPER")
    private String developer;

    @Lob
    @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "REVIEWS_NUM", columnDefinition = "int default 0")
    private int reviewsNum;

    @Column(name = "RATING", columnDefinition = "double precision default 0.0")
    private double rating;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReviewsNum() {
        return reviewsNum;
    }

    public void setReviewsNum(int reviewsNum) {
        this.reviewsNum = reviewsNum;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return new EqualsBuilder()
                .append(reviewsNum, game.reviewsNum)
                .append(rating, game.rating)
                .append(id, game.id)
                .append(name, game.name)
                .append(developer, game.developer)
                .append(description, game.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(developer)
                .append(description)
                .append(reviewsNum)
                .append(rating)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", developer='" + developer + '\'' +
                ", description='" + description + '\'' +
                ", reviewsNum=" + reviewsNum +
                ", rating=" + rating +
                '}';
    }
}
