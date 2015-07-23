package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.MultiPoint;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MultiPointSerializer extends GeometrySerializer<MultiPoint> {

    public MultiPointSerializer() {
        super("MultiPoint");
    }

    @Override
    public void serialize(MultiPoint mp, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        writeTypeField(json);
        writeStartCoordinates(json);
        writePoints(json, mp.getPoints());
        writeEndCoordinates(json);

        json.writeEndObject();
    }
}
