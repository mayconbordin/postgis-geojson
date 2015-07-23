package org.postgis.geojson;

import org.postgis.geojson.serializers.MultiPolygonSerializer;
import org.postgis.geojson.serializers.GeometryCollectionSerializer;
import org.postgis.geojson.serializers.MultiPointSerializer;
import org.postgis.geojson.serializers.PolygonSerializer;
import org.postgis.geojson.serializers.MultiLineStringSerializer;
import org.postgis.geojson.serializers.PointSerializer;
import org.postgis.geojson.serializers.LineStringSerializer;
import org.postgis.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class PostGISModule extends SimpleModule {
    private static final long serialVersionUID = 1L;

    public PostGISModule() {
        super("PostGISModule");

        addSerializer(Point.class, new PointSerializer());
        addSerializer(Polygon.class, new PolygonSerializer());
        addSerializer(LineString.class, new LineStringSerializer());

        addSerializer(MultiPoint.class, new MultiPointSerializer());
        addSerializer(MultiPolygon.class, new MultiPolygonSerializer());
        addSerializer(MultiLineString.class, new MultiLineStringSerializer());

        addSerializer(GeometryCollection.class, new GeometryCollectionSerializer());
    }
}
