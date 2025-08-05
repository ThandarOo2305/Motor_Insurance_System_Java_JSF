package org.ace.java.component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

public class DDLGenerator {
	public static void main(String[] args) {
		try {
			Map<String, String> propertiesMap = new HashMap<String, String>();
			propertiesMap.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
			propertiesMap.put(PersistenceUnitProperties.DDL_GENERATION_MODE, "sql-script");
			propertiesMap.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE, "resources/create.sql");
			propertiesMap.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE, "resources/drop.sql");
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA", propertiesMap);
			emf.createEntityManager();
			System.out.println(emf.getProperties().get(PersistenceUnitProperties.JDBC_CONNECTOR));
			emf.close();

			BufferedReader br = new BufferedReader(new FileReader("resources/create.sql"));
			String strLine = "";
			StringBuilder fileContent = new StringBuilder();
			while ((strLine = br.readLine()) != null) {
				fileContent.append(strLine);
				fileContent.append(";\n");
			}
			
			BufferedWriter out = new BufferedWriter(new FileWriter("resources/create.sql"));
			out.write(fileContent.toString());
			br.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
