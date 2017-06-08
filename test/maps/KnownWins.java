package maps;
/**
 * Written on Wed Jun 07 23:59:24 CEST 2017
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
MAP.putAll(KnownWins_06.MAP);
}
}
