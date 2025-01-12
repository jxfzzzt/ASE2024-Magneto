package com.car.utility.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Table;

/**
 * @author linjiang.xie
 * @date 2020/12/11 12:26
 */
@Data
@Table(name = "test")
public class Test extends BaseModelInfo {
}
