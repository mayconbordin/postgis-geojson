# postgis-geojson

GeoJSON Jackson Serializers and Deserializers for PostGIS Geometry objects.

## GeoJSON Support

This library gives support for serialization/deserialization of all [Geometry Objects](http://geojson.org/geojson-spec.html#geometry-objects) defined
in the GeoJSON specification.

The relation between GeoJSON geometry objects and PostGIS objects is given below:

GeoJSON           | PostGIS
------------------| -------------
[Point](http://geojson.org/geojson-spec.html#point)| [Point](http://postgis.refractions.net/documentation/javadoc/org/postgis/Point.html)
[MultiPoint](http://geojson.org/geojson-spec.html#multipoint)| [MultiPoint](http://postgis.refractions.net/documentation/javadoc/org/postgis/MultiPoint.html)
[LineString](http://geojson.org/geojson-spec.html#linestring)| [LineString](http://postgis.refractions.net/documentation/javadoc/org/postgis/LineString.html)
[MultiLineString](http://geojson.org/geojson-spec.html#multilinestring)| [MultiLineString](http://postgis.refractions.net/documentation/javadoc/org/postgis/MultiLineString.html)
[Polygon](http://geojson.org/geojson-spec.html#polygon)| [Polygon](http://postgis.refractions.net/documentation/javadoc/org/postgis/Polygon.html)
[MultiPolygon](http://geojson.org/geojson-spec.html#multipolygon)| [MultiPolygon](http://postgis.refractions.net/documentation/javadoc/org/postgis/MultiPolygon.html)
[GeometryCollection](http://geojson.org/geojson-spec.html#geometry-collection)| [GeometryCollection](http://postgis.refractions.net/documentation/javadoc/org/postgis/GeometryCollection.html)

## Installation

Add the JitPack repository to your `<repositories>` list in the `pom.xml` file:

```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```

Then add the dependency to your `pom.xml` file:

```xml
<dependency>
  <groupId>com.github.mayconbordin</groupId>
  <artifactId>postgis-geojson</artifactId>
  <version>1.0</version>
</dependency>
```

For more information go to the [JitPack page](https://jitpack.io/#mayconbordin/postgis-geojson/).

## Usage

First you need to register the library module within the `ObjectMapper` instance you are going to use:

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new PostGISModule());
```

The you can serialize objects:

```java
String json = mapper.writeValueAsString(new Point(125.6, 10.1));
```

And deserialize them:

```java
Point point = (Point) mapper.readValue(json, Geometry.class);
```
