package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import RuleInductionFromCoverings.Execute;

public class understandingDataMining {
	
	@Test
	public void testPowerSetGeneration() {
		Set<Integer> originSet = new HashSet<Integer>();
		originSet.add(1);
		originSet.add(2);
		originSet.add(3);
		
		Set<Set<Integer>> powerSet = Execute.powerSet(originSet);

		assertTrue(powerSet.contains(new HashSet<Integer>() {{ add(1); }}));
		assertTrue(powerSet.contains(new HashSet<Integer>() {{ add(2); }}));
		assertTrue(powerSet.contains(new HashSet<Integer>() {{ add(3); }}));
		assertTrue(powerSet.contains(new HashSet<Integer>() {{ add(1); add(2); }}));
		assertTrue(powerSet.contains(new HashSet<Integer>() {{ add(1); add(3); }}));
		assertTrue(powerSet.contains(new HashSet<Integer>() {{ add(2); add(3); }}));
		assertTrue(powerSet.contains(new HashSet<Integer>() {{ add(1); add(2); add(3); }}));
	}
	
	@Test
	public void testIsPartitionOf() {
		Set<List<Integer>> originalSet = new HashSet<List<Integer>>() {{ 
			add(Arrays.asList(1, 2, 3, 4));
			add(Arrays.asList(5, 6));
		}};
		Set<List<Integer>> partitionA = new HashSet<List<Integer>>() {{ 
			add(Arrays.asList(1, 2, 3, 4, 5, 6));
		}};
		Set<List<Integer>> partitionB = new HashSet<List<Integer>>() {{ 
			add(Arrays.asList(1, 2));
			add(Arrays.asList(5));
		}};
		
		assertFalse(Execute.isPartition(originalSet, partitionA));
		assertTrue(Execute.isPartition(originalSet, partitionB));
		assertFalse(Execute.isPartition(partitionB, originalSet));
		assertTrue(Execute.isPartition(partitionA, partitionB));
	}
	
	@Test
	public void testIsMinimal() {
		
	}
	
	@Test
	public void testIsCovering() {
		
	}
	
}
