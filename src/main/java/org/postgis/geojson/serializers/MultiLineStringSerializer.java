package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.LineString;
import org.postgis.MultiLineString;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MultiLineStringSerializer extends GeometrySerializer<MultiLineString> {

    public MultiLineStringSerializer() {
        super("MultiLineString");
    }

    @Override
    public void serialize(MultiLineString mls, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        writeTypeField(json);
        writeStartCoordinates(json);

        for (LineString ls : mls.getLines()) {
            json.writeStartArray();
            writePoints(json, ls.getPoints());
            json.writeEndArray();
        }

        writeEndCoordinates(json);
        json.writeEndObject();
    }
}
