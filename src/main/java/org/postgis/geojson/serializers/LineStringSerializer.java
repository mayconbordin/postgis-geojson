package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.LineString;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LineStringSerializer extends GeometrySerializer<LineString> {

    public LineStringSerializer() {
        super("LineString");
    }

    @Override
    public void serialize(LineString ls, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        writeTypeField(json);
        writeStartCoordinates(json);

        writePoints(json, ls.getPoints());

        writeEndCoordinates(json);
        json.writeEndObject();
    }
}
