package com.github.kaguya.util;

/**
 * Twitter雪花算法snowflake
 * <p>
 * 0  -  0000000000  0000000000  0000000000  0000000000  0  -  00000  -  00000  -  000000000000
 * 1位符号位-41位时间戳-10位(数据中心+机器Id)-|12位序列(自增的)
 * 符号位：始终为0，不可用。
 * 41位时间戳：精确到毫秒，可用69年
 * 12位序列：毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 *
 * <p>
 * 最终结果
 * 1、生成ID能够按照时间有序生成
 * 2、生成id的结果是一个64bit大小的整数，Long型
 * </p>
 **/
public class SnowFlake {

    /**
     * 开始时间戳，毫秒级别 2019-12-09 09:52:39 000
     */
    private final static long START_TIME_STAMP = 1575856359000L;

    /**
     * 序列号占用的位数
     */
    private final static long SEQUENCE_BIT = 12L;
    /**
     * 机器标识占用的位数
     */
    private final static long MACHINE_BIT = 8L;
    /**
     * 数据中心占用的位数
     */
    private final static long DATA_CENTER_BIT = 4L;

    /**
     * -1L ^ (-1L << n)表示占n个bit的数字的最大值是多少。
     * 例如：-1L ^ (-1L << 2)等于10进制的3，即二进制的11表示十进制3。
     */
    /**
     * 生成序列的掩码(0b111111111111=0xfff=4095)
     */
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);
    /**
     * 支持的最大机器标识ID 255
     */
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    /**
     * 支持的最大数据中心ID 15
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);

    /**
     * 机器ID向左移12位
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    /**
     * 数据中心ID向左移20位(12+8)
     */
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    /**
     * 时间戳向左移24位(12+8+4)
     */
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATA_CENTER_BIT;

    /**
     * 机器标识ID
     */
    private long machineId;
    /**
     * 数据中心ID
     */
    private long dataCenterId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 上次生成ID的时间截
     */
    private long lastTimeStamp = -1L;

    private static SnowFlake snowFlake = new SnowFlake(1, 1);

    public SnowFlake(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    public static Long generateId() {
        long id = snowFlake.nextId();
        return id;
    }

    /**
     * 产生下一个ID (该方法是线程安全的)
     *
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimeStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (timestamp == lastTimeStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //毫秒内序列溢出
            if (sequence == 0L) {
                timestamp = tilNextMillis(lastTimeStamp);
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTimeStamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return (timestamp - START_TIME_STAMP) << TIMESTAMP_LEFT
                | dataCenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        // 获得大于上次生成ID的时间截
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20191209; i++) {
            System.out.println(SnowFlake.generateId());
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}