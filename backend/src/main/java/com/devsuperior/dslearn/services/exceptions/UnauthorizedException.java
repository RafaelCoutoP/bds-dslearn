package com.devsuperior.dslearn.services.exceptions;

public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	//vai retornar o 401 de não autorizado.
	public UnauthorizedException (String msg) {
		super(msg);
	}
}
