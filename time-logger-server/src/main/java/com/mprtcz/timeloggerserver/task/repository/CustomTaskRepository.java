package com.mprtcz.timeloggerserver.task.repository;

import com.mprtcz.timeloggerserver.task.model.Task;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by mprtcz on 2017-01-28.
 */
@Repository
@Transactional
public class CustomTaskRepository {

    private
    SessionFactory sessionFactory;

    public Task getTaskByUuid(String uuId) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Task.class)
                .add(Restrictions.eq("uuID", uuId)); //HQL uses class name instead of table name, and property names instead of column name.
        return (Task) criteria.uniqueResult();
    }

    @Autowired
    public CustomTaskRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
