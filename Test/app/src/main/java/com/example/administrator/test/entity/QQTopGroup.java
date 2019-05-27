package com.example.administrator.test.entity;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/27
 * PackageName com.example.administrator.test.entity
 */
public class QQTopGroup {
    private int groupId;
    private String groupName;
    private int type;
    private List<QQTopListInfo> qqTopListInfos;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<QQTopListInfo> getQqTopListInfos() {
        return qqTopListInfos;
    }

    public void setQqTopListInfos(List<QQTopListInfo> qqTopListInfos) {
        this.qqTopListInfos = qqTopListInfos;
    }
}
