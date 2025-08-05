package org.ace.java.component.idgen.service.interfaces;

public interface IDConfigLoader {
	public String getFormat(String className);
	public String getBranchCode();
	public String getBranchId();
	public boolean isCentralizedSystem();
}
