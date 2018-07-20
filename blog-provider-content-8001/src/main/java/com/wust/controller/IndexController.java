package com.wust.controller;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wust.constant.WebConst;
import com.wust.dto.ErrorCode;
import com.wust.dto.MetaDto;
import com.wust.dto.Types;
import com.wust.entity.Bo.ArchiveBo;
import com.wust.entity.Bo.CommentBo;
import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.CommentVo;
import com.wust.entity.Vo.ContentVo;
import com.wust.entity.Vo.MetaVo;
import com.wust.service.IContentService;
import com.wust.service.IMetaService;
import com.wust.service.ISiteService;
import com.wust.utils.IPKit;
import com.wust.utils.PatternKit;
import com.wust.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 * Created by Administrator on 2017/3/8 008.
 */
@Controller
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private IContentService contentService;

    /*
    @Resource
    private ICommentService commentService;
    */

    @Resource
    private IMetaService metaService;

    @Resource
    private ISiteService siteService;



    @GetMapping(value = "page/{p}")
    @ResponseBody
    public PageInfo<ContentVo> index(@PathVariable int p, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;
        PageInfo<ContentVo> articles = contentService.getContents(p, limit);

        /*
        request.setAttribute("articles", articles);
        if (p > 1) {
            this.title(request, "第" + p + "页");
        }
        return this.render("index");
        */
        return articles;
    }

    /**
     * 文章页
     *
     * @param cid     文章主键
     * @return
     */
    @GetMapping(value = {"article/{cid}", "article/{cid}.html"})
    @ResponseBody
    public ContentVo getArticle(@PathVariable String cid) {
        ContentVo contents = contentService.getContents(cid);
        /*
        if (null == contents || "draft".equals(contents.getStatus())) {
            return this.render_404();
        }
        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        completeArticle(request, contents);
        if (!checkHitsFrequency(request, cid)) {
            updateArticleHit(contents.getCid(), contents.getHits());
        }
        return this.render("post");
        */
        return contents;
    }

    /**
     * 文章页(预览)
     * @param cid     文章主键
     * @return
     */
    @GetMapping(value = {"article/{cid}/preview", "article/{cid}.html"})
    @ResponseBody
    public ContentVo articlePreview(@PathVariable String cid) {
        ContentVo contents = contentService.getContents(cid);
        /*
        if (null == contents) {
            return this.render_404();
        }
        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        completeArticle(request, contents);
        if (!checkHitsFrequency(request, cid)) {
            updateArticleHit(contents.getCid(), contents.getHits());
        }
        return this.render("post");
        */
        return contents;
    }

    /**
     * 抽取公共方法
     *
     * @param request
     * @param contents
     */
    private void completeArticle(HttpServletRequest request, ContentVo contents) {
        if (contents.getAllowComment()) {
            String cp = request.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            request.setAttribute("cp", cp);
            //PageInfo<CommentBo> commentsPaginator = commentService.getComments(contents.getCid(), Integer.parseInt(cp), 6);
            //request.setAttribute("comments", commentsPaginator);
        }
    }

    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        TaleUtils.logout(session, response);
    }


    /**
     * 分类页
     *
     * @return
     */
    @GetMapping(value = "category/{keyword}/{page}")
    @ResponseBody
    public Map categories(@PathVariable String keyword,
                             @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        MetaDto metaDto = metaService.getMeta(Types.CATEGORY.getType(), keyword);
        /*
        if (null == metaDto) {
            return this.render_404();
        }
        */

        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);

        /*
        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "分类");
        request.setAttribute("keyword", keyword);
        return this.render("page-category");
        */

        Map<String,Object> result = new HashMap<>();
        result.put("articles", contentsPaginator);
        result.put("meta", metaDto);
        return result;
    }


    /**
     * 归档页
     *
     * @return
     */
    @GetMapping(value = "archives")
    @ResponseBody
    public List archives() {
        List<ArchiveBo> archives = siteService.getArchives();
        /*
        request.setAttribute("archives", archives);
        return this.render("archives");
        */
        return archives;
    }


    /**
     * 友链页
     *
     * @return
     */
    @GetMapping(value = "links")
    @ResponseBody
    public List links() {
        List<MetaVo> links = metaService.getMetas(Types.LINK.getType());
        /*
        request.setAttribute("links", links);
        return this.render("links");
        */
        return links;
    }

    /**
     * 自定义页面,如关于的页面
     */
    @GetMapping(value = "/{pagename}")
    @ResponseBody
    public ContentVo page(@PathVariable String pagename) {
        ContentVo contents = contentService.getContents(pagename);

        return contents;
    }


    /**
     * 搜索页
     *
     * @param keyword
     * @return
     */
    @GetMapping(value = "search/{keyword}/{page}")
    @ResponseBody
    public PageInfo<ContentVo> search(@PathVariable String keyword, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        //page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        PageInfo<ContentVo> articles = contentService.getArticles(keyword, page, limit);
        /*
        request.setAttribute("articles", articles);
        request.setAttribute("type", "搜索");
        request.setAttribute("keyword", keyword);
        return this.render("page-category");
        */
        return articles;
    }

    /**
     * 更新文章的点击率
     * @param temp
     */
    @PostMapping(value = "hit")
    @ResponseBody
    private Boolean updateArticleHit(@RequestBody ContentVo temp) {

       contentService.updateContentByCid(temp);
       return true;

    }

    /**
     * 标签分页
     *
     * @param name
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "tag/{name}/{page}")
    @ResponseBody
    public Map tags(@PathVariable String name, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {

        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        // 对于空格的特殊处理
        name = name.replaceAll("\\+", " ");
        MetaDto metaDto = metaService.getMeta(Types.TAG.getType(), name);
        /*
        if (null == metaDto) {
            return this.render_404();
        }
        */

        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);
        /*
        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "标签");
        request.setAttribute("keyword", name);

        return this.render("page-category");
        */

        Map<String,Object> result = new HashMap<>();
        result.put("articles", contentsPaginator);
        result.put("meta", metaDto);
        result.put("keyword", name);
        return result;
    }


}
