package org.registrator.community.dto;

import java.util.List;

public class ResourceDiscreteDTO {
	private DiscreteParameterDTO discreteParameterDTO;
	private List<Double> values;

	public ResourceDiscreteDTO() {
		
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public DiscreteParameterDTO getDiscreteParameterDTO() {
		return discreteParameterDTO;
	}

	public void setDiscreteParameterDTO(DiscreteParameterDTO discreteParameterDTO) {
		this.discreteParameterDTO = discreteParameterDTO;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(discreteParameterDTO.toString());
		for (int i = 0, valuesSize = values.size(); i < valuesSize; i++) {
			Double value = values.get(i);
			result.append("Значення " + i + ": " + value + "\n");
		}
		return result.toString();
	}
		
	
	
}
