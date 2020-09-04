package com.crm.service;

import com.crm.domain.PageResult;
import com.crm.domain.Transfer;
import com.crm.query.TransferQueryObject;

import java.util.List;

public interface ITransferService {
    PageResult queryForPage(TransferQueryObject queryObject);

    int deleteByPrimaryKey(Long id);

    int insert(Transfer record);

    Transfer selectByPrimaryKey(Long id);

    List<Transfer> selectAll();

    int updateByPrimaryKey(Transfer record);
}
