package com.mprtcz.timeloggerweb.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by mprtcz on 2017-02-15.
 */
public class TaskArray<E extends JavaScriptObject> extends JavaScriptObject {
    protected TaskArray() {}
    public final native int length() /*-{ return this.length; }-*/;
    public final native E get(int i) /*-{ return this[i];     }-*/;
}
