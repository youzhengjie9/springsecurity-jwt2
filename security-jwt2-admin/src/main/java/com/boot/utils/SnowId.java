package com.boot.utils;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;


/**
 * 雪花算法生成分布式id
 * 注意：
 * ❄ 如果生成ID速度不超过5W/s，不用修改任何配置参数
 *
 * ❄ 如果生成ID速度超过5W/s，低于50W，推荐修改：options.SeqBitLength=10
 *
 * ❄ 如果生成ID速度超过50W/s，接近500W，推荐修改：options.SeqBitLength=12
 * 增加 SeqBitLength 会让性能更高，但生成的 ID 也会更长
 *
 * @author youzhengjie
 * @date 2022/10/16 23:45:45
 */
@ApiModel("对雪花漂移算法进行二次封装")
public class SnowId implements Serializable {

    private static IdGeneratorOptions options = new IdGeneratorOptions((short) 1); //雪花算法

    static {
        /**
         * 默认为6：50w并发需要8秒
         * 设置为10：50w并发只需要0.5秒，提升巨大
         */
        options.SeqBitLength=10;
        YitIdHelper.setIdGenerator(options);
    }

    //雪花算法生成分布式id
    public static long nextId(){

        return YitIdHelper.nextId();
    }

}
