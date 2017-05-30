package wildfour;
/**
 * Generated on Tue May 30 08:53:36 CEST 2017
 * Max. rounds 5
 * Max. depth 16
**/

import java.util.HashMap;
import java.util.Map;

public class TheMap {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(wildfour.maps.Map01.MAP);
}
}
