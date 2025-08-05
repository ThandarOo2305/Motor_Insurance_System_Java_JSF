package org.ace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class test {
	
	@Id
	@GeneratedValue
	private String id;

	private String Ccoaid;
	
	public test() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCcoaid() {
		return Ccoaid;
	}

	public void setCcoaid(String ccoaid) {
		Ccoaid = ccoaid;
	}

}
