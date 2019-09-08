package com.boot.study.common;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.session.RowBounds;

@Data
@ToString
public class PageParam<T> {

    private int limit;

    private int page;

    private T condition;

    public RowBounds getRowBounds() {
        page = page < 1 ? 1 : page;
        limit = limit <= 0 ? 10 : limit;
        int offset = (page - 1) * limit;
        return new RowBounds(offset, limit);
    }


}
