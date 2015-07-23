package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.Geometry;
import org.postgis.GeometryCollection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GeometryCollectionSerializer extends GeometrySerializer<GeometryCollection> {

    public GeometryCollectionSerializer() {
        super("GeometryCollection");
    }

    @Override
    public void serialize(GeometryCollection coll, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        writeTypeField(json);
        json.writeArrayFieldStart("geometries");

        for (Geometry g : coll.getGeometries()) {
            json.writeObject(g);
        }

        json.writeEndArray();
        json.writeEndObject();
    }
}
