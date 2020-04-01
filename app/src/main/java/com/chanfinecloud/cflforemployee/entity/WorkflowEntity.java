package com.chanfinecloud.cflforemployee.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 流程实体
 */
public class WorkflowEntity implements Serializable {
    private static final long serialVersionUID = 1L;



    /**
     * id : 1712a0677eb4634e61a122748118ed43
     * processId : null
     * problemDesc : 春天是一个特别有意思的季节，没有寒冬的冷冽和萧条，也没有炎夏的火热和激情，但它带给人更多的美好和希望，如果问春天是什么颜色的，那春天应该是属于丰富多彩的，而春天的穿搭和这个季节有着必然的联系，在众多时尚单品中，卫衣有着更多的可能性。
     * code : 202003300005
     * projectId : ec93bb06f5be4c1f19522ca78180e2i9
     * projectName : 长房时代城
     * problemResourceKey : 1712a050b661c65ec656d24487cb86f3
     * liveResourceKey : 1712a33dcfccd4d555da4e74924ab417
     * typeId : 4
     * typeName : 综合维修
     * statusId : 21
     * statusName : 回访关闭
     * householdId : 1708ed9bd5442a637fbd1c64298858fd
     * createTime : 2020-03-30 13:59:59
     * householdName : 刘子帅
     * householdMobile : 15531270511
     * address : 8栋三单元1003
     * assigneeId :
     * nodeName :
     * assignTime :
     */

    private String id;
    private Object processId;
    private String problemDesc;
    private String code;
    private String projectId;
    private String projectName;
    private String problemResourceKey;
    private String liveResourceKey;
    private int typeId;
    private String typeName;
    private int statusId;
    private String statusName;
    private String householdId;
    private String createTime;
    private String householdName;
    private String householdMobile;
    private String address;
    private String assigneeId;
    private String nodeName;
    private String assignTime;
    private List<ResourceEntity> problemResourceValue;
    private List<ResourceEntity> liveResourceValue;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getProcessId() {
        return processId;
    }

    public void setProcessId(Object processId) {
        this.processId = processId;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProblemResourceKey() {
        return problemResourceKey;
    }

    public void setProblemResourceKey(String problemResourceKey) {
        this.problemResourceKey = problemResourceKey;
    }

    public String getLiveResourceKey() {
        return liveResourceKey;
    }

    public void setLiveResourceKey(String liveResourceKey) {
        this.liveResourceKey = liveResourceKey;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public String getHouseholdMobile() {
        return householdMobile;
    }

    public void setHouseholdMobile(String householdMobile) {
        this.householdMobile = householdMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(String assignTime) {
        this.assignTime = assignTime;
    }

    public List<ResourceEntity> getProblemResourceValue() {
        return problemResourceValue;
    }

    public void setProblemResourceValue(List<ResourceEntity> problemResourceValue) {
        this.problemResourceValue = problemResourceValue;
    }

    public List<ResourceEntity> getLiveResourceValue() {
        return liveResourceValue;
    }

    public void setLiveResourceValue(List<ResourceEntity> liveResourceValue) {
        this.liveResourceValue = liveResourceValue;
    }
}
