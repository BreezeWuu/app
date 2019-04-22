package com.zotye.wms.data.api.model;

import java.io.Serializable;

/**
 * Created by hechuangju on 2019/04/23
 */
public class VehicleReceiptDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5675105896724188794L;

    private String id ;

    private String no;

    private String createTime;
    private String group;
    private String line;
    private String factory;
    private String user;
    private Integer flag;



    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public String getFactory() {
        return factory;
    }
    public void setFactory(String factory) {
        this.factory = factory;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

}