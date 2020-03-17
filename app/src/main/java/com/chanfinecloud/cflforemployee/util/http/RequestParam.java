package com.chanfinecloud.cflforemployee.util.http;

import org.xutils.common.Callback;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 请求参数封装
 */
public class RequestParam<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public RequestParam() {
        this.authorization=true;
    }

    private String url;
    private HttpMethod method;
    private Map<String, String> getRequestMap;
    private Map<String, Object> postRequestMap;
    private Object postJsonRequest;
    private boolean authorization;
    private String filepath;
    private Callback.CommonCallback<T> callback;
    private Callback.ProgressCallback<T> progressCallback;

    public boolean isAuthorization() {
        return authorization;
    }

    public void setAuthorization(boolean authorization) {
        this.authorization = authorization;
    }

    public Callback.ProgressCallback<T> getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(Callback.ProgressCallback<T> progressCallback) {
        this.progressCallback = progressCallback;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Object getPostJsonRequest() {
        return postJsonRequest;
    }

    public void setPostJsonRequest(Object postJsonRequest) {
        this.postJsonRequest = postJsonRequest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getGetRequestMap() {
        return getRequestMap;
    }

    public void setGetRequestMap(Map<String, String> getRequestMap) {
        this.getRequestMap = getRequestMap;
    }

    public Map<String, Object> getPostRequestMap() {
        return postRequestMap;
    }

    public void setPostRequestMap(Map<String, Object> postRequestMap) {
        this.postRequestMap = postRequestMap;
    }

    public Callback.CommonCallback<T> getCallback() {
        return callback;
    }

    public void setCallback(Callback.CommonCallback<T> callback) {
        this.callback = callback;
    }
}
