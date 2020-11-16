package com.devsuperior.dslearn.services.exceptions;

public class ForbiddenException extends RuntimeException {
	private static final long serialVersionUID = 1L; 
	
	//vai retorna o erro 403 de permissão
	public ForbiddenException (String msg) {
		super(msg);
	}
}
