package com.apgroup.pms.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 원료 entity
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class RawMaterial {
	
	private String id;
	
	private int remains;
	
	private int stock;

}
