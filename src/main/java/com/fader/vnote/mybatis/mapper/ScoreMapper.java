package com.fader.vnote.mybatis.mapper;

import com.fader.vnote.mybatis.model.Score;
import org.apache.ibatis.annotations.Param;

/**
 * @author FaderW
 * 2019/11/27
 */

public interface ScoreMapper {

    Score getScoreById(@Param("id") String id);
}
