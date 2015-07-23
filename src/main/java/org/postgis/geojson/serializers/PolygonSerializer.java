package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.Polygon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PolygonSerializer extends GeometrySerializer<Polygon> {

    public PolygonSerializer() {
        super("Polygon");
    }

    @Override
    public void serialize(Polygon polygon, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        writeTypeField(json);
        writeStartCoordinates(json);

        for (int i=0; i<polygon.numRings(); i++) {
            json.writeStartArray();
            writePoints(json, polygon.getRing(i).getPoints());
            json.writeEndArray();
        }

        writeEndCoordinates(json);
        json.writeEndObject();
    }
}
