package com.company.rocketmqdemo.producer.batch;

import org.springframework.messaging.Message;

import java.util.Iterator;
import java.util.List;

/**
 * 消息拆分
 *
 * @author Real
 * @since 2022/12/24 23:05
 */
public class MessageSplitter implements Iterator<List<Message<String>>> {

    /**
     * 分割数据大小
     */
    private final int sizeLimit = 1024 * 1024;

    /**
     * 分割数据列表
     */
    private final List<Message<String>> messages;

    /**
     * 分割索引
     */
    private int currIndex;

    public MessageSplitter(List<Message<String>> messages) {
        this.messages = messages;
        // 保证单条数据的大小不大于sizeLimit
        messages.forEach(m -> {
            if (m.toString().length() > sizeLimit) {
                throw new RuntimeException("单条消息不能大于" + sizeLimit + "B");
            }
        });
    }


    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message<String>> next() {
        int nextIndex = currIndex;
        int totalSize = 0;
        for (; nextIndex < messages.size(); nextIndex++) {
            Message<String> nextMessage = messages.get(nextIndex);
            totalSize = totalSize + nextMessage.toString().length();
            if (totalSize > sizeLimit) {
                break;
            }
        }
        List<Message<String>> subList = messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;
        return subList;
    }
}
