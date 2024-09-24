package org.example.telegrambot.model;

import java.util.List;
import lombok.Data;

@Data
public class GeocodingResponse {

    private List<Result> results;

    @Data
    public static class Result {

        private List<AddressComponent> addressComponents;
        private String formattedAddress;
        private Geometry geometry;
        private String placeId;
        private List<String> types;
    }

    @Data
    public static class AddressComponent {

        private String longName;
        private String shortName;
        private List<String> types;
    }

    @Data
    public static class Geometry {

        private Bounds bounds;
        private Location location;
        private String locationType;
        private Viewport viewport;
    }

    @Data
    public static class Bounds {

        private Location northeast;
        private Location southwest;
    }

    @Data
    public static class Location {

        private double lat;
        private double lng;
    }

    @Data
    public static class Viewport {

        private Location northeast;
        private Location southwest;
    }
}
