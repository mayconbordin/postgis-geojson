package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.Point;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PointSerializer extends GeometrySerializer<Point> {

    public PointSerializer() {
        super("Point");
    }

    @Override
    public void serialize(Point point, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        writeTypeField(json);
        writeStartCoordinates(json);
        writeNumbers(json, point.getX(), point.getY(), point.getZ());
        writeEndCoordinates(json);

        json.writeEndObject();
    }
}
