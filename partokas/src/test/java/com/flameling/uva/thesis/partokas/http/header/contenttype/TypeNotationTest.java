package com.flameling.uva.thesis.partokas.http.header.contenttype;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TypeNotationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEqualsObject() {
		TypeNotation tn1 = new TypeNotation(GroupType.IMAGE,SubType.PNG);
		TypeNotation tn2 = new TypeNotation(GroupType.IMAGE,SubType.PNG);
		assertEquals(tn1, tn2);
		assertTrue(tn1.equals(tn2));
		assertTrue(tn2.equals(tn1));
		assertTrue(tn1.equals(tn1));
	}

}
