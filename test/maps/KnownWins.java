package maps;
/**
 * Written on Sat Jun 03 17:11:53 CEST 2017
**/

import java.util.HashMap;
import java.util.Map;

public class KnownWins {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(KnownWins_01.MAP);
MAP.putAll(KnownWins_02.MAP);
}
}
