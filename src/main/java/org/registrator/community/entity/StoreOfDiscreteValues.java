package org.registrator.community.entity;


import javax.persistence.*;

@Entity
@Table(name = "store_of_discrete_values")
public class StoreOfDiscreteValues {
	
	
	@Id
	@Column(name = "store_of_discrete_values_id")
	@GeneratedValue
	private Integer StoreOfDiscreteValuesId;

	@ManyToOne
	@JoinColumn(name = "resource_id", nullable = false)
	private Resource resource;
	
	
	@Column(name = "value", nullable = false)
	private Double value;
	
	@Column(name = "unit_name", nullable = false)
	private String unitName ;
	
	@ManyToOne
	@JoinColumn(name = "discrete_values_id", nullable = false)
	private DiscreteValue discreteValue;	

	public StoreOfDiscreteValues() {
		
	}

	public Integer getStoreOfDiscreteValuesId() {
		return StoreOfDiscreteValuesId;
	}

	public void setStoreOfDiscreteValuesId(Integer storeOfDiscreteValuesId) {
		StoreOfDiscreteValuesId = storeOfDiscreteValuesId;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public DiscreteValue getDiscreteValue() {
		return discreteValue;
	}

	public void setDiscreteValue(DiscreteValue discreteValue) {
		this.discreteValue = discreteValue;
	}

}