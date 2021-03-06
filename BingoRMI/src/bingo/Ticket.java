package bingo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Lucas Roncato
 * */
public class Ticket {
	List<Integer> list = new ArrayList<Integer>();

	public Ticket() {
		int test;
		while (list.size() < 25) {
			if (!(list.contains((test = randomNumber(1, 99))))) {
				list.add(test);
			}
		}
		Collections.sort(list);
		System.out.println(list);
	}

	public List<Integer> getList() {
		return list;
	}

	public Map<Double, Integer> getMap() {
		Map<Double, Integer> map = new TreeMap<Double, Integer>();
		int count = 0;
		for (double i = 0; i < 5; i++) {
			for (double j = 0; j < 5; j++) {
				if (count < 25) {
					map.put(i + (j / 10), list.get(count));
					count++;
				}
			}
		}
		return map;
	}

	public static int randomNumber(int min, int max) {
		return new Random().nextInt(max) + min;
	}

}
