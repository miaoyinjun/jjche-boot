package org.jjche.cat.constant;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>
 * Cat Transaction 监控
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-24
 */
public class CatTransactionManager {

    public static <T> T newTransaction(Supplier<T> function, String type, String name, Map<String, Object> data) {
        Transaction transaction = Cat.newTransaction(type, name);
        if (data != null && !data.isEmpty()) {
            data.forEach(transaction::addData);
        }
        try {
            T result = function.get();
            transaction.setStatus(Message.SUCCESS);
            return result;
        } catch (Exception e) {
            Cat.logError(e);
            if (e.getMessage() != null) {
                Cat.logEvent(type + "_" + name + "_Error", e.getMessage());
            }
            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
    }

    public static <T> T newTransaction(Supplier<T> function, String type, String name) {
        return newTransaction(function, type, name, null);
    }

    public static void newTransaction(Runnable runnable, String type, String name, Map<String, Object> data) {
        Transaction transaction = Cat.newTransaction(type, name);
        if (data != null && !data.isEmpty()) {
            data.forEach(transaction::addData);
        }
        try {
            runnable.run();
            transaction.setStatus(Message.SUCCESS);
        } catch (Exception e) {
            Cat.logError(e);
            if (e.getMessage() != null) {
                Cat.logEvent(type + "_" + name + "_Error", e.getMessage());
            }
            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
    }

    public static void newTransaction(Runnable runnable, String type, String name) {
        newTransaction(runnable, type, name, null);
    }

}