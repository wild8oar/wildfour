package wildfour;
/**
 * Generated on Thu Jun 01 18:19:35 CEST 2017
 * Max. rounds 15
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
}
}
