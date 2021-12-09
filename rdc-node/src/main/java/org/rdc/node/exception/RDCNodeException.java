package org.rdc.node.exception;

public class RDCNodeException extends Exception{
	public RDCNodeException(String message) {
		super(message);
	}
	public RDCNodeException(String message, Throwable e){
		super(message,e);
	}
}
