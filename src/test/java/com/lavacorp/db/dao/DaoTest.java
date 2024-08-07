package com.lavacorp.db.dao;

import com.lavacorp.db.Database;
import com.lavacorp.entities.DatabaseObj;
import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(DatabaseExtension.class)
abstract public class DaoTest<T extends DatabaseObj, K extends Dao<T>> {
    Handle handle;
    K dao;

    final T[] DATA;
    final T[] UPDATED_DATA;
    final Class<K> DAO_TYPE;

    public DaoTest(Class<K> dao, T[] data, T[] updated_data) {
        this.DAO_TYPE = dao;
        this.DATA = data;
        this.UPDATED_DATA = updated_data;
    }

    public Stream<T> getData() {
        Assumptions.assumeTrue(DATA != null);
        Assumptions.assumeTrue(DATA.length > 0);

        return Stream.of(DATA);
    }

    public Stream<Arguments> getToUpdateData() {
        Assumptions.assumeTrue(UPDATED_DATA != null);
        Assumptions.assumeTrue(DATA.length == UPDATED_DATA.length);

        Stream.Builder<Arguments> builder = Stream.builder();
        for (int i = 0; i < DATA.length; i++)
            builder.add(Arguments.of(DATA[i], UPDATED_DATA[i]));
        return builder.build();
    }

    @BeforeEach
    void beforeEach() {
        handle = Database.getJdbi().open();
        dao = handle.attach(DAO_TYPE);

        handle.begin();
    }

    @AfterEach
    void afterEach() {
        handle.commit();
        handle.close();

        dao = null;
    }

    @ParameterizedTest
    @Order(0)
    @MethodSource("getData")
    void testCreate(T data) {
        dao.create(data);
    }

    @Test
    @Order(1)
    void testRetrieveAll() {
        List<T> expected = getData().toList();
        List<T> actual = dao.retrieveAll();

        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @ParameterizedTest
    @Order(1)
    @MethodSource("getData")
    void testRetrieveById(T expected) {
        Integer id = expected.getId();
        assertNotNull(id);

        T actual = dao.retrieve(id);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @Order(2)
    @MethodSource("getToUpdateData")
    void testUpdate(T expected, T updated) {
        assertNotNull(expected.getId());
        assertEquals(expected.getId(), updated.getId());

        dao.update(updated);

        T actual = dao.retrieve(expected.getId());
        assertEquals(updated, actual);

        handle.rollback();
    }

    @ParameterizedTest
    @Order(3)
    @MethodSource("getData")
    void testDeleteById(T expected) {
        Integer id = expected.getId();
        assertNotNull(id);

        dao.delete(id);
        assertNull(dao.retrieve(id));

        handle.rollback();
    }
}
