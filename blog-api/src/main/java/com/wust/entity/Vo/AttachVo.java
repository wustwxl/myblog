package com.wust.entity.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class AttachVo implements Serializable {
    private Integer id;

    private String fname;

    private String ftype;

    private String fkey;

    private Integer authorId;

    private Integer created;

    private static final long serialVersionUID = 1L;

}