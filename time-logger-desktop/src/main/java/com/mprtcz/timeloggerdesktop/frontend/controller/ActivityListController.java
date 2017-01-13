package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.DialogElementsConstructor;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class ActivityListController {

    private static final int LIST_VIEW_ROW_HEIGHT = 24; // inint 26/20/7
    private static final int LIST_VIEW_ROW_PADDING = 20;
    private static final int LIST_VIEW_OFFSET = 9;

    private JFXListView<Activity> activityNamesList;
    private ActivityService activityService;
    private ChangeListener exceptionListener;
    private ChangeListener changeListener;
    private ExecutorService executorService;

    public ActivityListController(JFXListView<Activity> activityNamesList, ActivityService activityService,
                                  ChangeListener exceptionListener, ChangeListener changeListener,
                                  ExecutorService executorService) {
        this.activityNamesList = activityNamesList;
        this.activityService = activityService;
        this.exceptionListener = exceptionListener;
        this.changeListener = changeListener;
        this.executorService = executorService;

        this.populateListView();
        this.setUpListViewListener();
        this.setListViewFactory();
    }

    private void setActivityListItems(List<Activity> activities) {
        this.activityNamesList.setItems(FXCollections.observableList(activities));
        this.activityNamesList.setExpanded(true);
        this.activityNamesList.depthProperty().set(1);
        this.activityNamesList.setPrefHeight
                ((activities.size() * (LIST_VIEW_ROW_HEIGHT + LIST_VIEW_ROW_PADDING)) + LIST_VIEW_OFFSET);
    }

    void setListViewFactory() {
        this.activityNamesList.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
            @Override
            public ListCell<Activity> call(ListView<Activity> param) {
                return new JFXListCell<Activity>() {
                    @Override
                    public void updateItem(Activity item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setGraphic(DialogElementsConstructor.createListViewCellLayout(item));
                        }
                    }
                };
            }
        });
    }

    void populateListView() {
        Task<List<Activity>> task = new Task<List<Activity>>() {
            @Override
            protected List<Activity> call() throws Exception {
                return ActivityListController.this.activityService.getActivities();
            }
        };
        task.setOnSucceeded(event -> this.setActivityListItems(task.getValue()));
        task.setOnFailed(event -> System.out.println(task.exceptionProperty().toString()));
        task.exceptionProperty().addListener(this.exceptionListener);
        this.executorService.execute(task);
    }

    void setUpListViewListener() {
        this.activityNamesList.getSelectionModel().selectedItemProperty().addListener(this.changeListener);
    }
}
