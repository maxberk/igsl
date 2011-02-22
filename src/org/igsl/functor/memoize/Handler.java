/**
 * Implicit Graph Search Library(C), 2009, 2010, 2011 
 */

package org.igsl.functor.memoize;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

class Handler implements InvocationHandler {
	
	Object obj;
	HashMap maps; // a map with a method name and a result hash map with "argument-result" pairs
	
	Handler(Object obj, Class theInterface) {
		this.obj = obj;
		this.maps = new HashMap<String, HashMap<Object, Object>>();
		
		for(Method intMethod : theInterface.getMethods()) {
			for(Method objMethod : obj.getClass().getMethods()) {
				if(objMethod.getAnnotation(Memoize.class) == null) {
					continue;
				}
				
				if(intMethod.getName().equals(objMethod.getName())) {
					Class<?> intReturnClass = intMethod.getReturnType();
					Class<?> objReturnClass = objMethod.getReturnType();
					
					if(intReturnClass != null && objReturnClass != null &&
						intReturnClass.isAssignableFrom(objReturnClass)) {

						Class<?>[] intParameterClasses = intMethod.getParameterTypes();
						Class<?>[] objParameterClasses = objMethod.getParameterTypes();
						if(intParameterClasses.length == objParameterClasses.length) {
							boolean mismatched = false;
							for(int i = 0; i < intParameterClasses.length; ++i) {
								if(!intParameterClasses[i].isAssignableFrom(objParameterClasses[i])) {
									mismatched = true;
									break;
								}
							}
							
							if(!mismatched) {
								maps.put(objMethod.getName(), new HashMap<Object, Object>());
							}
						}
					}
				}
			}
		}
		
		// in the case of no method memoized all interface methods are treated as memoized
		if(maps.size() == 0) {
			for(Method intMethod : theInterface.getMethods()) {
				maps.put(intMethod.getName(), new HashMap<Object, Object>());
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
