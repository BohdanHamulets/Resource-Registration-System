package org.registrator.community.dto;

public class DiscreteParameterDTO {
	
	private String description;
	private String unitName;
	
	public DiscreteParameterDTO(String description, String unitName) {
		super();
		this.description = description;
		this.unitName = unitName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	

}