package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.Geometry;
import org.postgis.Point;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;

public abstract class GeometrySerializer<T extends Geometry> extends JsonSerializer<T> {
    protected String type;

    public GeometrySerializer(String type) {
        this.type = type;
    }

    protected void writeTypeField(JsonGenerator json) throws IOException {
        json.writeStringField("type", type);
    }

    protected void writeStartCoordinates(JsonGenerator json) throws IOException {
        json.writeArrayFieldStart("coordinates");
    }

    protected void writeEndCoordinates(JsonGenerator json) throws IOException {
        json.writeEndArray();
    }

    protected void writeNumbers(JsonGenerator json, double...numbers) throws IOException {
        for (double number : numbers) {
            json.writeNumber(number);
        }
    }

    protected void writePoints(JsonGenerator json, Point[] points) throws IOException {
        for (Point point : points) {
            json.writeStartArray();
            writeNumbers(json, point.getX(), point.getY(), point.getZ());
            json.writeEndArray();
        }
    }
}
