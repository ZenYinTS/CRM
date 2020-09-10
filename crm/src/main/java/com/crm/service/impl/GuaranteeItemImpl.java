package com.crm.service.impl;

import com.crm.domain.Guaranteeitem;
import com.crm.mapper.GuaranteeitemMapper;
import com.crm.service.IGuaranteeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuaranteeItemImpl implements IGuaranteeItemService {

    @Autowired
    private GuaranteeitemMapper guaranteeItemDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return guaranteeItemDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Guaranteeitem record) {
        return guaranteeItemDao.insert(record);
    }

    @Override
    public Guaranteeitem selectByPrimaryKey(Long id) {
        return guaranteeItemDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Guaranteeitem> selectByGid(Long gid) {
        return guaranteeItemDao.selectByGid(gid);
    }

    @Override
    public List<Guaranteeitem> selectAll() {
        return guaranteeItemDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Guaranteeitem record) {
        return guaranteeItemDao.updateByPrimaryKey(record);
    }
}
