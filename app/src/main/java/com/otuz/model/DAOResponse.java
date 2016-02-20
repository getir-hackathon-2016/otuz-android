package com.otuz.model;

import java.util.ArrayList;

/**
 * A POJO for holding an Error Model and a parsed object which is the desired response of server.
 * Created by AhmetOguzhanBasar on 20.02.2016.
 */
public class DAOResponse {

    private ErrorModel error;
    private Object object;
    private ArrayList<Object> objectArray;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

    public ArrayList<Object> getObjectArray() {
        return objectArray;
    }

    public void setObjectArray(ArrayList<Object> objectArray) {
        this.objectArray = objectArray;
    }
}
