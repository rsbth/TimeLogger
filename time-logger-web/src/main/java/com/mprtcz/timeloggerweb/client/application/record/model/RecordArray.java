package com.mprtcz.timeloggerweb.client.application.record.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class RecordArray <E extends JavaScriptObject> extends JavaScriptObject {
    protected RecordArray() {}
    public final native int length() /*-{ return this.length; }-*/;
    public final native E get(int i) /*-{ return this[i];     }-*/;
}
