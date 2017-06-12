package maps;
/**
 * Written on Fri Jun 09 21:07:00 CEST 2017
**/

import java.util.HashMap;
import java.util.Map;

import maps.r18d18y.MapR18D18Y_01;
import maps.r18d18y.MapR18D18Y_02;
import maps.r18d18y.MapR18D18Y_03;
import maps.r18d18y.MapR18D18Y_04;
import maps.r18d18y.MapR18D18Y_05;
import maps.r18d18y.MapR18D18Y_06;
import maps.r18d18y.MapR18D18Y_07;
import maps.r18d18y.MapR18D18Y_08;
import maps.r18d18y.MapR18D18Y_09;
import maps.r18d18y.MapR18D18Y_10;
import maps.r18d18y.MapR18D18Y_11;
import maps.r18d18y.MapR18D18Y_12;

public class MapR18D18Y {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(MapR18D18Y_01.MAP);
MAP.putAll(MapR18D18Y_02.MAP);
MAP.putAll(MapR18D18Y_03.MAP);
MAP.putAll(MapR18D18Y_04.MAP);
MAP.putAll(MapR18D18Y_05.MAP);
MAP.putAll(MapR18D18Y_06.MAP);
MAP.putAll(MapR18D18Y_07.MAP);
MAP.putAll(MapR18D18Y_08.MAP);
MAP.putAll(MapR18D18Y_09.MAP);
MAP.putAll(MapR18D18Y_10.MAP);
MAP.putAll(MapR18D18Y_11.MAP);
MAP.putAll(MapR18D18Y_12.MAP);
}
}
