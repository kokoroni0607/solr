package com.creditstate.demo.entity.po;


import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 信息发布表Vo
 */
public class TWebInfoContentVo implements Serializable {


    /**
     * 更新者ID
     */
    protected String updateUserid;

    /**
     * 更新者用户名
     */
    protected String updateUsername;

    /**
     * 更新时间
     */
    protected Date updateTime;

    /**
     * 主键
     */
    @Field
    protected String id;

    /**
     * 标题
     */
    @Field
    protected String title;

    /**
     * 子标题
     */
    protected String subTitle;

    /**
     * 摘要
     */
    protected String summary;

    /**
     * 内容
     */

    protected String content;

    /**
     * 发布者id
     */
    protected String creatorId;

    /**
     * 缩略图(大) 链接地址
     */
    protected String thumbnailBigLink;

    /**
     * 缩略图(小) 链接地址
     */
    protected String thumbnailSmallLink;

    /**
     * 信息发布时间
     */
    @Field
    protected Date publishTime;


    /**
     * 信息发布时间(String格式)
     */

    protected String cPublishTime;

    /**
     * 是否置顶  0：非置顶  1：置顶
     */
    protected String isTop;

    /**
     * 发布状态：0 ：未发布  1： 发布
     */
    protected String status;

    /**
     * 顺序
     */
    protected Double orderNo;

    /**
     * 静态化页面链接
     */
    @Field
    protected String staticPageLink;

    /**
     * 是否删除 0：未删除  1： 删除
     */
    protected String delFlag;

    /**
     * 上报单位id
     */
    protected String creatorOrgid;

    /**
     * 上报单位名称
     */
    protected String creatorOrgname;

    /**
     * 推荐显示
     */
    protected String recommendShow;

    /**
     * 来源
     */
    protected String infoSources;

    /**
     * 来源类型：1 管理员新增，2 单位上报审核
     */
    protected String infoSourcesType;

    /**
     * 创建者ID
     */
    protected String createUserid;

    /**
     * 创建者用户名
     */
    protected String createUsername;

    /**
     * 添加时间
     */
    protected Date createTime;

    /**
     * 栏目名称
     */
    protected String columnName;

    /**
     * 子栏目名称
     */
    protected String subColumnName;

    /**
     * 位置名称
     */
    protected String seatName;

    protected Integer pageSize = 6;

    protected String themeName;

    private List<TWebInfoContentExtendedVO> extendedList;

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getUpdateUserid() {
        return this.updateUserid;
    }

    public void setUpdateUserid(String updateUserid) {
        this.updateUserid = updateUserid;
    }

    public String getUpdateUsername() {
        return this.updateUsername;
    }

    public void setUpdateUsername(String updateUsername) {
        this.updateUsername = updateUsername;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return this.id;
    }

    @Field
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    @Field
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getThumbnailBigLink() {
        return this.thumbnailBigLink;
    }

    public void setThumbnailBigLink(String thumbnailBigLink) {
        this.thumbnailBigLink = thumbnailBigLink;
    }

    public String getThumbnailSmallLink() {
        return this.thumbnailSmallLink;
    }

    public void setThumbnailSmallLink(String thumbnailSmallLink) {
        this.thumbnailSmallLink = thumbnailSmallLink;
    }

    public Date getPublishTime() {
        return this.publishTime;
    }

    @Field
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getIsTop() {
        return this.isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Double orderNo) {
        this.orderNo = orderNo;
    }

    public String getStaticPageLink() {
        return this.staticPageLink;
    }

    @Field
    public void setStaticPageLink(String staticPageLink) {
        this.staticPageLink = staticPageLink;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreatorOrgid() {
        return this.creatorOrgid;
    }

    public void setCreatorOrgid(String creatorOrgid) {
        this.creatorOrgid = creatorOrgid;
    }

    public String getCreatorOrgname() {
        return this.creatorOrgname;
    }

    public void setCreatorOrgname(String creatorOrgname) {
        this.creatorOrgname = creatorOrgname;
    }

    public String getRecommendShow() {
        return this.recommendShow;
    }

    public void setRecommendShow(String recommendShow) {
        this.recommendShow = recommendShow;
    }

    public String getInfoSources() {
        return this.infoSources;
    }

    public void setInfoSources(String infoSources) {
        this.infoSources = infoSources;
    }

    public String getInfoSourcesType() {
        return this.infoSourcesType;
    }

    public void setInfoSourcesType(String infoSourcesType) {
        this.infoSourcesType = infoSourcesType;
    }

    public String getCreateUserid() {
        return this.createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public String getCreateUsername() {
        return this.createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubColumnName() {
        return subColumnName;
    }

    public void setSubColumnName(String subColumnName) {
        this.subColumnName = subColumnName;
    }

    public String getcPublishTime() {
        return cPublishTime;
    }

    public void setcPublishTime(String cPublishTime) {
        this.cPublishTime = cPublishTime;
    }

    public List<TWebInfoContentExtendedVO> getExtendedList() {
        return extendedList;
    }

    public void setExtendedList(List<TWebInfoContentExtendedVO> extendedList) {
        this.extendedList = extendedList;
    }
}
