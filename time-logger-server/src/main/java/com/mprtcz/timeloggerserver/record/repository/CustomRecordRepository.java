package com.mprtcz.timeloggerserver.record.repository;

import com.mprtcz.timeloggerserver.record.model.Record;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Repository
@Transactional
public class CustomRecordRepository {

    private
    SessionFactory sessionFactory;

    public List<Record> getRecordsByTaskId(Long id) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Record.class)
                .add(Restrictions.eq("task.serverId", id)); //HQL uses class name instead of table name, and property names instead of column name.
        return criteria.list();
    }

    public Record getRecordBySyncDate(Date syncDate) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Record.class)
                .add(Restrictions.eq("synchronizationDate", syncDate));
        return (Record) criteria.uniqueResult();
    }

    public Record getRecordByUuId(String uuId) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Record.class)
                .add(Restrictions.eq("uuId", uuId));
        return (Record) criteria.uniqueResult();
    }

    @Autowired
    public CustomRecordRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
