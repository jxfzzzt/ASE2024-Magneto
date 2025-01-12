package com.car.account.web.mapper.goods;

import com.car.account.web.model.goods.GoodsParent;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/23
 */
@Repository
public interface GoodsParentMapper extends Mapper<GoodsParent> {

    /**
     * g根据父节点查询分组信息
     * @param search
     * @return
     */
    List<GoodsParent> queryListByParent(GoodsParent search);

}
