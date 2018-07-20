package com.wust.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.CommentVo;
import com.wust.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 13 on 2017/2/26.
 */
@Controller
@RequestMapping("admin/comments")
public class CommentController_Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController_Consumer.class);

    private static final String REST_URL_PREFIX = "http://BLOG-COMMENT/";

    /**
     * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
     * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        //UserVo users = this.user(request);

        PageInfo<CommentVo> commentsPaginator = restTemplate.getForObject(REST_URL_PREFIX + "admin/comments/", PageInfo.class);

        List<CommentVo> currLists = new ArrayList<>();
        String jsonData = JsonUtil.getJsonFromBean(commentsPaginator.getList());
        JSONArray jArray= JSONArray.parseArray(jsonData);
        Collection collection = JSONArray.parseArray(jArray.toJSONString(), CommentVo.class);
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            CommentVo currContent = (CommentVo) it.next();
            currLists.add(currContent);
        }
        commentsPaginator.setList(currLists);

        request.setAttribute("comments", commentsPaginator);
        return "admin/comment_list";
    }

    /**
     * 删除一条评论
     *
     * @param coid
     * @return
     */
    @PostMapping(value = "delete")
    @ResponseBody
    public RestResponseBo delete(@RequestParam Integer coid) {

        return restTemplate.getForObject(REST_URL_PREFIX + "admin/comments/delete/"+coid, RestResponseBo.class);

    }

    @PostMapping(value = "status")
    @ResponseBody
    public RestResponseBo delete(@RequestParam Integer coid, @RequestParam String status) {

        return restTemplate.getForObject(REST_URL_PREFIX + "admin/comments/status/"+coid+"/"+status, RestResponseBo.class);

    }

}
