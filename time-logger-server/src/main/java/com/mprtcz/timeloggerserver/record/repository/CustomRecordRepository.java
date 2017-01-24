package com.mprtcz.timeloggerserver.record.repository;

import com.mprtcz.timeloggerserver.record.model.Record;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Repository
public class CustomRecordRepository {

    private
    SessionFactory sessionFactory;

    public List<Record> getRecordsByTaskId(Long id) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Record.class)
                .add(Restrictions.eq("TASK_ID", id));
        return criteria.list();
    }

    @Autowired
    public CustomRecordRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
