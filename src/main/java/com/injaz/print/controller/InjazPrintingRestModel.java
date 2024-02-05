package com.injaz.print.controller;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

public class InjazPrintingRestModel implements Serializable {

	
	public InjazPrintingRestModel() {
		// TODO Auto-generated constructor stub
	}
	
	private String name;
	
	private String id;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private MultipartFile file;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}
