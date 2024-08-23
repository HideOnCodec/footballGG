package com.footballgg.server.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
public class ApiResponse {
    private Filter filters;
    private ResultSet resultSet;
    private Competition competition;
    private List<Match> matches;

    // Getters and Setters
    @Getter
    public static class Filter {
        private String season;
        private String matchday;

        // Getters and Setters
    }
    @Getter
    public static class ResultSet {
        private int count;
        private String first;
        private String last;
        private int played;

        // Getters and Setters
    }
    @Getter
    public static class Competition {
        private int id;
        private String name;
        private String code;
        private String type;
        private String emblem;

        // Getters and Setters
    }
    @Getter
    @Setter
    public static class Match {
        private Area area;
        private Competition competition;
        private Season season;
        private int id;
        @JsonProperty("utcDate")
        private String utcDate;
        private String status;
        private int matchday;
        private String stage;
        private String group;
        private String lastUpdated;
        private Team homeTeam;
        private Team awayTeam;
        private Score score;
        private Odds odds;
        private List<Referee> referees;

        // Getters and Setters
    }
    @Getter
    public static class Area {
        private int id;
        private String name;
        private String code;
        private String flag;

        // Getters and Setters
    }
    @Getter
    public static class Season {
        private int id;
        private String startDate;
        private String endDate;
        private int currentMatchday;
        private Object winner; // Use Object because winner can be null

        // Getters and Setters
    }
    @Getter
    public static class Team {
        private int id;
        private String name;
        private String shortName;
        private String tla;
        private String crest;

        // Getters and Setters
    }
    @Getter
    public static class Score {
        private String winner;
        private String duration;
        private FullTime fullTime;
        private HalfTime halfTime;

        // Getters and Setters
        @Getter
        public static class FullTime {
            private Integer home;
            private Integer away;

            // Getters and Setters
        }
        @Getter
        public static class HalfTime {
            private Integer home;
            private Integer away;

            // Getters and Setters
        }
    }
    @Getter
    public static class Odds {
        @JsonProperty("msg")
        private String message;

        // Getters and Setters
    }
    @Getter
    public static class Referee {
        // Referee details if needed; otherwise, it can remain empty

        // Getters and Setters
    }
}
