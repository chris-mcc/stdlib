package com.mediasmiths.std.net;

import java.io.*;

public class NoInterfaceException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public NoInterfaceException() {
		super();
	}


	public NoInterfaceException(String msg) {
		super(msg);
	}


	public NoInterfaceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
