package com.lavacorp.db.dao;

import com.lavacorp.db.mapper.SupplierMapper;
import com.lavacorp.entities.company.Supplier;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;

@RegisterRowMapper(SupplierMapper.class)
public interface SupplierDao extends DaoNamed<Supplier> {
}
