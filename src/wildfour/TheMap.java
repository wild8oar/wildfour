package wildfour;

/*
 * R18D18, optimized some losses, recomputed moves to depth 18.
 * Optimized wins to depth 24.
 */

import java.util.HashMap;
import java.util.Map;

import wildfour.maps.MapR18D18Z_01;
import wildfour.maps.MapR18D18Z_02;
import wildfour.maps.MapR18D18Z_03;
import wildfour.maps.MapR18D18Z_04;
import wildfour.maps.MapR18D18Z_05;
import wildfour.maps.MapR18D18Z_06;
import wildfour.maps.MapR18D18Z_07;
import wildfour.maps.MapR18D18Z_08;
import wildfour.maps.MapR18D18Z_09;
import wildfour.maps.MapR18D18Z_10;
import wildfour.maps.MapR18D18Z_11;

public class TheMap {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(MapR18D18Z_01.MAP);
MAP.putAll(MapR18D18Z_02.MAP);
MAP.putAll(MapR18D18Z_03.MAP);
MAP.putAll(MapR18D18Z_04.MAP);
MAP.putAll(MapR18D18Z_05.MAP);
MAP.putAll(MapR18D18Z_06.MAP);
MAP.putAll(MapR18D18Z_07.MAP);
MAP.putAll(MapR18D18Z_08.MAP);
MAP.putAll(MapR18D18Z_09.MAP);
MAP.putAll(MapR18D18Z_10.MAP);
MAP.putAll(MapR18D18Z_11.MAP);
}
}
