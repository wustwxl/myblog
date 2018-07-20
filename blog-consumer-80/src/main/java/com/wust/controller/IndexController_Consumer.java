package com.wust.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wust.constant.WebConst;
import com.wust.dto.MetaDto;
import com.wust.dto.Types;
import com.wust.entity.Bo.ArchiveBo;
import com.wust.entity.Bo.CommentBo;
import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.CommentVo;
import com.wust.entity.Vo.ContentVo;
import com.wust.entity.Vo.MetaVo;
import com.wust.utils.IPKit;
import com.wust.utils.JsonUtil;
import com.wust.utils.PatternKit;
import com.wust.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class IndexController_Consumer extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController_Consumer.class);

	private static final String REST_URL_PREFIX = "http://BLOG-CONTENT/";

	/**
	 * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
	 * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 首页
	 * @param request
	 * @param limit
	 * @return
	 */
	@GetMapping(value = "/")
	public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
		return this.index(request, 1, limit);
	}


	/**
	 * 首页分页
	 *
	 * @param request request
	 * @param p       第几页
	 * @param limit   每页大小
	 * @return 主页
	 */
	@GetMapping(value = "page/{p}")
	public String index(HttpServletRequest request, @PathVariable int p, @RequestParam(value = "limit", defaultValue = "12") int limit) {

		PageInfo<ContentVo> contentsPaginator = restTemplate.getForObject(REST_URL_PREFIX + "page/" + p, PageInfo.class);

		List<ContentVo> currLists = new ArrayList<>();
		String jsonData = JsonUtil.getJsonFromBean(contentsPaginator.getList());
		JSONArray jArray= JSONArray.parseArray(jsonData);
		Collection collection = JSONArray.parseArray(jArray.toJSONString(), ContentVo.class);
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			ContentVo currContent = (ContentVo) it.next();
			currLists.add(currContent);
		}
		contentsPaginator.setList(currLists);

		request.setAttribute("articles", contentsPaginator);
		if (p > 1) {
			this.title(request, "第" + p + "页");
		}
		return this.render("index");
	}

	/**
	 * 文章页
	 *
	 * @param request 请求
	 * @param cid     文章主键
	 * @return
	 */
	@GetMapping(value = {"article/{cid}", "article/{cid}.html"})
	public String getArticle(HttpServletRequest request, @PathVariable String cid) {
		ContentVo contents = restTemplate.getForObject(REST_URL_PREFIX + "article/" + cid, ContentVo.class);

		if (null == contents || "draft".equals(contents.getStatus())) {
			return this.render_404();
		}
		request.setAttribute("article", contents);
		request.setAttribute("is_post", true);

		completeArticle(request, contents);

		/*
		if (!checkHitsFrequency(request, cid)) {
			updateArticleHit(contents.getCid(), contents.getHits());
		}
		*/
		updateArticleHit(contents.getCid(), contents.getHits());

		return this.render("post");
	}

	/**
	 * 文章页(预览)
	 *
	 * @param request 请求
	 * @param cid     文章主键
	 * @return
	 */
	@GetMapping(value = {"article/{cid}/preview", "article/{cid}.html"})
	public String articlePreview(HttpServletRequest request, @PathVariable String cid) {
		ContentVo contents = restTemplate.getForObject(REST_URL_PREFIX + "article/" + cid + "/preview", ContentVo.class);

		if (null == contents) {
			return this.render_404();
		}
		request.setAttribute("article", contents);
		request.setAttribute("is_post", true);
		completeArticle(request, contents);

		/*
		if (!checkHitsFrequency(request, cid)) {
			updateArticleHit(contents.getCid(), contents.getHits());
		}
		*/
		updateArticleHit(contents.getCid(), contents.getHits());

		return this.render("post");

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

			PageInfo<CommentBo> commentsPaginator = restTemplate.getForObject("http://BLOG-COMMENT/admin/comments/comment/"+contents.getCid()+"/"+Integer.parseInt(cp)+"/6",PageInfo.class);

			List<CommentBo> currLists = new ArrayList<>();
			if(null == commentsPaginator.getList() )
			{
				LOGGER.info("+++++++++++++++++List Is Empty");
			}else{
				String jsonData = JsonUtil.getJsonFromBean(commentsPaginator.getList());
				Collection collection = JSONArray.parseArray(jsonData, CommentBo.class);
				Iterator it = collection.iterator();
				while (it.hasNext()) {
					CommentBo currContent = (CommentBo) it.next();
					currLists.add(currContent);
				}
			}
			commentsPaginator.setList(currLists);

			request.setAttribute("comments", commentsPaginator);
		}
	}

	/**
	 * 分类页
	 *
	 * @return
	 */
	@GetMapping(value = "category/{keyword}")
	public String categories(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
		return this.categories(request, keyword, 1, limit);
	}

	@GetMapping(value = "category/{keyword}/{page}")
	public String categories(HttpServletRequest request, @PathVariable String keyword,
	                         @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
		page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;


		Map<String,Object> result = restTemplate.getForObject(REST_URL_PREFIX + "category/" + keyword + "/" + page, Map.class);

		MetaDto metaDto = (MetaDto)result.get("meta");
		if (null == metaDto) {
			return this.render_404();
		}

		PageInfo<ContentVo> contentsPaginator = (PageInfo)result.get("articles");
		List<ContentVo> currLists = new ArrayList<>();
		String jsonData = JsonUtil.getJsonFromBean(contentsPaginator);
		JSONArray jArray= JSONArray.parseArray(jsonData);
		Collection collection = JSONArray.parseArray(jArray.toJSONString(), ContentVo.class);
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			ContentVo curr = (ContentVo) it.next();
			currLists.add(curr);
		}
		contentsPaginator.setList(currLists);

		request.setAttribute("articles", contentsPaginator);
		request.setAttribute("meta", metaDto);
		request.setAttribute("type", "分类");
		request.setAttribute("keyword", keyword);

		return this.render("page-category");
	}


	/**
	 * 归档页
	 *
	 * @return
	 */
    @GetMapping(value = "archives")
    public String archives(HttpServletRequest request) {
        List<ArchiveBo> archives = restTemplate.getForObject(REST_URL_PREFIX + "archives", List.class);
        request.setAttribute("archives", archives);
        return this.render("archives");
    }


	/**
	 * 友链页
	 *
	 * @return
	 */
	@GetMapping(value = "links")
	public String links(HttpServletRequest request) {
		List<MetaVo> links = restTemplate.getForObject(REST_URL_PREFIX + "links", List.class);
		request.setAttribute("links", links);
		return this.render("links");
	}

	/**
	 * 自定义页面,如关于的页面
	 */
	@GetMapping(value = "/{pagename}")
	public String page(@PathVariable String pagename, HttpServletRequest request) {

		ContentVo contents = restTemplate.getForObject(REST_URL_PREFIX  + pagename, ContentVo.class);

		if (null == contents) {
			return this.render_404();
		}

		if (contents.getAllowComment()) {
			String cp = request.getParameter("cp");
			if (StringUtils.isBlank(cp)) {
				cp = "1";
			}
			PageInfo<CommentBo> commentsPaginator = restTemplate.getForObject("http://BLOG-COMMENT/admin/comments/comment"+contents.getCid()+"/"+Integer.parseInt(cp)+"/6",PageInfo.class);

			List<CommentBo> currLists = new ArrayList<>();
			if(null == commentsPaginator.getList() )
			{
				LOGGER.info("+++++++++++++++++List Is Empty");
			}else{
				String jsonData = JsonUtil.getJsonFromBean(commentsPaginator.getList());
				Collection collection = JSONArray.parseArray(jsonData, CommentBo.class);
				Iterator it = collection.iterator();
				while (it.hasNext()) {
					CommentBo currContent = (CommentBo) it.next();
					currLists.add(currContent);
				}
			}
			commentsPaginator.setList(currLists);
			request.setAttribute("comments", commentsPaginator);
		}

		request.setAttribute("article", contents);

		/*
		if (!checkHitsFrequency(request, cid)) {
			updateArticleHit(contents.getCid(), contents.getHits());
		}
		*/
		updateArticleHit(contents.getCid(), contents.getHits());

		return this.render("page");
	}

	/**
	 * 评论操作
	 */
	@PostMapping(value = "comment")
	@ResponseBody
	public RestResponseBo comment(HttpServletRequest request, HttpServletResponse response,
	                              @RequestParam Integer cid, @RequestParam Integer coid,
	                              @RequestParam String author, @RequestParam String mail,
	                              @RequestParam String url, @RequestParam String text, @RequestParam String _csrf_token) {

		/*
		String ref = request.getHeader("Referer");
		if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
			return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
		}

		String token = cache.hget(Types.CSRF_TOKEN.getType(), _csrf_token);
		if (StringUtils.isBlank(token)) {
			return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
		}
		*/

		if (null == cid || StringUtils.isBlank(text)) {
			return RestResponseBo.fail("请输入完整后评论");
		}

		if (StringUtils.isNotBlank(author) && author.length() > 50) {
			return RestResponseBo.fail("姓名过长");
		}

		if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
			return RestResponseBo.fail("请输入正确的邮箱格式");
		}

		if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
			return RestResponseBo.fail("请输入正确的URL格式");
		}

		if (text.length() > 200) {
			return RestResponseBo.fail("请输入200个字符以内的评论");
		}

		String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
		Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
		if (null != count && count > 0) {
			return RestResponseBo.fail("您发表评论太快了，请过会再试");
		}

		author = TaleUtils.cleanXSS(author);
		text = TaleUtils.cleanXSS(text);

		author = EmojiParser.parseToAliases(author);
		text = EmojiParser.parseToAliases(text);

		CommentVo comments = new CommentVo();
		comments.setAuthor(author);
		comments.setCid(cid);
		comments.setIp(request.getRemoteAddr());
		comments.setUrl(url);
		comments.setContent(text);
		comments.setMail(mail);
		comments.setParent(coid);

		return restTemplate.postForObject("http://BLOG-COMMENT/admin/comments/comment", comments, RestResponseBo.class);

	}

	/**
	 * 搜索页
	 *
	 * @param keyword
	 * @return
	 */
	@GetMapping(value = "search/{keyword}")
	public String search(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
		return this.search(request, keyword, 1, limit);
	}

	@GetMapping(value = "search/{keyword}/{page}")
	public String search(HttpServletRequest request, @PathVariable String keyword, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
		page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

		PageInfo<ContentVo> articles = restTemplate.getForObject(REST_URL_PREFIX + "search/" + keyword + "/" + page, PageInfo.class);

		List<ContentVo> currLists = new ArrayList<>();
		String jsonData = JsonUtil.getJsonFromBean(articles.getList());
		JSONArray jArray= JSONArray.parseArray(jsonData);
		Collection collection = JSONArray.parseArray(jArray.toJSONString(), ContentVo.class);
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			ContentVo currContent = (ContentVo) it.next();
			currLists.add(currContent);
		}
		articles.setList(currLists);

		request.setAttribute("articles", articles);
		request.setAttribute("type", "搜索");
		request.setAttribute("keyword", keyword);
		return this.render("page-category");
	}

	/**
	 * 更新文章的点击率
	 *
	 * @param cid
	 * @param chits
	 */
	private void updateArticleHit(Integer cid, Integer chits) {
		Integer hits = cache.hget("article" + cid, "hits");
		if (chits == null) {
			chits = 0;
		}
		hits = null == hits ? 1 : hits + 1;
		if (hits >= WebConst.HIT_EXCEED) {
			ContentVo temp = new ContentVo();
			temp.setCid(cid);
			temp.setHits(chits + hits);

			restTemplate.postForObject(REST_URL_PREFIX + "hit", temp, Boolean.class);

			cache.hset("article" + cid, "hits", 1);
		} else {
			cache.hset("article" + cid, "hits", hits);
		}
	}

	/**
	 * 标签页
	 *
	 * @param name
	 * @return
	 */
	@GetMapping(value = "tag/{name}")
	public String tags(HttpServletRequest request, @PathVariable String name, @RequestParam(value = "limit", defaultValue = "12") int limit) {
		return this.tags(request, name, 1, limit);
	}

	/**
	 * 标签分页
	 *
	 * @param request
	 * @param name
	 * @param page
	 * @param limit
	 * @return
	 */
	@GetMapping(value = "tag/{name}/{page}")
	public String tags(HttpServletRequest request, @PathVariable String name, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {


		Map<String,Object> result = restTemplate.getForObject(REST_URL_PREFIX + "tag/" + name + "/" + page, Map.class);

		name = (String)result.get("keyword");

		MetaDto metaDto = (MetaDto)result.get("meta");
		if (null == metaDto) {
			return this.render_404();
		}

		PageInfo<ContentVo> contentsPaginator = (PageInfo)result.get("articles");
		List<ContentVo> currLists = new ArrayList<>();
		String jsonData = JsonUtil.getJsonFromBean(contentsPaginator);
		JSONArray jArray= JSONArray.parseArray(jsonData);
		Collection collection = JSONArray.parseArray(jArray.toJSONString(), ContentVo.class);
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			ContentVo curr = (ContentVo) it.next();
			currLists.add(curr);
		}
		contentsPaginator.setList(currLists);

		request.setAttribute("articles", contentsPaginator);
		request.setAttribute("meta", metaDto);
		request.setAttribute("type", "标签");
		request.setAttribute("keyword", name);

		return this.render("page-category");
	}

	/**
	 * 设置cookie
	 *
	 * @param name
	 * @param value
	 * @param maxAge
	 * @param response
	 */
	private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setSecure(false);
		response.addCookie(cookie);
	}

	/**
	 * 检查同一个ip地址是否在2小时内访问同一文章
	 *
	 * @param request
	 * @param cid
	 * @return
	 */
	private boolean checkHitsFrequency(HttpServletRequest request, String cid) {
		String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
		Integer count = cache.hget(Types.HITS_FREQUENCY.getType(), val);
		if (null != count && count > 0) {
			return true;
		}
		cache.hset(Types.HITS_FREQUENCY.getType(), val, 1, WebConst.HITS_LIMIT_TIME);
		return false;
	}


}
