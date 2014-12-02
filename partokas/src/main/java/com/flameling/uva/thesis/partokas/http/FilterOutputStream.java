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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public final class FilterOutputStream extends ServletOutputStream {
	
	private DataOutputStream stream = null;

	public FilterOutputStream(OutputStream os) {
		stream = new DataOutputStream(os);
	}

	@Override
	public void print(String arg0) throws IOException {
		int len = (arg0 == null ? -1 : arg0.length());

		for (int i = 0; i < len; i++) {
			char c = arg0.charAt(i);
			write(c);
		}
	}

	@Override
	public void print(boolean arg0) throws IOException {
		print(String.valueOf(arg0));
	}

	@Override
	public void print(char arg0) throws IOException {
		print(String.valueOf(arg0));
	}

	@Override
	public void print(int arg0) throws IOException {
		print(String.valueOf(arg0));
	}

	@Override
	public void print(long arg0) throws IOException {
		print(String.valueOf(arg0));
	}

	@Override
	public void print(float arg0) throws IOException {
		print(String.valueOf(arg0));
	}

	@Override
	public void print(double arg0) throws IOException {
		print(String.valueOf(arg0));
	}

	@Override
	public void println() throws IOException {
		print("\r\n");
	}

	@Override
	public void println(String arg0) throws IOException {
		print(arg0);
		println();
	}

	@Override
	public void println(boolean arg0) throws IOException {
		println(String.valueOf(arg0));
	}

	@Override
	public void println(char arg0) throws IOException {
		println(String.valueOf(arg0));
	}

	@Override
	public void println(int arg0) throws IOException {
		println(String.valueOf(arg0));
	}

	@Override
	public void println(long arg0) throws IOException {
		println(String.valueOf(arg0));
	}

	@Override
	public void println(float arg0) throws IOException {
		println(String.valueOf(arg0));
	}

	@Override
	public void println(double arg0) throws IOException {
		println(String.valueOf(arg0));
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}

	@Override
	public void flush() throws IOException {
		stream.flush();
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		stream.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		stream.write(b);
	}

	@Override
	public void write(int b) throws IOException {
		stream.write(b);
	}
}
