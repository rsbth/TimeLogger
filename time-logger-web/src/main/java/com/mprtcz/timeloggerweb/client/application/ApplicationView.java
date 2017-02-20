/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.mprtcz.timeloggerweb.client.application;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.mprtcz.timeloggerweb.client.http.AsyncHttpClient;
import com.mprtcz.timeloggerweb.client.uielements.TasksListUiCreator;
import gwt.material.design.addins.client.cutout.MaterialCutOut;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialRow;

import javax.inject.Inject;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @UiField
    MaterialRow tasksMaterialRow;
    @UiField
    MaterialCollapsible taskCollapsible;
    @UiField
    MaterialModal modalFixed;
    @UiField
    MaterialCutOut cutout;


    @Inject
    ApplicationView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        populateMaterialList();
    }

    private void populateMaterialList() {
        AsyncHttpClient httpClientAsync = new AsyncHttpClient();
        TasksListUiCreator tasksListUiCreator = new TasksListUiCreator(cutout, tasksMaterialRow, taskCollapsible);
        httpClientAsync.getTasksAsync(tasksListUiCreator);
    }
}