package com.zotye.wms.data.api.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by hechuangju on 2019/04/22
 */
public class StorageLocationDto implements Serializable {

    private static final long serialVersionUID = -4259876416274811961L;

    private String id;

    private String pid;

    private String code;

    private String name;

    private String wmsType;

    private boolean open;

    private boolean checked;

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getPid() {
        return pid;
    }


    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getWmsType() {
        return wmsType;
    }

    public void setWmsType(String wmsType) {
        this.wmsType = wmsType;
    }

    public boolean isOpen() {
        return open;
    }


    public void setOpen(boolean open) {
        this.open = open;
    }


    public boolean isChecked() {
        return checked;
    }



    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
