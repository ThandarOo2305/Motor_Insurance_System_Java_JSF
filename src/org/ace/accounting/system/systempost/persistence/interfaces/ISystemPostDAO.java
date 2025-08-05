package org.ace.accounting.system.systempost.persistence.interfaces;

import java.util.Date;
import java.util.List;

import org.ace.accounting.system.systempost.SystemPost;
import org.ace.java.component.persistence.exception.DAOException;

public interface ISystemPostDAO {

	public List<SystemPost> findAll() throws DAOException;
	public SystemPost findbyPostingName(String postingName) throws DAOException;
	public void updatePostingDateByName(Date postingDate, String postingName)throws DAOException;
}
