package it.algos.evento.entities.lettera;

import java.util.HashMap;

@SuppressWarnings("serial")
public class LetteraMap extends HashMap<LetteraKeys, String> {

	public void add(LetteraKeys key, String value) {
		put(key, value);
	}

	public HashMap<String, String> getEscapeMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		for (LetteraKeys lettKey : keySet()) {
			String key = lettKey.getKey();
			String value = get(lettKey);
			map.put(key, value);
		}
		return map;
	}

}
