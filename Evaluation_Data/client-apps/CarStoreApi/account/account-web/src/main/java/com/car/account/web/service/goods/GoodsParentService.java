package com.car.account.web.service.goods;

import com.car.account.client.response.goods.sub.GoodsClassifyRes;
import com.car.account.client.response.goods.GoodsParentRes;
import com.car.common.res.ResultRes;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/23
 */
public interface GoodsParentService {

    /**
     * 查询父节点所有地区，根节点传-1
     * @param parentUuid
     * @return
     */
    ResultRes<List<GoodsParentRes>> queryListByParent(String parentUuid);

    /**
     * 根据uuid查询商品组信息
     * @param uuid
     * @return
     */
    ResultRes<GoodsParentRes> queryGoodsParent(String uuid);

    /**
     * 查询所有商品父组信息
     * @return
     */
    ResultRes<List<GoodsParentRes>> queryList();

    /**
     * 查询分组列表信息
     * @param parentUuid 上级分组uuid
     * @return
     */
    ResultRes<List<GoodsClassifyRes>> queryClassify(String parentUuid);
}
