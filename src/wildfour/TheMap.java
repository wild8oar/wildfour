package wildfour;
/**
 * Generated on Tue May 30 13:48:50 CEST 2017
 * Max. rounds 7
 * Max. depth 18
**/

import java.util.HashMap;
import java.util.Map;

public class TheMap {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(wildfour.maps.Map01.MAP);
}
}
