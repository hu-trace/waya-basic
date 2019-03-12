package com.waya.support.extend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>JSON格式的HashMap
 * <p>继承{@link HashMap},重写{@link #toString()}方法实现
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @see HashMap
 * @since 1.8
 * @time 2019年2月22日
 */
public class JsonMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = 5573280320610714532L;

	public JsonMap() {
        super();
    }
	
	public JsonMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

	@Override
	public String toString() {
		Iterator<java.util.Map.Entry<K, V>> i = entrySet().iterator();
		if (!i.hasNext()) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (;;) {
			java.util.Map.Entry<K, V> e = i.next();
			K key = e.getKey();
			V value = e.getValue();
			sb.append('"');
			sb.append(key == this ? "(this Map)" : key);
			sb.append('"');
			sb.append(':');
			sb.append(value == this ? "(this Map)" : value);
			if (!i.hasNext()) {
				return sb.append('}').toString();
			}
			sb.append(',').append(' ');
		}
	}

}
