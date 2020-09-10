package com.fader.vnote.mybatis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

/**
 * @author FaderW
 * 2019/11/26
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Students {

    private String id;

    private String name;

    private int age;
}
