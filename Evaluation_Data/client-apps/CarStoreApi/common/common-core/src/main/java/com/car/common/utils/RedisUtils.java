package com.car.common.utils;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具
 * @author 谢林江
 *
 */
@Component
public class RedisUtils {


	@SuppressWarnings("rawtypes")
	@Autowired
    private RedisTemplate redisTemplate;

	@SuppressWarnings("rawtypes")
	@Autowired
    private RedisTemplate billredisTemplate;




	/**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
	public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
	public boolean set(final String key, Object value, Long expireTime ,TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * @param pattern
     */
    @SuppressWarnings("unchecked")
	public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }
    /**
     * 删除对应的value
     * @param key
     */
    @SuppressWarnings("unchecked")
	public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String getString(final String key) {
        Object result = null;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if(result==null){
            return null;
        }
        return result.toString();
    }

    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    @SuppressWarnings("unchecked")
	public void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    @SuppressWarnings("unchecked")
	public Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 列表添加
     * @param k
     * @param v
     */
    @SuppressWarnings("unchecked")
	public void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }

    /**
     * 列表添加
     * @param k
     * @param v
     */
    @SuppressWarnings("unchecked")
    public void lPush(String k, Collection v,Long timeOutSeconds){
        ListOperations list = billredisTemplate.opsForList();
        list.leftPush(k,v);
        list.getOperations().expire(k,timeOutSeconds,TimeUnit.SECONDS);
    }


    /**
     * 列表添加
     * @param k
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String k,Class<T> t){
        List<T> resList=new ArrayList<>();
        billredisTemplate.opsForList().range(k, 0, -1).stream().forEach(item-> resList.addAll(JSONArray.parseArray(JSONArray.toJSONString(item), t)));
        return  resList;
    }

    /**
     * 列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Object> lRange(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }

    /**
     * 集合添加
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
	public void add(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	public Set<Object> setMembers(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    @SuppressWarnings("unchecked")
	public void zAdd(String key,Object value,double scoure){
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new StringRedisSerializer());
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }

    /**
     * 有序集合获取
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    @SuppressWarnings("unchecked")
	public Set<Object> rangeByScore(String key,double scoure,double scoure1){
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new StringRedisSerializer());
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 通过Key跟scoure获取Values
     * @param key
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
	public Double getScoreByKey(String key,Object obj) {
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate.opsForZSet().score(key, obj);
    }

    /**
     * 获取zset中所有的数据
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
	public Set<Object> getZsetAll(String key){
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new StringRedisSerializer());
    	long zsetSize = redisTemplate.opsForZSet().size(key);
    	return redisTemplate.opsForZSet().reverseRange(key, 0, zsetSize);
    }

    public long getExpireTime(String cacheKey, TimeUnit seconds) {
        return redisTemplate.getExpire(cacheKey, seconds);
    }
}
