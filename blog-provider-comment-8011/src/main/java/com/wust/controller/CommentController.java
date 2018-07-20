package com.wust.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wust.constant.WebConst;
import com.wust.dto.ErrorCode;
import com.wust.dto.Types;
import com.wust.entity.Bo.CommentBo;
import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.CommentVo;
import com.wust.entity.Vo.CommentVoExample;
import com.wust.entity.Vo.UserVo;
import com.wust.service.ICommentService;
import com.wust.utils.IPKit;
import com.wust.utils.PatternKit;
import com.wust.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13 on 2017/2/26.
 */
@Controller
@RequestMapping("admin/comments")
public class CommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    @Resource
    private ICommentService commentsService;

    @GetMapping(value = "")
    @ResponseBody
    public PageInfo index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit) {
        //UserVo users = this.user(request);

        UserVo users = new UserVo();
        users.setUsername("commenter");
        users.setEmail("752571422@qq.com");
        users.setUid(1);

        CommentVoExample commentVoExample = new CommentVoExample();
        commentVoExample.setOrderByClause("coid desc");
        commentVoExample.createCriteria().andAuthorIdNotEqualTo(users.getUid());
        PageInfo<CommentVo> commentsPaginator = commentsService.getCommentsWithPage(commentVoExample, page, limit);
        //request.setAttribute("comments", commentsPaginator);
        return commentsPaginator;
    }

    /**
     * 删除一条评论
     *
     * @param coid
     * @return
     */
    @GetMapping(value = "delete/{coid}")
    @ResponseBody
    public RestResponseBo delete(@PathVariable Integer coid) {
        try {
            CommentVo comments = commentsService.getCommentById(coid);
            if (null == comments) {
                return RestResponseBo.fail("不存在该评论");
            }
            commentsService.delete(coid, comments.getCid());
        } catch (Exception e) {
            String msg = "评论删除失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    @GetMapping(value = "status/{coid}/{status}")
    @ResponseBody
    public RestResponseBo delete(@PathVariable Integer coid, @PathVariable String status) {
        try {
            CommentVo comments = commentsService.getCommentById(coid);
            if (comments != null) {
                comments.setCoid(coid);
                comments.setStatus(status);
                commentsService.update(comments);
            } else {
                return RestResponseBo.fail("操作失败");
            }
        } catch (Exception e) {
            String msg = "操作失败";
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "comment")
    @ResponseBody
    public RestResponseBo comment(@RequestBody CommentVo comments) {

        try {
            String result = commentsService.insertComment(comments);

            if (!WebConst.SUCCESS_RESULT.equals(result)) {
                return RestResponseBo.fail(result);
            }
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "评论发布失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
    }

    @GetMapping(value = "comment/{cid}/{page}/{limit}")
    @ResponseBody
    public PageInfo<CommentBo> getComments(@PathVariable Integer cid,@PathVariable Integer page,@PathVariable Integer limit) {

        return commentsService.getComments(cid, page, limit);
    }

}
