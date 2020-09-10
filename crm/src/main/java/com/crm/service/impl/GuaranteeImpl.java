package com.crm.service.impl;

import com.crm.domain.Guarantee;
import com.crm.domain.PageResult;
import com.crm.mapper.GuaranteeMapper;
import com.crm.query.GuaranteeQueryObject;
import com.crm.service.IGuaranteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuaranteeImpl implements IGuaranteeService {

    @Autowired
    private GuaranteeMapper guaranteeDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return guaranteeDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Guarantee record) {
        return guaranteeDao.insert(record);
    }

    @Override
    public Guarantee selectByPrimaryKey(Long id) {
        return guaranteeDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Guarantee> selectAll() {
        return guaranteeDao.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Guarantee record) {
        return guaranteeDao.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(GuaranteeQueryObject queryObject) {
        Long total = guaranteeDao.getTotalRows(queryObject);
        if (total == 0){
            return new PageResult();
        }
        List<Guarantee> guaranteeList = guaranteeDao.queryForPage(queryObject);
        return new PageResult(total,guaranteeList);
    }
}
