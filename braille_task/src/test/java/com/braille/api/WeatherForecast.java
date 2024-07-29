package com.braille.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherForecast {
    public Integer id;
    public String date;
    public Integer temperatureC;
    public Integer temperatureF;
    public String summary;

    @JsonCreator
    public WeatherForecast(
            @JsonProperty("id") Integer id,
            @JsonProperty("date") String date,
            @JsonProperty("temperatureC") Integer temperatureC,
            @JsonProperty("summary") String summary) {
        this.id = id;
        this.date = date;
        this.temperatureC = temperatureC;
        this.summary = summary;
    }
}