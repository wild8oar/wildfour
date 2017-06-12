package maps;
/**
 * Written on Mon Jun 12 15:36:39 CEST 2017
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
MAP.putAll(KnownWins_07.MAP);
MAP.putAll(KnownWins_08.MAP);
MAP.putAll(KnownWins_09.MAP);
MAP.putAll(KnownWins_10.MAP);
MAP.putAll(KnownWins_11.MAP);
}
}
