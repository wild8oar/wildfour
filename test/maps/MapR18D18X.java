package maps;
/**
 * Written on Fri Jun 09 08:20:47 CEST 2017
 * 
 * R18D18 after optimizing wins to 24 and losses to 18.
**/

import java.util.HashMap;
import java.util.Map;

import maps.r18d18x.MapR18D18X_01;
import maps.r18d18x.MapR18D18X_02;
import maps.r18d18x.MapR18D18X_03;
import maps.r18d18x.MapR18D18X_04;
import maps.r18d18x.MapR18D18X_05;
import maps.r18d18x.MapR18D18X_06;
import maps.r18d18x.MapR18D18X_07;
import maps.r18d18x.MapR18D18X_08;
import maps.r18d18x.MapR18D18X_09;
import maps.r18d18x.MapR18D18X_10;

public class MapR18D18X {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(MapR18D18X_01.MAP);
MAP.putAll(MapR18D18X_02.MAP);
MAP.putAll(MapR18D18X_03.MAP);
MAP.putAll(MapR18D18X_04.MAP);
MAP.putAll(MapR18D18X_05.MAP);
MAP.putAll(MapR18D18X_06.MAP);
MAP.putAll(MapR18D18X_07.MAP);
MAP.putAll(MapR18D18X_08.MAP);
MAP.putAll(MapR18D18X_09.MAP);
MAP.putAll(MapR18D18X_10.MAP);
}
}
