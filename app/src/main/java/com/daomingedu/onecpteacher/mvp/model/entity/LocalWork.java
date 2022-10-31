package com.daomingedu.onecpteacher.mvp.model.entity;

import java.io.Serializable;

/**
 * 本地作品
 * Created by xjh on 2016/8/17.
 */
public class LocalWork implements Serializable {

    public static final int VIDEO = 1;//视频类型
    public static final int PINAO = 2;//钢琴类型
    public static final int KSONG = 3;//视唱
    private String id;//id
    private int type;//类型1视频2录音
    private String name;//名字
    private String path;//路径
    private String shareId;//社交分享id
    private Long createtime;//创建时间
    private String scoreId;//乐谱id
    private int totalScore;//分数
    public boolean checked; //是否选中
    private String scoreName;//乐谱名字


    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }
}
