package wildfour;
/**
 * Generated on Thu Jun 01 01:35:26 CEST 2017
 * Max. rounds 13
 * Max. depth 18
**/

import java.util.HashMap;
import java.util.Map;

public class TheMap {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(wildfour.maps.Map01.MAP);
MAP.putAll(wildfour.maps.Map02.MAP);
}
}
