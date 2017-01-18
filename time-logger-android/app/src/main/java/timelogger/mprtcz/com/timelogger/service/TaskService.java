package timelogger.mprtcz.com.timelogger.service;

import timelogger.mprtcz.com.timelogger.dao.CustomDao;
import timelogger.mprtcz.com.timelogger.model.Task;

/**
 * Created by Azet on 2017-01-18.
 */

public class TaskService {

    private CustomDao customDao;

    public void saveTask(Task task) {
        this.customDao.saveTask(task);
    }
}
