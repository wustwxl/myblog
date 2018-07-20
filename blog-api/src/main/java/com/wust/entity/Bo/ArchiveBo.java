package com.wust.entity.Bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wust.entity.Vo.ContentVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ArchiveBo implements Serializable {

    private String date;
    private String count;
    private List<ContentVo> articles;

}
