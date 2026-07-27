package com.zs.bpm.model.domain.dto;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {

    public String name;
    public int type;
    public int settype;
    public int examineMode;
    public int signPct;
    public int noHanderAction;
    public int ccSelfSelectFlag;
    public boolean isOther;
    public int priorityLevel;

    public List<JSONObject> nodeUserList = new ArrayList<>();
    public List<JSONObject> nodeRoleList = new ArrayList<>();
    public List<JSONObject> nodePostList = new ArrayList<>();
    public List<JSONObject> nodeDeptHeadList = new ArrayList<>();
    public List<JSONObject> conditionList = new ArrayList<>();
    public List<String> candidateParam = new ArrayList<>();

    // 新增：表单字段权限和按钮配置
    public JSONArray fieldPermission = new JSONArray();
    public JSONArray buttonSetting = new JSONArray();

    public Node childNode;
    public List<Node> conditionNodes;
}
