/**
 * OWASP CSRFGuard
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * Copyright (c) 2007 - The OWASP Foundation
 * 
 * The CSRFGuard is published by OWASP under the LGPL. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Eric Sheridan <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created 2007
 */

package com.flameling.uva.thesis.partokas.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@SuppressWarnings("restriction")
public final class MutableHttpResponse extends HttpServletResponseWrapper {
	
	private ByteArrayOutputStream output = null;

	private FilterOutputStream stream = null;

	private PrintWriter writer = null;

	public MutableHttpResponse(HttpServletResponse response) {
		super(response);

		reset();
	}

	public byte[] getContent() throws IOException {
		flushBuffer();

		return output.toByteArray();
	}

	public void setContent(byte[] content) throws IOException {
		reset();

		stream.write(content);
	}

	public void setContent(String s) throws IOException {
		setContent(s.getBytes());
	}

	@Override
	public void flushBuffer() throws IOException {
		writer.flush();
		stream.flush();
		output.flush();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return stream;
	}

	@Override
	public ServletResponse getResponse() {
		// TODO Auto-generated method stub
		return super.getResponse();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	@Override
	public boolean isCommitted() {
		return output.size() > 0;
	}

	@Override
	public void reset() {
		this.output = new ByteArrayOutputStream();
		this.stream = new FilterOutputStream(output);
		this.writer = new PrintWriter(stream);
	}

	@Override
	public void resetBuffer() {
		reset();
	}

	public void writeContent() throws IOException {
		byte[] content = getContent();
		ServletResponse response = getResponse();
		OutputStream os = response.getOutputStream();

		response.setContentLength(content.length);
		os.write(content);
		os.close();
	}
}
