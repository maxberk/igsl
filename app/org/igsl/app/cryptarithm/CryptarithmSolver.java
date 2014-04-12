package org.igsl.app.cryptarithm;

/**
 * Implicit Graph Search Library(C), 2009, 2014 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;

import org.igsl.functor.FiniteSetNodeGenerator;
import org.igsl.functor.BackwardPathIterator;

/**
 * Cryptarithm Problem is a problem of converting letters to digits in a sum-only
 * expressions.	
 */
public class CryptarithmSolver implements FiniteSetNodeGenerator<Character>{
	
	private ArrayList<String> sValues = new ArrayList<String>();
	private StringBuffer vars;
	private StringBuffer result;
	
	/**
	 * Constructor to provide an initial String parsing
	 * @param expression an expression of sum of components written as a String
	 */
	public CryptarithmSolver(String expression) {
		String[] values = expression.split("[+=]");
		int maxLen = 0;
		
		for(int k = 0; k < values.length-1; ++k) {
			StringBuffer sb = new StringBuffer(values[k]);
			sb.reverse();
			
			if(sb.length() > maxLen) {
				maxLen = sb.length();
			}
			
			sValues.add(sb.toString());
		}
		
		result = new StringBuffer(values[values.length-1]);
		result.reverse();
		
		Collections.sort(sValues, new Comparator<String>() {
			public int compare(String s1, String s2) {
				if(s1.length() > s2.length()) {
					return -1;
				} else if(s1.length() < s2.length()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		for(int i = 0; i < maxLen-1; ++i) {
			Iterator<String> si = sValues.iterator();
			
			while(si.hasNext()) {
				String s = si.next();
				
				if(s.length() > i-1) {
					String letter = s.substring(i, i+1);
					
					int ivar = vars.indexOf(letter);
					int ires = result.indexOf(letter);
					
					if(ivar < 0 && (ires < 0 || i < ires)) {
						vars.append(letter);
					}
				}
			}
		}
	}
	
	public Character[] getAllValues() {
		char[] letters = vars.toString().toCharArray();
		
		Character[] result = new Character[letters.length];
		for(int i = 0; i < letters.length; ++i) {
			result[i] = new Character(letters[i]);
		}
		
		return result;
	}
	
	public int getMaxDepth() {
		return vars.length();
	}
	
	public boolean isValidTransition(Character letter, BackwardPathIterator<Character> iterator) {
		return true;
	}
	
}
