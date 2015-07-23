package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.MultiPolygon;
import org.postgis.Polygon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MultiPolygonSerializer extends GeometrySerializer<MultiPolygon> {

    public MultiPolygonSerializer() {
        super("MultiPolygon");
    }

    @Override
    public void serialize(MultiPolygon mp, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        writeTypeField(json);
        writeStartCoordinates(json);

        for (Polygon polygon : mp.getPolygons()) {
            json.writeStartArray();

            for (int i=0; i<polygon.numRings(); i++) {
                json.writeStartArray();
                writePoints(json, polygon.getRing(i).getPoints());
                json.writeEndArray();
            }

            json.writeEndArray();
        }

        writeEndCoordinates(json);
        json.writeEndObject();
    }
}
