package org.igsl.functor.memoize;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

class Handler implements InvocationHandler {
	
	Object obj;
	HashMap maps;
	
	Handler(Object obj, String[] methodNames) {
		this.obj = obj;
		this.maps = new HashMap<String, HashMap<Object, Object>>();

		for(String methodName : methodNames) {
			for(Method method : obj.getClass().getMethods()) {
				if(methodName.equalsIgnoreCase(method.getName())) {
					if(method.getAnnotation(Memoize.class) != null) {
						System.out.println("annotation found");
						maps.put(methodName, new HashMap<Object, Object>());
					}
					break;
				}
			}
		}
		
		// in the case of no method memoized all handler methods are treated as memoized
		if(maps.size() == 0) {
			System.out.println("maps.size() == 0");
			
			for(String methodName : methodNames) {
				maps.put(methodName, new HashMap<Object, Object>());
			}
		}
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		
		HashMap<Object, Object> map = (HashMap<Object, Object>) maps.get(method.getName());
		if(map != null) {
			Object arg = method.getName().equalsIgnoreCase("getTransitionCost") ?
					new Pair(args[0], args[1]) : args[0];
				
			result = map.get(arg);
				
			if(result == null) {
				result = method.invoke(obj, args);
				map.put(arg, result);
			}
		} else {
			result = method.invoke(obj, args);				
		}
		
		return result;
	}
	
	class Pair {
		Object o1, o2;
		
		Pair(Object o1, Object o2) {
			this.o1 = o1;
			this.o2 = o2;
		}
		
		public boolean equals(Pair p) {
			return o1.equals(p.o1) && o2.equals(p.o2);
		}
		
		public int hashCode() {
			return 31 * (31 + o1.hashCode()) + o2.hashCode() ;
		}
	}

}
