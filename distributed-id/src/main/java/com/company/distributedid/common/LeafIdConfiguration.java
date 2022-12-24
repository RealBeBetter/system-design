package com.company.distributedid.common;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.segment.SegmentIDGenImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Real
 * Date: 2022/10/16 21:42
 */
@Configuration
public class LeafIdConfiguration {

    @Bean("iDGen")
    public IDGen getIdGen() {
        return new SegmentIDGenImpl();
    }

}
