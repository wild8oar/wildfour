package wildfour;

import java.util.Arrays;
import java.util.List;

public class CalcEncoding {
	
	private static final List<String> PATTERNS = Arrays.asList(new String[] {"",
			"1", "2",
			"11", "12", "21", "22",
			"111", "112", "121", "122", "211", "212", "221", "222",
			"1112", "1121", "1122", "1211", "1212", "1221", "1222",
			"2111", "2112", "2121", "2122", "2211", "2212", "2221",
			"11121", "11122", "11211", "11212", "11221", "11222",
			"12111", "12112", "12121", "12122", "12211", "12212", "12221",
			"21112", "21121", "21122", "21211", "21212", "21221", "21222",
			"22111", "22112", "22121", "22122", "22211", "22212",
			"111211", "111212", "111221", "111222",
			"112111", "112112", "112121", "112122", "112211", "112212", "112221",
			"121112", "121121", "121122", "121211", "121212", "121221", "121222",
			"122111", "122112", "122121", "122122", "122211", "122212",
			"211121", "211122", "211211", "211212", "211221", "211222",
			"212111", "212112", "212121", "212122", "212211", "212212", "212221",
			"221112", "221121", "221122", "221211", "221212", "221221", "221222",
			"222111", "222112", "222121", "222122"	
	});
	
	private static final long[] POW3 = {1, 3, 9, 27, 81, 243};

	public static void main(String[] args) {
		int[] map = new int[720];
		int[] rev = new int[PATTERNS.size()];
		for (int i=0; i<map.length; i++) {
			map[i] = -1;
		}
		for (int i=0; i< PATTERNS.size(); i++) {
			String pat = PATTERNS.get(i);
			int v = 0;
			for (int p=0; p<pat.length(); p++) {
				int x = Integer.parseInt(pat.substring(p, p+1));
				if (x>2) throw new IllegalStateException("x=" + x);
				v += x*POW3[p];
			} 
			map[v] = i;
			rev[i] = v;
		}
		System.out.println(Arrays.toString(map));
		System.out.println(Arrays.toString(rev));
	}
	
}
