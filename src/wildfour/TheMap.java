package wildfour;
/**
 * Written on Sat Jun 10 16:48:12 CEST 2017
**/

import java.util.HashMap;
import java.util.Map;

import wildfour.maps.MapR19D20Y_01;
import wildfour.maps.MapR19D20Y_02;
import wildfour.maps.MapR19D20Y_03;
import wildfour.maps.MapR19D20Y_04;
import wildfour.maps.MapR19D20Y_05;
import wildfour.maps.MapR19D20Y_06;
import wildfour.maps.MapR19D20Y_07;
import wildfour.maps.MapR19D20Y_08;
import wildfour.maps.MapR19D20Y_09;
import wildfour.maps.MapR19D20Y_10;
import wildfour.maps.MapR19D20Y_11;
import wildfour.maps.MapR19D20Y_12;
import wildfour.maps.MapR19D20Y_13;
import wildfour.maps.MapR19D20Y_14;
import wildfour.maps.MapR19D20Y_15;
import wildfour.maps.MapR19D20Y_16;
import wildfour.maps.MapR19D20Y_17;



public class TheMap {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(MapR19D20Y_01.MAP);
MAP.putAll(MapR19D20Y_02.MAP);
MAP.putAll(MapR19D20Y_03.MAP);
MAP.putAll(MapR19D20Y_04.MAP);
MAP.putAll(MapR19D20Y_05.MAP);
MAP.putAll(MapR19D20Y_06.MAP);
MAP.putAll(MapR19D20Y_07.MAP);
MAP.putAll(MapR19D20Y_08.MAP);
MAP.putAll(MapR19D20Y_09.MAP);
MAP.putAll(MapR19D20Y_10.MAP);
MAP.putAll(MapR19D20Y_11.MAP);
MAP.putAll(MapR19D20Y_12.MAP);
MAP.putAll(MapR19D20Y_13.MAP);
MAP.putAll(MapR19D20Y_14.MAP);
MAP.putAll(MapR19D20Y_15.MAP);
MAP.putAll(MapR19D20Y_16.MAP);
MAP.putAll(MapR19D20Y_17.MAP);
}
}
