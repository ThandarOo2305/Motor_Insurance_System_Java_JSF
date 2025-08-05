package org.ace.accounting.common;

import java.util.Properties;

import javax.annotation.Resource;

import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Service;

@Service("PropertiesManager")
public class PropertiesManager {

	@Resource(name = "LANGUAGE_EN")
	private Properties properties;

	public String getProperties(String propertiesName) {
		String genName = null;
		try {
			genName = "/" + (String) properties.getProperty(propertiesName);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return genName;
	}

}
