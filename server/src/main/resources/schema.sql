
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS tokens CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS intervals CASCADE;

CREATE TABLE users (
    id serial8 PRIMARY KEY,
    username VARCHAR(64) UNIQUE NOT NULL,
    email VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL
);


CREATE TABLE tokens (
    user_id int8 PRIMARY KEY,
    token VARCHAR(256) UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE friendships (
    smaller_user_id int8 NOT NULL,
    larger_user_id int8 NOT NULL,
    status int4 NOT NULL,
    PRIMARY KEY (smaller_user_id, larger_user_id),
    FOREIGN KEY (smaller_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (larger_user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE intervals (
    id serial8 PRIMARY KEY,
    user_id int8 NOT NULL,
    day_of_week int4 NOT NULL,
    start_min int4 NOT NULL,
    end_min int4 NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
