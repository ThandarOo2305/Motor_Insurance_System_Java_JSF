package org.ace.java.component.idgen.service;

import java.lang.reflect.Field;
import java.util.Date;

import org.ace.accounting.common.BasicEntity;
import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.java.component.FormatID;
import org.ace.java.component.idgen.service.interfaces.ICustomIDGenerator;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class IDInterceptor extends DescriptorEventAdapter {

	private static ICustomIDGenerator customIDGenerator;
	private static IUserProcessService userProcessService;

	@Autowired(required = true)
	@Qualifier("CustomIDGenerator")
	public void setcustomIDGenerator(ICustomIDGenerator generator) {
		customIDGenerator = generator;
	}

	@SuppressWarnings("rawtypes")
	private String getPrefix(Class cla) {
		return customIDGenerator.getPrefix(cla);
	}

	@Autowired(required = true)
	@Qualifier("UserProcessService")
	public void setUserProcessService(IUserProcessService userProcessService) {
		IDInterceptor.userProcessService = userProcessService;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void preInsert(DescriptorEvent event) {
		try {
			Object obj = event.getObject();
			Class cla = obj.getClass();

			Field field = cla.getDeclaredField("id");
			field.setAccessible(true);
			String id = (String) field.get(obj);
			id = FormatID.formatId(id, getPrefix(cla), 10);
			field.set(obj, id);
			Field coCreateAndUpateField = cla.getDeclaredField("basicEntity");
			coCreateAndUpateField.setAccessible(true);

			BasicEntity basicEntity = (BasicEntity) coCreateAndUpateField.get(obj);
			if (basicEntity == null) {
				basicEntity = new BasicEntity();
				basicEntity.setCreatedUserId(userProcessService.getLoginUser().getId());
				// basicEntity.setCreatedUserId("ISSYS001000000000114122015");
				if(null == basicEntity.getCreatedDate()) {
					basicEntity.setCreatedDate(new Date());
				}
				
			}

			coCreateAndUpateField.set(obj, basicEntity);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void preUpdateWithChanges(DescriptorEvent event) {
		try {

			Object obj = event.getObject();
			Class cla = obj.getClass();
			Field coCreateAndUpateField = cla.getDeclaredField("basicEntity");
			coCreateAndUpateField.setAccessible(true);

			BasicEntity basicEntity = (BasicEntity) coCreateAndUpateField.get(obj);
			basicEntity.setUpdatedUserId(userProcessService.getLoginUser().getId());
			basicEntity.setUpdatedDate(new Date());
			coCreateAndUpateField.set(obj, basicEntity);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
