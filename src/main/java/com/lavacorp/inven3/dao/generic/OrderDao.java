package com.lavacorp.inven3.dao.generic;

import com.lavacorp.inven3.dao.OrderDirection;
import com.lavacorp.inven3.dao.StockDao;
import com.lavacorp.inven3.model.generic.Order;
import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface OrderDao<T extends Order> extends Dao<T> {
    @CreateSqlObject
    StockDao getStockDao();

    @SqlUpdate
    @GetGeneratedKeys("id")
    int insertOrderDetails(@BindBean T order);

    @SqlQuery("select")
    List<T> selectAllByStatus(@Bind @Define Order.OrderStatus status);

    @SqlQuery("select")
    int selectAllByStatus(@Bind @Define Order.OrderStatus status, boolean count);

    @SqlQuery("select")
    List<T> selectAllByStatus(@Bind @Define Order.OrderStatus status, String orderColumn, OrderDirection orderDirection);

    @SqlQuery("select")
    List<T> selectAllByStatus(@Bind @Define Order.OrderStatus status, int page, int pageSize);

    @SqlQuery("select")
    List<T> selectAllByStatus(@Bind @Define Order.OrderStatus status, String orderColumn, OrderDirection orderDirection, int page, int pageSize);
}
