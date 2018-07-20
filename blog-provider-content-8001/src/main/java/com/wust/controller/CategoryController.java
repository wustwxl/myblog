package com.wust.controller;

import com.wust.constant.WebConst;
import com.wust.dto.MetaDto;
import com.wust.dto.Types;
import com.wust.entity.Bo.RestResponseBo;
import com.wust.entity.Vo.ContentVo;
import com.wust.service.IMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 13 on 2017/2/21.
 */
@Controller
@RequestMapping("admin/category")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    private IMetaService metasService;

    @GetMapping(value = "")
    @ResponseBody
    public Map index() {
        List<MetaDto> categories = metasService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
        List<MetaDto> tags = metasService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        //request.setAttribute("categories", categories);
        //request.setAttribute("tags", tags);
        //return "admin/category";

        LOGGER.info("+++++++++++++++++++++"+categories);
        LOGGER.info("+++++++++++++++++++++"+tags);
        Map<String,Object> result = new HashMap<>();
        result.put("categories", categories);
        result.put("tags", tags);

        return  result;
    }

    @PostMapping(value = "save")
    @ResponseBody
    public RestResponseBo saveCategory(@RequestBody Map param) {

        String cname = (String) param.get("cname");
        Integer mid = (Integer) param.get("mid");

        try {
            metasService.saveMeta(Types.CATEGORY.getType(), cname, mid);
        } catch (Exception e) {
            String msg = "分类保存失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    @GetMapping(value = "delete/{mid}")
    @ResponseBody
    public RestResponseBo delete(@PathVariable int mid) {
        try {
            metasService.delete(mid);
        } catch (Exception e) {
            String msg = "删除失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

}
