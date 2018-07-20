package com.wust.controller;

import com.github.pagehelper.PageInfo;
import com.wust.dto.LogActions;
import com.wust.dto.Types;
import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.ContentVo;
import com.wust.entity.Vo.ContentVoExample;
import com.wust.entity.Vo.MetaVo;
import com.wust.entity.Vo.UserVo;
import com.wust.service.IContentService;
import com.wust.service.ILogService;
import com.wust.service.IMetaService;
import com.wust.utils.StringUtil;
import com.wust.vo.MessageVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/article")
public class ContentController {

	private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

	@Value("${SUCCESS_RESULT}")
	private String SUCCESS_RESULT;

	@Autowired
	private IContentService contentsService;

	@Resource
	private IMetaService metasService;

	@Resource
	private ILogService logService;

	@GetMapping(value = "/test")
	@ResponseBody
	public MessageVo test() {

		MessageVo mesVo = new MessageVo();
		String errorCode = StringUtil.getStringRandom(12);
		//打印12位随机字符串,用于日至搜索。
		logger.error("errorCode-----" + errorCode);

		mesVo.setCode(1);
		mesVo.setInfo("获取文章列表成功!");
		mesVo.setErrorCode(errorCode);
		mesVo.setData(contentsService.getContents("1"));

		return mesVo;
	}

	@GetMapping(value = "")
	@ResponseBody
	public PageInfo<ContentVo>  index(@RequestParam(value = "page", defaultValue = "1") int page,
	                    @RequestParam(value = "limit", defaultValue = "15") int limit) {

		ContentVoExample contentVoExample = new ContentVoExample();
		contentVoExample.setOrderByClause("created desc");
		contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType());
		PageInfo<ContentVo> contentsPaginator = contentsService.getArticlesWithpage(contentVoExample, page, limit);
		//request.setAttribute("articles", contentsPaginator);
		//return "admin/article_list";

		return contentsPaginator;
	}

	@GetMapping(value = "/publish")
	@ResponseBody
	public List<MetaVo> newArticle() {
		List<MetaVo> categories = metasService.getMetas(Types.CATEGORY.getType());
		//request.setAttribute("categories", categories);
		//return "admin/article_edit";
		return categories;
	}

	@GetMapping(value = "/{cid}")
	@ResponseBody
	public Map editArticle(@PathVariable String cid) {
		ContentVo contents = contentsService.getContents(cid);
		//request.setAttribute("contents", contents);
		List<MetaVo> categories = metasService.getMetas(Types.CATEGORY.getType());
		//request.setAttribute("categories", categories);
		//request.setAttribute("active", "article");
		//return "admin/article_edit";

		Map<String,Object> result = new HashMap<>();
		result.put("contents", contents);
		result.put("categories", categories);

		return  result;
	}

	@PostMapping(value = "/publish")
	@ResponseBody
	public RestResponseBo publishArticle(@RequestBody ContentVo contents) {
		//UserVo users = this.user(request);

		UserVo users = new UserVo();
		users.setUsername("admin");
		users.setEmail("1175141062@qq.com");

		contents.setAuthorId(1);
		contents.setType(Types.ARTICLE.getType());
		if (StringUtils.isBlank(contents.getCategories())) {
			contents.setCategories("默认分类");
		}

		String result = contentsService.publish(contents);
		if (!SUCCESS_RESULT.equals(result)) {
			return RestResponseBo.fail(result);
		}
		return RestResponseBo.ok();
	}

	@PostMapping(value = "/modify")
	@ResponseBody
	public RestResponseBo modifyArticle(@RequestBody ContentVo contents) {
		//UserVo users = this.user(request);

		UserVo users = new UserVo();
		users.setUsername("admin");
		users.setEmail("1175141062@qq.com");
		contents.setAuthorId(1);

		contents.setType(Types.ARTICLE.getType());
		String result = contentsService.updateArticle(contents);
		if (!SUCCESS_RESULT.equals(result)) {
			return RestResponseBo.fail(result);
		}
		return RestResponseBo.ok();
	}

	/**
	 * 更新文章评论数
	 * @param cid
	 * @return
	 */
	@PostMapping(value = "/comment/modify/{cid}")
	@ResponseBody
	public RestResponseBo updateContentByCid(@PathVariable String cid,@RequestParam int commentsNum) {

		ContentVo temp = contentsService.getContents(cid);
		temp.setCommentsNum(commentsNum);

		contentsService.updateContentByCid(temp);

		return RestResponseBo.ok();
	}

	/**
	 * 根据ID获取文章信息
	 * @param cid
	 * @return
	 */
	@GetMapping(value = "/comment/{cid}")
	@ResponseBody
	public ContentVo getContents(@PathVariable String cid) {

		return contentsService.getContents(cid);
	}

	@GetMapping(value = "/delete/{cid}")
	@ResponseBody
	public RestResponseBo delete(@PathVariable int cid) {
		String result = contentsService.deleteByCid(cid);
		//logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), 333);
		if (!SUCCESS_RESULT.equals(result)) {
			return RestResponseBo.fail(result);
		}
		return RestResponseBo.ok();
	}
}
