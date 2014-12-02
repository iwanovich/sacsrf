package com.flameling.uva.thesis.partokas;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flameling.uva.thesis.partokas.http.header.contenttype.MediaType;

public class HtmlTokenInjectorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMediaType() {
		String contentType = "image/png;charset=UTF-8";
		HtmlTokenInjector hti = new HtmlTokenInjector();
		MediaType actual = hti.getMediaType(null, contentType);
		MediaType expected = MediaType.PNG;
		assertEquals(expected, actual);
	}

}
