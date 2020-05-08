package com.chanfinecloud.cflforemployee.entity;

import java.io.Serializable;

/**
 * Created by Loong on 2020/4/9.
 * Version: 1.0
 * Describe:
 */
public class WorkflowViewTagEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String formType;
    private String formKey;
    private WorkflowViewEntity workflowView;
    private WorkflowFormContentEntity workflowFormContent;

    public WorkflowViewTagEntity(String formType, String formKey, WorkflowViewEntity workflowView,WorkflowFormContentEntity workflowFormContent) {
        this.formType = formType;
        this.formKey = formKey;
        this.workflowView = workflowView;
        this.workflowFormContent = workflowFormContent;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public WorkflowViewEntity getWorkflowView() {
        return workflowView;
    }

    public void setWorkflowView(WorkflowViewEntity workflowView) {
        this.workflowView = workflowView;
    }

    public WorkflowFormContentEntity getWorkflowFormContent() {
        return workflowFormContent;
    }

    public void setWorkflowFormContent(WorkflowFormContentEntity workflowFormContent) {
        this.workflowFormContent = workflowFormContent;
    }
}
