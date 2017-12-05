package cn.bubi.mybatis.balance.read.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 带锁的环形队列
 */
public class CircularBlockingQueue<E> extends CircularQueue<E>{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 对添加，删除，指针移动操作加锁
     */
    private final ReentrantLock putLock = new ReentrantLock();


    @Override
    public boolean add(E e){
        final ReentrantLock putLock = this.putLock;
        try {
            putLock.lockInterruptibly();
            super.add(e);

            return true;
        } catch (InterruptedException exp) {
            logger.error("found InterruptedException:", exp);
            return false;
        } finally {
            putLock.unlock();
        }

    }

    @Override
    public E next(){
        final ReentrantLock putLock = this.putLock;
        try {
            putLock.lockInterruptibly();
            return super.next();
        } catch (InterruptedException e) {
            logger.error("found InterruptedException:", e);
            return null;
        } finally {
            putLock.unlock();
        }

    }

    @Override
    public E prev(){
        final ReentrantLock putLock = this.putLock;
        try {
            putLock.lockInterruptibly();
            return super.prev();
        } catch (InterruptedException e) {
            logger.error("found InterruptedException:", e);
            return null;
        } finally {
            putLock.unlock();
        }
    }

    @Override
    public boolean remove(E e){
        final ReentrantLock putLock = this.putLock;
        try {
            putLock.lockInterruptibly();

            return super.remove(e);
        } catch (InterruptedException exp) {
            logger.error("found InterruptedException:", e);
            return false;
        } finally {
            putLock.unlock();
        }
    }

}