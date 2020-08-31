package com.crm.mapper;

import com.crm.domain.Dictcatalog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictcatalogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Dictcatalog record);

    Dictcatalog selectByPrimaryKey(Long id);

    List<Dictcatalog> selectAll();

    int updateByPrimaryKey(Dictcatalog record);
}