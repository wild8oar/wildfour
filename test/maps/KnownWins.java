package maps;
/**
 * Written on Mon Jun 05 09:34:57 CEST 2017
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
