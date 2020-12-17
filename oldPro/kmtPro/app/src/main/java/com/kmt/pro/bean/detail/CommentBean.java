package com.kmt.pro.bean.detail;

import java.io.Serializable;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-02 17:37
 **/
public class CommentBean implements Serializable {
    public String evaId;
    public String userId;
    public String userName;//昵称
    public String headURL;//评论人头像
    public String content;//评论文字
    public String images;//评论图片
    public String postTime;//评论时间
    public String ifIsSelf;//是否是自己（1代表是，0代表不是）
    public String likeNum;//点赞数
    public String isLiked;//该用户是否赞过，（1，赞过，0，没赞过）
    public String commentNum;//评论数
    public String hasInvestorsTag;//0 不是发行人/1是发行人
    public String isShowAll = "0";//自定义数据,评论是否显示全部, "0"为部分 "1"为全部

    public List<UserCommentListBean> userCommentList;
    public List<String> likedPeople;

    public CommentBean() {
        userName = "userName";
        headURL = "investor/5gVtxGqwXcQ.jpg";
        content = "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent";
        postTime = "2020-07-02";
        images = "investor/5gVtxGqwXcQ.jpg,investor/3kqHgnv4UiI.jpg,investor/9OFjbIol6Ha.jpg,investor/5CE65LmFOSo.jpg,investor/5gVtxGqwXcQ.jpg,investor/3kqHgnv4UiI.jpg,investor/9OFjbIol6Ha.jpg,investor/5CE65LmFOSo.jpg,investor/5CE65LmFOSo.jpg";
    }

    public static class UserCommentListBean implements Serializable {
        public String content;//二级评论的内容
        public String dateStr;//时间
        public String evaId;//子评论ID
        public String images;
        public String investorsCode;
        public String parentEvaId;//父评论ID
        public String replayUserId;//回复人的userid
        public String replayUserName;//回复人的姓名
        public String userAvatar;//二级评论人的头像
        public String userId;//评论人的id
        public String userName;//评论人的姓名
        public String hasInvestorsTag;//0 不是发行人/1是发行人

    }
}
