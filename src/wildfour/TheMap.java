package wildfour;
/**
 * Generated on Sat Jun 03 01:03:11 CEST 2017
 * Max. rounds 18
 * Max. depth 18
**/

import java.util.HashMap;
import java.util.Map;

public class TheMap {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(wildfour.maps.Map01.MAP);
MAP.putAll(wildfour.maps.Map02.MAP);
MAP.putAll(wildfour.maps.Map03.MAP);
MAP.putAll(wildfour.maps.Map04.MAP);
MAP.putAll(wildfour.maps.Map05.MAP);
MAP.putAll(wildfour.maps.Map06.MAP);
MAP.putAll(wildfour.maps.Map07.MAP);
MAP.putAll(wildfour.maps.Map08.MAP);
MAP.putAll(wildfour.maps.Map09.MAP);
MAP.putAll(wildfour.maps.Map10.MAP);
}
}
