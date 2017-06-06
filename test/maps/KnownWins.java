package maps;
/**
 * Written on Tue Jun 06 15:07:14 CEST 2017
**/

import java.util.HashMap;
import java.util.Map;

public class KnownWins {

public static final Map<String, Integer> MAP = new HashMap<>();

static {
MAP.putAll(KnownWins_01.MAP);
MAP.putAll(KnownWins_02.MAP);
MAP.putAll(KnownWins_03.MAP);
MAP.putAll(KnownWins_04.MAP);
MAP.putAll(KnownWins_05.MAP);
}
}
