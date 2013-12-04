package test;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import RuleInductionFromCoverings.Execute;
import RuleInductionFromCoverings.Relation;


public class testingThings {
	
	@Test
	public void testThisHashCodeThingy() {
		String what = "this";
		String elseIsThere = "that";
		
		String greatStuff = "this";
		String rightHere = "that";
		
		int awesome = (what+elseIsThere).hashCode();
		int greatest = (greatStuff+rightHere).hashCode();
		
		System.out.println("Awesome = " + awesome + " greatest = " + greatest);
		
		assertTrue(awesome == greatest);
	}
	
	@Test
	public void testPowerSet() {
		Execute doItNow = new Execute(new Relation());
		Set<Integer> mySet = new HashSet<Integer>();
		 mySet.add(1);
		 mySet.add(2);
		 mySet.add(3);
		 for (Set<Integer> s : doItNow.powerSet(mySet)) {
		     System.out.println(s);
		 }
		
	}

}
