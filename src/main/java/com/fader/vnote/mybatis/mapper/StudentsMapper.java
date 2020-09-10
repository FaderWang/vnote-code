package com.fader.vnote.mybatis.mapper;

import com.fader.vnote.mybatis.model.Students;
import org.apache.ibatis.annotations.Param;

/**
 * @author FaderW
 * 2019/11/26
 */

public interface StudentsMapper {

    Students selectStu(@Param("id") String id);
}
