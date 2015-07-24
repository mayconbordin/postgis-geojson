package org.postgis.geojson.serializers;

import java.io.IOException;

import org.postgis.Geometry;
import org.postgis.Point;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.postgis.GeometryCollection;
import org.postgis.LineString;
import org.postgis.MultiLineString;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.Polygon;
import static org.postgis.geojson.GeometryTypes.*;

/**
 * Serializer for Geometry types.
 * 
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public class GeometrySerializer extends JsonSerializer<Geometry> {
    
    @Override
    public void serialize(Geometry geom, JsonGenerator json, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        json.writeStartObject();

        if (geom instanceof Point) {
            serializePoint((Point)geom, json);
        } else if (geom instanceof Polygon) {
            serializePolygon((Polygon)geom, json);
        } else if (geom instanceof LineString) {
            serializeLineString((LineString)geom, json);
        } else if (geom instanceof MultiPolygon) {
            serializeMultiPolygon((MultiPolygon)geom, json);
        } else if (geom instanceof MultiPoint) {
            serializeMultiPoint((MultiPoint)geom, json);
        } else if (geom instanceof MultiLineString) {
            serializeMultiLineString((MultiLineString)geom, json);
        } else if (geom instanceof GeometryCollection) {
            serializeGeometryCollection((GeometryCollection)geom, json);
        }

        json.writeEndObject();
    }
    
    protected void serializeGeometryCollection(GeometryCollection gc, JsonGenerator json) throws IOException {
        writeTypeField(GEOMETRY_COLLECTION, json);
        json.writeArrayFieldStart("geometries");
        
        for (Geometry geom : gc.getGeometries()) {
            serialize(geom, json, null);
        }
        
        json.writeEndArray();
    }
    
    protected void serializeMultiLineString(MultiLineString mls, JsonGenerator json) throws IOException {
        writeTypeField(MULTI_LINE_STRING, json);
        writeStartCoordinates(json);

        for (LineString ls : mls.getLines()) {
            json.writeStartArray();
            writePoints(json, ls.getPoints());
            json.writeEndArray();
        }

        writeEndCoordinates(json);
    }
    
    protected void serializeMultiPoint(MultiPoint mp, JsonGenerator json) throws IOException {
        writeTypeField(MULTI_POINT, json);
        writeStartCoordinates(json);
        writePoints(json, mp.getPoints());
        writeEndCoordinates(json);
    }
    
    protected void serializeMultiPolygon(MultiPolygon mp, JsonGenerator json) throws IOException {
        writeTypeField(MULTI_POLYGON, json);
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
    }
    
    protected void serializeLineString(LineString ls, JsonGenerator json) throws IOException {
        writeTypeField(LINE_STRING, json);
        writeStartCoordinates(json);
        writePoints(json, ls.getPoints());
        writeEndCoordinates(json);
    }
    
    protected void serializePolygon(Polygon polygon, JsonGenerator json) throws IOException {
        writeTypeField(POLYGON, json);
        writeStartCoordinates(json);

        for (int i=0; i<polygon.numRings(); i++) {
            json.writeStartArray();
            writePoints(json, polygon.getRing(i).getPoints());
            json.writeEndArray();
        }

        writeEndCoordinates(json);
    }
    
    protected void serializePoint(Point point, JsonGenerator json) throws IOException {
        writeTypeField(POINT, json);
        writeStartCoordinates(json);
        writeNumbers(json, point.getX(), point.getY(), point.getZ());
        writeEndCoordinates(json);
    }

    protected void writeTypeField(String type, JsonGenerator json) throws IOException {
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
