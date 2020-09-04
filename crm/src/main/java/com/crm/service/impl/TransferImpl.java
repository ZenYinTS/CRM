package com.crm.service.impl;

import com.crm.domain.PageResult;
import com.crm.domain.Transfer;
import com.crm.mapper.TransferMapper;
import com.crm.query.TransferQueryObject;
import com.crm.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferImpl implements ITransferService {

    @Autowired
    private TransferMapper transferDao;


    @Override
    public PageResult queryForPage(TransferQueryObject queryObject) {
        Long count = transferDao.queryPageCount(queryObject);
        if (count == 0)
            return new PageResult();
        List<Transfer> roles = transferDao.queryPage(queryObject);
        return new PageResult(count,roles);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return transferDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Transfer record) {
        return transferDao.insert(record);
    }

    @Override
    public Transfer selectByPrimaryKey(Long id) {
        return transferDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Transfer> selectAll() {
        return transferDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Transfer record) {
        return transferDao.updateByPrimaryKey(record);
    }
}
