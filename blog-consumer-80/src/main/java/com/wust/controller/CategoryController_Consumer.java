package com.wust.controller;

import com.alibaba.fastjson.JSONArray;
import com.wust.dto.MetaDto;
import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.MetaVo;
import com.wust.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin/category")
public class CategoryController_Consumer
{

	//通过IP调用服务
	//private static final String REST_URL_PREFIX = "http://localhost:8001";
	//通过微服务名 blog-content 调用服务,不再关心地址和端口
	private static final String REST_URL_PREFIX = "http://BLOG-CONTENT/";
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController_Consumer.class);
	/**
	 * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
	 * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 分类&标签管理 - 首页
	 * @param request
	 * @return
	 */
	@GetMapping(value = "")
	public String index(HttpServletRequest request) {


		Map<String,Object> result = restTemplate.getForObject(REST_URL_PREFIX + "admin/category", Map.class);

		List<MetaDto> catLists = new ArrayList<>();
		String catData = JsonUtil.getJsonFromBean(result.get("categories"));
		JSONArray catArray= JSONArray.parseArray(catData);
		Collection catCollection = JSONArray.parseArray(catArray.toJSONString(), MetaDto.class);
		Iterator catIt = catCollection.iterator();
		while (catIt.hasNext()) {
			MetaDto curr = (MetaDto) catIt.next();
			catLists.add(curr);
		}

		List<MetaDto> tagsLists = new ArrayList<>();
		String tagsData = JsonUtil.getJsonFromBean(result.get("tags"));
		JSONArray tagsArray= JSONArray.parseArray(tagsData);
		Collection tagsCollection = JSONArray.parseArray(tagsArray.toJSONString(), MetaDto.class);
		Iterator tagsIt = tagsCollection.iterator();
		while (tagsIt.hasNext()) {
			MetaDto curr = (MetaDto) tagsIt.next();
			tagsLists.add(curr);
		}

		request.setAttribute("categories", catLists);
		request.setAttribute("tags", tagsLists);
		return "admin/category";
	}

	/**
	 * 添加分类
	 * @param cname
	 * @param mid
	 * @return
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public RestResponseBo saveCategory(@RequestParam String cname, @RequestParam Integer mid) {

		Map<String,Object> param = new HashMap<>();
		param.put("cname", cname);
		param.put("mid", mid);

		return restTemplate.postForObject(REST_URL_PREFIX + "admin/category/save", param, RestResponseBo.class);
	}

	/**
	 * 删除分类&标签
	 * @param mid
	 * @return
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public RestResponseBo delete(@RequestParam int mid) {
		return restTemplate.getForObject(REST_URL_PREFIX + "admin/category/delete/"+mid, RestResponseBo.class);
	}
}
