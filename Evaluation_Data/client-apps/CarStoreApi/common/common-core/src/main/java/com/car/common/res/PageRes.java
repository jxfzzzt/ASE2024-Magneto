package com.car.common.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.car.common.enums.ResEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suhaibo
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class PageRes<T> {
    @ApiModelProperty(value="输出编码，如：0000",name="code",example="0000")
    private String code;
    @ApiModelProperty(value="输出消息(String)",name="msg",example="操作成功")
    private String msg;
    @ApiModelProperty(value="输出对象(Object)",name="data")
    private T data;
    @ApiModelProperty(value="页数量",name="size" ,example="10")
    private Integer size;
    @ApiModelProperty(value="总条数",name="total" ,example="100")
    private Integer total;
    @ApiModelProperty(value="总页数",name="pages",example="10")
    private Integer pages;

    /**
     * 求页码
     * @param total
     * @param pageSize
     * @return
     */
    public static Integer dealSize(Integer total, Integer pageSize) {
        log.info("setSize total:{},pageSize:{}", total, pageSize);
        Integer pages = total / pageSize;
        Integer more = total % pageSize;
        if (more > 0) {
            ++pages;
        }
        log.info("setSize size:{}", pages);
        return pages;
    }

    public PageRes(){

    }

    /**
     *
     * @param data  数据对象
     * @param size  页大小
     * @param total 总数量
     * @param pages 总页数
     * @param code  返回编码
     * @param msg   返回消息
     */
    public PageRes(final T data, final Integer size, final Integer total,final Integer pages, final String code, final String msg) {
        this.data = data;
        this.size = size;
        this.total = total;
        this.code = code;
        this.msg = msg;
        this.pages = pages;
    }

    /**
     * 返回正确消息体
     * @param data
     * @param <T>
     * @return BaseRes
     */
    public static <T> PageRes<T> success(final T data, final Integer size, final Integer total, final Integer pages) {
        return new PageRes( data , size ,total ,pages , ResEnum.SUCCESS.getValue(), ResEnum.SUCCESS.getDesc());
    }


    /**
     * 判断是否执行正确
     * @return
     */
    public boolean isSuccess() {
        return this.code.equals(ResEnum.SUCCESS.getValue());
    }

}
