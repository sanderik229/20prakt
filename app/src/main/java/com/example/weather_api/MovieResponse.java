package com.example.weather_api;

import java.util.List;

public class MovieResponse {
    private int kinopoiskId;
    private String nameRu;
    private String description;
    private String year;
    private String ratingKinopoisk;
    private String posterUrl;
    private List<Genre> genres;

    public int getKinopoiskId() {
        return kinopoiskId;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getDescription() {
        return description;
    }

    public String getYear() {
        return year;
    }

    public String getRatingKinopoisk() {
        return ratingKinopoisk;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public static class Genre {
        private String genre;

        public String getGenre() {
            return genre;
        }
    }
}
