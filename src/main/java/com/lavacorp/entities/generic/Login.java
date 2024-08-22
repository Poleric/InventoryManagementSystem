package com.lavacorp.entities.generic;

import com.lavacorp.db.Database;
import org.jdbi.v3.core.Handle;
import com.lavacorp.db.dao.UserDao;
import com.lavacorp.entities.User;
import org.jetbrains.annotations.Nullable;


public class Login {
    public void register(String name, String password) {
        User user = User.builder()
                .name(name)
                .password(password)
                .build();

        try (Handle handle = Database.getJdbi().open()) {
            UserDao dao = handle.attach(UserDao.class);
            dao.create(user);
        }
        System.out.println("User registered: " + name);

    }

    public @Nullable User login(String name, String password) {
        User user;

        try (Handle handle = Database.getJdbi().open()) {
            UserDao dao = handle.attach(UserDao.class);
            user = dao.retrieve(name);
        }

        if (user == null)
            return null;

        if (user.comparePassword(password))
            return user;

        return null;
    }
}
