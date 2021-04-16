package com.tzword.contentcenter.service;

import com.tzword.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.tzword.contentcenter.dao.content.ShareMapper;
import com.tzword.contentcenter.domain.dto.messageing.UserAddBonusMsgDTO;
import com.tzword.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.tzword.contentcenter.domain.entity.content.Share;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/11 16:38
 */
@Service
public class ShareService {
    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Autowired
    private Source source;

//    public Share auditById(Integer id) {
//        Share share = this.shareMapper.selectByPrimaryKey(id);
//        if (share == null){
//            throw new IllegalArgumentException("参数非法");
//        }
//        String transactionId = UUID.randomUUID().toString();
//
//        //  1.发送半消息
//         this.rocketMQTemplate.sendMessageInTransaction(
//                 "tx-add-bonus-group",
//                 "add-bonus",
//                 MessageBuilder.withPayload(
//                         UserAddBonusMsgDTO.builder().userId(share.getId()).bonus(50).build()
//                 )
//                         .setHeader(RocketMQHeaders.TRANSACTION_ID,transactionId)
//                         .setHeader("share_id",id)
//                         .build(),
//                 "审核原因"
//                 );
//
//
//
//        return share;
//    }

    /**
     * @Description: 使用stream实现分布式事务
     * @param id 1
     * @return com.tzword.contentcenter.domain.entity.content.Share
     * @throws
     * @author jianghy
     * @date 2021/4/16 12:05
     */
    public Share auditById(Integer id) {
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null){
            throw new IllegalArgumentException("参数非法");
        }
        String transactionId = UUID.randomUUID().toString();

        //  1.发送半消息
        this.source.output().send(
                MessageBuilder.withPayload(
                         UserAddBonusMsgDTO.builder().userId(share.getId()).bonus(50).build()
                 )
                        //header有妙用
                         .setHeader(RocketMQHeaders.TRANSACTION_ID,transactionId)
                         .setHeader("share_id",id)
                        //这里如果是dto,需要转成json字符串
                        //.setHeader("msg",JSON.toJSONString(dto))
                        .setHeader("msg","stream审核原因")
                         .build()
        );



        return share;
    }


    /**
     * @Description: 执行本地方法
     * @param id 1
 * @param reson 2 
     * @return void 
     * @throws
     * @author jianghy
     * @date 2021/4/13 22:14 
     */
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDB(Integer id,String reson){
        //修改姓名
        Share share = Share.builder().aboutme("张三").id(id).avatar(reson).build();
        //加上selective只会更新不为空的字段
        this.shareMapper.updateByPrimaryKeySelective(share);
    }


    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithRocketMqLog(Integer id,String reson,String transactionId){
        this.auditByIdInDB(id,reson);
        this.rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("审核分享。。")
                        .build()

        );
    }
}
