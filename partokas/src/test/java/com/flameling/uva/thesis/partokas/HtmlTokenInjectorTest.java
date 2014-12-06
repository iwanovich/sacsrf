package com.flameling.uva.thesis.partokas;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flameling.uva.thesis.partokas.http.header.contenttype.MediaType;
import com.flameling.uva.thesis.partokas.http.header.contenttype.ContentType;

public class HtmlTokenInjectorTest {
	
	HtmlTokenInjector hti;
	String contentType;
	MediaType actual;

	@Before
	public void setUp() throws Exception {
		hti = new HtmlTokenInjector();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	
	private void testGetMediaType(String contentType, MediaType expected) {
		actual = ContentType.getMediaType(contentType);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPNGType(){
		testGetMediaType("image/png;charset=UTF-8", MediaType.PNG);
	}
	
	@Test
	public void testGetCSSType(){
		testGetMediaType("text/css;charset=UTF-8", MediaType.CSS);
	}
	
	@Test
	public void testGetHTMLType(){
		testGetMediaType("text/html;charset=UTF-8", MediaType.HTML);
	}
	
	@Test
	public void testGetJSType(){
		testGetMediaType("text/javascript;charset=UTF-8", MediaType.JS);
		testGetMediaType("application/javascript;charset=UTF-8", MediaType.JS);
	}
	
	@Test
	public void testSupport(){
		assertTrue(MediaType.supportsTypeNotation("image/png"));
		assertTrue(MediaType.supportsTypeNotation("image/jpeg"));
		assertTrue(MediaType.supportsTypeNotation("image/bmp"));
		
		assertFalse(MediaType.supportsTypeNotation("image/html"));
		assertFalse(MediaType.supportsTypeNotation("image/willem"));
		assertFalse(MediaType.supportsTypeNotation("willem/html"));
		assertFalse(MediaType.supportsTypeNotation("null/null"));
		assertFalse(MediaType.supportsTypeNotation("null"));
	}

}
