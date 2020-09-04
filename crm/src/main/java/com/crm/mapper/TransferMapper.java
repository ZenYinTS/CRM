package com.crm.mapper;

import com.crm.domain.Transfer;
import com.crm.query.TransferQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Transfer record);

    Transfer selectByPrimaryKey(Long id);

    List<Transfer> selectAll();

    int updateByPrimaryKey(Transfer record);

    Long queryPageCount(TransferQueryObject queryObject);

    List<Transfer> queryPage(TransferQueryObject queryObject);
}