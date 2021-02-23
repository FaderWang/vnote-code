package com.fader.vnote.spring.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author FaderW
 * @Date 2021/2/23 14:08
 */
@Component
public class TransactionSimple {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 编程式事务
     */
    public void manualTransaction() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    // TODO 业务代码
                } catch (Exception e) {
                    // 回滚
                    status.setRollbackOnly();
                }
            }
        });

        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // TODO 业务代码
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            // 回滚
            platformTransactionManager.rollback(transactionStatus);
        }
    }
}
