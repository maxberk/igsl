package org.igsl.functor.memoize;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

class Handler implements InvocationHandler {
	
	Object obj;
	String[] methodNames;
	HashMap[] maps = null; 
	
	Handler(Object obj, String[] methodNames) {
		this.obj = obj;
		this.methodNames = methodNames;
		
		maps = new HashMap[methodNames.length];
		for(int i = 0; i < methodNames.length; ++i) {
			maps[i] = new HashMap();
		}
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		Object result = null;
		boolean isFound = false;
		
		for(int i = 0; i < methodNames.length; ++i) {
			if(methodName.equals(methodNames[i])) {
				result = maps[i].get(args[0]);
				
				if(result == null) {
					result = method.invoke(obj, args);
					maps[i].put(args[0], result);
					//System.out.println("Filter: methodName = " + methodName);
				} else {
					//System.out.println("Found: methodName = " + methodName);
				}
				
				isFound = true;
				break;
			}
		}
		
		if(isFound == false) {
			result = method.invoke(obj, args);				
			//System.out.println("Invoked: methodName = " + method.getName());
		}
		
		return result;
	}

}
