package com.tzword.contentcenter.rocketmq;

import com.tzword.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.tzword.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.tzword.contentcenter.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/13 21:50
 */
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    private final ShareService shareService;

    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * @Description: 执行本地事务
     * @param msg 1
     * @param arg 2
     * @return org.apache.rocketmq.spring.core.RocketMQLocalTransactionState
     * @throws
     * @author jianghy
     * @date 2021/4/13 21:52
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        MessageHeaders headers = msg.getHeaders();
        String transactionId = (String)headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.valueOf((String) headers.get("share_id"));
        try {
            this.shareService.auditByIdWithRocketMqLog(shareId,(String) arg,transactionId);
            //本地事务成功就提交
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            //本地事务失败就回滚
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }



    /**
     * @Description: 本地事务的检查方法（如果mq没有收到提交或者回滚的请求就会执行这个方法）
     * @param msg 1
     * @return org.apache.rocketmq.spring.core.RocketMQLocalTransactionState
     * @throws
     * @author jianghy
     * @date 2021/4/13 21:53
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        MessageHeaders headers = msg.getHeaders();
        String transactionId = (String)headers.get(RocketMQHeaders.TRANSACTION_ID);
        RocketmqTransactionLog rocketmqTransactionLog = this.rocketmqTransactionLogMapper.selectOne(RocketmqTransactionLog.builder().transactionId(transactionId).build());
        if (rocketmqTransactionLog == null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;

    }
}
