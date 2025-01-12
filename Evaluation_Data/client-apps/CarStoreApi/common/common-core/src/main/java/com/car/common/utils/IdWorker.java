package com.car.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 生成订单号
 * @author zhangyp
 * @date 2021年1月16日15:31:45
 */
@Slf4j
public class IdWorker {
    private static final long TWEPOCH = 1288834974657L;
    /**
     * 机器标识位数
     */
    private static final long WORKER_ID_BITS = 5L;
    /**
     * 数据中心标识位数
     */
    private static final long DATACENTER_ID_BITS = 5L;
    /**
     * 机器ID最大值
     */
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    /**
     * 数据中心ID最大值
     */
    private static final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);
    /**
     * 毫秒内自增位
     */
    private static final long SEQUENCE_BITS = 12L;
    /**
     * 机器ID偏左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据中心ID左移17位
     */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间毫秒左移22位
     */
    private static final long TIMESTAMP_LEFT_SHIFT =
            SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    private static long lastTimestamp = -1L;

    private long sequence = 0L;
    private final long workerId;
    private final long dataCenterId;

    public IdWorker(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        if (dataCenterId > MAX_DATACENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception(
                        "Clock moved backwards. Refusing to generate id for "
                                + (lastTimestamp - timestamp)
                                + " milliseconds");
            } catch (Exception e) {
                log.error("e:{}", e);
            }
        }

        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID
        long nextId =
                ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
                        | (dataCenterId << DATACENTER_ID_SHIFT)
                        | (workerId << WORKER_ID_SHIFT)
                        | sequence;

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        IdWorker idWorker = new IdWorker(1, 1);
        int i = 0;
        while (i < 100){

            System.out.println(idWorker.nextId());
            i++;
        }
    }
}
