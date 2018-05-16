package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/16.
 */

public class ResponseWorkerParam {

    private int require_id;
    private int worker_id;
    private int operation;

    public ResponseWorkerParam() {
    }

    public ResponseWorkerParam(int require_id, int worker_id, int operation) {
        this.require_id = require_id;
        this.worker_id = worker_id;
        this.operation = operation;
    }

    public int getRequire_id() {
        return require_id;
    }

    public void setRequire_id(int require_id) {
        this.require_id = require_id;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }
}
