package com.tzword.contentcenter.controller;

import com.tzword.contentcenter.domain.dto.messageing.UserAddBonusMsgDTO;
import com.tzword.contentcenter.domain.entity.content.Share;
import com.tzword.contentcenter.service.ShareService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/11 16:35
 */
@RestController
@RequestMapping("/admin/shares")
public class ShareAdminController {
    @Autowired
    private ShareService shareService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PutMapping("/audit/{id}")
    public Share auditById(@PathVariable Integer id){
        //TODO 认证、授权
        Share share = this.shareService.auditById(id);
        //发送消息到mq
        rocketMQTemplate.convertAndSend("add-bonus",
                UserAddBonusMsgDTO.builder().userId(share.getId()).bonus(50).build());
        return share;
    }
}
