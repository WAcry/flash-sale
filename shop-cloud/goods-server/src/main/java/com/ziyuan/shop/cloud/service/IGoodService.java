package com.ziyuan.shop.cloud.service;

import com.ziyuan.shop.cloud.domain.Good;

import java.util.List;
import java.util.Set;

public interface IGoodService {

    /**
     * query items by ids
     * @param idList
     * @return
     */
    List<Good> getListByIdList(Set<Long> idList);
}
