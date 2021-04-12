package com.tzword.contentcenter.service;

import com.tzword.contentcenter.dao.content.ShareMapper;
import com.tzword.contentcenter.domain.entity.content.Share;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/11 16:38
 */
@Service
public class ShareService {
    @Autowired
    private ShareMapper shareMapper;

    public Share auditById(Integer id) {
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null){
            throw new IllegalArgumentException("参数非法");
        }
        return share;
    }
}
