package wildfour;
/**
 * Generated on Mon May 29 19:15:41 CEST 2017
 * Max. rounds 5
 * Max. depth 13
**/

import java.util.HashMap;
import java.util.Map;

public class TheMap {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(wildfour.maps.Map01.MAP);
}
}
