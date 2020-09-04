package com.crm.mapper;

import com.crm.domain.Customer;
import com.crm.domain.Dictdetail;
import com.crm.query.CustomerQueryObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer record);

    Customer selectByPrimaryKey(Long id);

    List<Customer> selectAll();

    int updateByPrimaryKey(Customer record);

    List<Dictdetail> jobQueryInDetail();

    List<Dictdetail> salarylevelQueryInDetail();

    List<Dictdetail> customersourceQueryInDetail();

    Long getTotalForPage(CustomerQueryObject queryObject);

    List queryForPage(CustomerQueryObject queryObject);

    int updateStatus(@Param("id") Long id, @Param("i") int i);
}