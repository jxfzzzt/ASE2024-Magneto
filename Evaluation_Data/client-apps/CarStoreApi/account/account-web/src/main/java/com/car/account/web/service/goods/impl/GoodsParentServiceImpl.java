package com.car.account.web.service.goods.impl;

import com.alibaba.fastjson.JSONArray;
import com.car.account.client.response.goods.GoodsParentRes;
import com.car.account.client.response.goods.sub.GoodsClassifyRes;
import com.car.account.web.common.constants.GoodsConstants;
import com.car.account.web.mapper.goods.GoodsParentMapper;
import com.car.account.web.model.goods.GoodsParent;
import com.car.account.web.service.goods.GoodsParentService;
import com.car.common.enums.StsEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouz
 * @date 2020/12/23
 */
@Slf4j
@Service
public class GoodsParentServiceImpl implements GoodsParentService {

    @Autowired
    GoodsParentMapper goodsParentMapper;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 查询父节点所有父组，根节点传-1
     * @param parentUuid
     * @return
     */
    @Override
    public ResultRes<List<GoodsParentRes>> queryListByParent(String parentUuid) {
        log.debug("根据父组uuid查询所有商品组 parentUuid {}",parentUuid);
        String cacheKey = String.format(GoodsConstants.GOODS_PARENT_QUERY_LIST_BY_PARENT_CACHE_KEY, new Object[] { parentUuid });
        String redisValue = (String) redisUtils.get(cacheKey);
        //开始接收缓存参数
        List<GoodsParentRes> goodsParentResList = new ArrayList<>();
        if(!StringUtils.isEmpty(redisValue)){
            goodsParentResList = JSONArray.parseArray(redisValue,GoodsParentRes.class);
            return ResultRes.success(goodsParentResList);
        }
        GoodsParent search = new GoodsParent();
        search.setParentId(parentUuid);
        search.setSts(StsEnum.ACTIVE.getValue());
        List<GoodsParent> goodsParentList = goodsParentMapper.queryListByParent(search);
        //封装返回输出
        for (GoodsParent goodsParent : goodsParentList) {
            GoodsParentRes res = new GoodsParentRes();
            BeanUtils.copyProperties(goodsParent, res);
            goodsParentResList.add(res);
        }
        //设置数据缓存时间
        redisUtils.set(cacheKey,JSONArray.toJSONString(goodsParentResList),5L, TimeUnit.MINUTES);
        return ResultRes.success(goodsParentResList);
    }

    /**
     * 根据uuid查询商品组信息
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<GoodsParentRes> queryGoodsParent(String uuid) {
        log.debug("根据uuid查询商品组信息 uuid {}",uuid);
        GoodsParent goodsParent = goodsParentMapper.selectByPrimaryKey(uuid);
//        if (goodsParent == null || StsEnum.INVALID.getValue().equals(goodsParent.getSts())) {
//            log.error("根据uuid查询商品组信息不存在 uuid {}",uuid);
//            throw new BusinessException(ResEnum.NON_EXISTENT);
//        }
        GoodsParentRes res = new GoodsParentRes();
        if (!StringUtils.isEmpty(goodsParent) && StsEnum.ACTIVE.getValue().equals(goodsParent.getSts())) {
            BeanUtils.copyProperties(goodsParent, res);
        }
        return ResultRes.success(res);
    }

    /**
     * 查询所有商品父组信息
     * @return
     */
    @Override
    public ResultRes<List<GoodsParentRes>> queryList() {
        log.debug("查询所有商品组信息");
        String cacheKey = GoodsConstants.GOODS_PARENT_QUERY_LIST_CACHE_KEY;
        String redisValue = (String) redisUtils.get(cacheKey);
        //开始接收缓存参数
        List<GoodsParentRes> goodsParentResList = new ArrayList<>();
        if(!StringUtils.isEmpty(redisValue)){
            goodsParentResList = JSONArray.parseArray(redisValue,GoodsParentRes.class);
            return ResultRes.success(goodsParentResList);
        }
        GoodsParent search = new GoodsParent();
        search.setSts(StsEnum.ACTIVE.getValue());
        List<GoodsParent> areaList = goodsParentMapper.select(search);
        //封装返回输出
        for (GoodsParent goodsParent : areaList) {
            GoodsParentRes res = new GoodsParentRes();
            BeanUtils.copyProperties(goodsParent, res);
            goodsParentResList.add(res);
        }
        //设置数据缓存时间
        redisUtils.set(cacheKey,JSONArray.toJSONString(goodsParentResList),5L, TimeUnit.MINUTES);
        return ResultRes.success(goodsParentResList);
    }

    @Override
    public ResultRes<List<GoodsClassifyRes>> queryClassify(String parentUuid) {

        List<GoodsClassifyRes> list = queryListByParentUuid(parentUuid);
        if(!CollectionUtils.isEmpty(list)){

            list.stream().forEach(s ->{
                String uuid = s.getUuid();
                List<GoodsClassifyRes> subList = queryListByParentUuid(uuid);

                if(!CollectionUtils.isEmpty(subList)){

                    List<GoodsParentRes> dst = new ArrayList<>();
                    subList.stream().forEach(ss ->{

                        GoodsParentRes vv = new GoodsParentRes();
                        BeanUtils.copyProperties(ss,vv);
                        dst.add(vv);
                    });
                    s.setSubList(dst);
                }
            });

        }
        return ResultRes.success(list);
    }


    private List<GoodsClassifyRes> queryListByParentUuid(String parentUuid){
        GoodsParent v = new GoodsParent();
        v.setSts(StsEnum.ACTIVE.getValue());
        v.setParentId(parentUuid);
        List<GoodsParent> list = goodsParentMapper.select(v);

        if(!CollectionUtils.isEmpty(list)){

            List<GoodsClassifyRes> dst = new ArrayList<>();
            list.stream().forEach(s ->{

                GoodsClassifyRes t = new GoodsClassifyRes();
                BeanUtils.copyProperties(s,t);
                dst.add(t);

            });

            return dst;
        }
        return null;
    }
}
