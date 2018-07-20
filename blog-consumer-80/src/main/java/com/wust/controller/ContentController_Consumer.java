package com.wust.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;

import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.ContentVo;
import com.wust.entity.Vo.MetaVo;
import com.wust.utils.JsonUtil;
import com.wust.vo.MessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin/article")
public class ContentController_Consumer {

	private static final Logger logger = LoggerFactory.getLogger(ContentController_Consumer.class);

	private static final String REST_URL_PREFIX = "http://BLOG-CONTENT/";

	/**
	 * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
	 * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
	 */
	@Autowired
	private RestTemplate restTemplate;


	/**
	 * 测试服务注册是否成功
	 * @return
	 */
	@GetMapping(value = "/test")
	@ResponseBody
	public MessageVo test() {

		return restTemplate.getForObject(REST_URL_PREFIX + "admin/article/test", MessageVo.class);

	}

	/**
	 * 后台管理-文章管理
	 * @param request
	 * @return
	 */
	@GetMapping(value = "")
	public String index(HttpServletRequest request) {

		PageInfo<ContentVo> contentsPaginator = restTemplate.getForObject(REST_URL_PREFIX + "admin/article", PageInfo.class);

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
		return "admin/article_list";
	}

	/**
	 * 后台管理-发布文章,需要先获取文章分类信息
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/publish")
	public String newArticle(HttpServletRequest request) {

		List<MetaVo> categories = restTemplate.getForObject(REST_URL_PREFIX + "admin/article/publish", List.class);
		request.setAttribute("categories", categories);
		return "admin/article_edit";
	}

	/**
	 * 获取指定文章
	 * @param cid
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/{cid}")
	public String editArticle(@PathVariable String cid, HttpServletRequest request) {

		Map<String,Object> result = restTemplate.getForObject(REST_URL_PREFIX + "admin/article/"+cid, Map.class);

		List<MetaVo> currLists = new ArrayList<>();
		String jsonData = JsonUtil.getJsonFromBean(result.get("categories"));
		JSONArray jArray= JSONArray.parseArray(jsonData);
		Collection collection = JSONArray.parseArray(jArray.toJSONString(), MetaVo.class);
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			MetaVo curr = (MetaVo) it.next();
			currLists.add(curr);
		}
		request.setAttribute("contents", result.get("contents"));
		request.setAttribute("categories", currLists);
		request.setAttribute("active", "article");
		return "admin/article_edit";
	}

	/**
	 * 发布文章
	 * @param contents
	 * @return
	 */
	@PostMapping(value = "/publish")
	@ResponseBody
	public RestResponseBo publishArticle(ContentVo contents) {

		return restTemplate.postForObject(REST_URL_PREFIX + "admin/article/publish", contents, RestResponseBo.class);
	}

	/**
	 * 修改文章
	 * @param contents
	 * @return
	 */
	@PostMapping(value = "/modify")
	@ResponseBody
	public RestResponseBo modifyArticle(ContentVo contents) {

		return restTemplate.postForObject(REST_URL_PREFIX + "admin/article/modify", contents, RestResponseBo.class);
	}

	/**
	 * 删除文章
	 * @param cid
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RestResponseBo delete(@RequestParam int cid) {

		return restTemplate.getForObject(REST_URL_PREFIX + "admin/article/delete/" + cid, RestResponseBo.class);
	}

}
