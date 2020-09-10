package com.fader.vnote.mybatis.model;

import lombok.Data;

/**
 * @author FaderW
 * 2019/11/27
 */
@Data
public class Score {

    private String id;

    private String stuId;

    private int score;

    private Students students;
}
