package org.postgis.geojson.deserializers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.postgis.Geometry;
import org.postgis.GeometryCollection;
import org.postgis.LineString;
import org.postgis.LinearRing;
import org.postgis.MultiLineString;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.Point;
import org.postgis.Polygon;
import static org.postgis.geojson.GeometryTypes.*;

/**
 * Deserializer for Geometry types.
 * 
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public class GeometryDeserializer extends JsonDeserializer<Geometry> {
    
    @Override
    public Geometry deserialize(JsonParser jp, DeserializationContext dc) 
            throws IOException, JsonProcessingException {
        String fieldName;
        String type = null;
        
        while (true) {
            fieldName = jp.nextFieldName();

            if (fieldName == null) {
                return null;
            } else if (fieldName.equals("type")) {
                type = jp.nextTextValue();
            } else if (fieldName.equals("coordinates")) {
                JsonNode node = jp.readValueAsTree();
                JsonNode coordinates = node.get("coordinates");
                
                return coordinatesToGeometry(type, coordinates, jp);
            } else if (fieldName.equals("geometries")) {
                JsonNode node = jp.readValueAsTree();
                JsonNode geometries = node.get("geometries");
                
                return new GeometryCollection(readNodeAsGeometryArray(geometries, jp));
            }
        }
    }
    
    protected Geometry coordinatesToGeometry(String type, JsonNode coordinates, JsonParser jp)
            throws JsonParseException {
        switch (type) {
            case POINT:
                return readNodeAsPoint(coordinates);
            case LINE_STRING:
                return readNodeAsLineString(coordinates);
            case POLYGON:
                return new Polygon(readNodeAsLinearRingArray(coordinates));
            case MULTI_POINT:
                return new MultiPoint(readNodeAsPointArray(coordinates));
            case MULTI_LINE_STRING:
                return new MultiLineString(readNodeAsLineStringArray(coordinates));
            case MULTI_POLYGON:
                return new MultiPolygon(readNodeAsPolygonArray(coordinates));
            default:
                throw new JsonParseException("\""+type+"\" is not a valid Geometry type.",
                        jp.getCurrentLocation());
        }
    }
    
    protected Geometry[] readNodeAsGeometryArray(JsonNode node, JsonParser jp) throws JsonParseException {
        if (!node.isArray()) {
            return null;
        }
        
        List<Geometry> values = new ArrayList<>();
        Iterator<JsonNode> it = node.iterator();
        
        while (it.hasNext()) {
            JsonNode val = it.next();
            if (val.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = val.fields();
                String type = null;
                JsonNode coordinates = null;
                
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> e = fields.next();
                    
                    if (e.getKey().equals("type")) {
                        type = e.getValue().asText();
                    } else if (e.getKey().equals("coordinates")) {
                        coordinates = e.getValue();
                    }
                }
                
                values.add(coordinatesToGeometry(type, coordinates, jp));
            }
        }

        return values.toArray(new Geometry[values.size()]);
    }
    
    protected LineString[] readNodeAsLineStringArray(JsonNode node) {
        if (!node.isArray()) {
            return null;
        }
        
        List<LineString> values = new ArrayList<>();
        Iterator<JsonNode> it = node.iterator();
        
        while (it.hasNext()) {
            JsonNode val = it.next();
            if (val.isArray()) {
                values.add(readNodeAsLineString(val));
            }
        }

        return values.toArray(new LineString[values.size()]);
    }
    
    protected LineString readNodeAsLineString(JsonNode node) {
        Point[] points = readNodeAsPointArray(node);
        return new LineString(points);
    }
    
    protected Polygon[] readNodeAsPolygonArray(JsonNode node) {
        if (!node.isArray()) {
            return null;
        }
        
        List<Polygon> values = new ArrayList<>();
        Iterator<JsonNode> it = node.iterator();
        
        while (it.hasNext()) {
            JsonNode val = it.next();
            if (val.isArray()) {
                values.add(new Polygon(readNodeAsLinearRingArray(val)));
            }
        }

        return values.toArray(new Polygon[values.size()]);
    }
    
    protected LinearRing[] readNodeAsLinearRingArray(JsonNode node) {
        if (!node.isArray()) {
            return null;
        }
        
        List<LinearRing> values = new ArrayList<>();
        Iterator<JsonNode> it = node.iterator();
        
        while (it.hasNext()) {
            JsonNode val = it.next();
            if (val.isArray()) {
                values.add(readNodeAsLinearRing(val));
            }
        }

        return values.toArray(new LinearRing[values.size()]);
    }
    
    protected LinearRing readNodeAsLinearRing(JsonNode node) {
        Point[] points = readNodeAsPointArray(node);
        return new LinearRing(points);
    }
    
    protected Point[] readNodeAsPointArray(JsonNode node) {
        if (!node.isArray()) {
            return null;
        }
        
        List<Point> values = new ArrayList<>();
        Iterator<JsonNode> it = node.iterator();
        
        while (it.hasNext()) {
            JsonNode val = it.next();
            if (val.isArray()) {
                values.add(readNodeAsPoint(val));
            }
        }

        return values.toArray(new Point[values.size()]);
    }

    protected Point readNodeAsPoint(JsonNode node) {
        if (!node.isArray()) {
            return null;
        }
        
        List<Double> values = new ArrayList<>();
        Iterator<JsonNode> it = node.iterator();
        
        while (it.hasNext()) {
            values.add(it.next().asDouble());
        }
        
        return new Point(values.get(0), values.get(1), values.size() > 2 ? values.get(2) : 0.0);
    }
}
