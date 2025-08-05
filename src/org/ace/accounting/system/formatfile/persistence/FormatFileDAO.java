package org.ace.accounting.system.formatfile.persistence;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ace.accounting.dto.ReportFormatDto;
import org.ace.accounting.report.ReportType;
import org.ace.accounting.report.reportStatement.persistence.ReportStatementDAO;
import org.ace.accounting.system.formatfile.ColType;
import org.ace.accounting.system.formatfile.FormatFile;
import org.ace.accounting.system.formatfile.persistence.interfaces.IFormatFileDAO;
import org.ace.java.component.persistence.BasicDAO;
import org.ace.java.component.persistence.exception.DAOException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("FormatFileDAO")
public class FormatFileDAO extends BasicDAO implements IFormatFileDAO {

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public List<FormatFile> findAll() throws DAOException {
        List<FormatFile> result = null;
        try {
            Query q = em.createNamedQuery("FormatFile.findAll");
            result = q.getResultList();
            em.flush();
        } catch (PersistenceException pe) {
            throw translate("Failed to find all of FormatFile", pe);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ReportFormatDto> findByReportType(ReportType reportType) throws DAOException {
        List<ReportFormatDto> result = null;
        try {
            Query q = em.createQuery(" SELECT DISTINCT NEW " + ReportFormatDto.class.getName()
                    + "(x.formatType,x.formatName) FROM FormatFile x WHERE x.reportType = :reportType ORDER BY x.formatType ASC ");
            q.setParameter("reportType", reportType);
            result = q.getResultList();
            em.flush();
        } catch (PersistenceException pe) {
            throw translate("Failed to find FormatFile by Report Type", pe);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public List<FormatFile> findByTypeAndName(String formatType, String formatName) throws DAOException {
        List<FormatFile> result = null;
        try {
            Query q = em.createNamedQuery("FormatFile.findByType&Name");
            q.setParameter("formatType", formatType);
            q.setParameter("formatName", formatName);
            result = q.getResultList();
            em.flush();
        } catch (PersistenceException pe) {
            throw translate("Failed to find FormatFile by type and name", pe);
        }
        return result;
    }

    /**
     * @see ReportStatementDAO#formulaProcess
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public List<FormatFile> findLR(String formatType, ReportType reportType) throws DAOException {
        List<FormatFile> result = null;
        /*
         * CONSULT : STATUS of FORMATFILE is currently a BOOLEAN CAN'T QUERY FOR
         * LR
         * 
         * UPDATE : THERE IS NO MORE FORMATFILE WITH STATUS 'LR' IN
         * ggip-25-04-2016 database backup
         */
        try {
            Query q = em.createQuery(" SELECT x FROM FormatFile x WHERE x.reportType=:reportType AND x.formatType=:formatType " + "AND x.status='LR' ORDER BY x.lNo ASC ");
            q.setParameter("formatType", formatType);
            q.setParameter("reportType", reportType);
            result = q.getResultList();
            em.flush();
        } catch (PersistenceException pe) {
            throw translate("Failed to find LR FormatFile", pe);
        }
        return result;
    }

    /**
     * @see ReportStatementDAO#formulaProcess
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<FormatFile> findOther(ColType colType, String formatType, ReportType reportType) throws DAOException {
        List<FormatFile> result = null;
        try {
            Query q = em.createQuery(" SELECT x FROM FormatFile x WHERE x.reportType=:reportType AND x.formatType=:formatType "
                    + "AND x.status = true AND x.colType = :colType ORDER BY x.lNo ASC ");
            q.setParameter("formatType", formatType);
            q.setParameter("reportType", reportType);
            q.setParameter("colType", colType);
            result = q.getResultList();
            em.flush();
        } catch (PersistenceException pe) {
            throw translate("Failed to find other FormatFile", pe);
        }
        return result;
    }
}
