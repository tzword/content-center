package com.tzword.contentcenter.domain.entity.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RocketmqTransactionLog {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 事务id
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 日志
     */
    private String log;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取事务id
     *
     * @return transaction_id - 事务id
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 设置事务id
     *
     * @param transactionId 事务id
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * 获取日志
     *
     * @return log - 日志
     */
    public String getLog() {
        return log;
    }

    /**
     * 设置日志
     *
     * @param log 日志
     */
    public void setLog(String log) {
        this.log = log;
    }
}