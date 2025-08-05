package org.ace.java.component.service.interfaces;

public interface IDataRepService<T> {
	
	public void insert(Object object);
	
	public T update(T param);
	
	public void delete(Object object);
	
	public T findById(Class<T> paramClass,Object paramObject);
}
