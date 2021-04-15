package com.tzword.contentcenter.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/15 21:28
 */
public interface MySource {
        String MY_OUTPUT = "my-output";

        @Output(MY_OUTPUT)
        MessageChannel output();
}
