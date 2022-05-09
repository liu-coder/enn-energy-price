package com.enn.energy.price.biz.service.bo.proxyprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liujin
 * @version 1.0.0
 * @Date 2022/5/7 16:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeEntryBO implements Serializable {
    private static final long serialVersionUID = -8000984920098115185L;
    private String code;
    private String name;
}
