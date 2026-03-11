drop table if exists user_sessions;
drop index if exists idx_user_sessions_user_id;
drop index if exists idx_user_sessions_session_id;
drop index if exists idx_user_sessions_started_at;
CREATE TABLE user_sessions (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    
    session_id UUID NOT NULL UNIQUE,

    started_at TIMESTAMP NOT NULL,
    last_activity_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,

    client_type VARCHAR(20),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_sessions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_sessions_user_id 
ON user_sessions(user_id);

CREATE INDEX idx_user_sessions_session_id 
ON user_sessions(session_id);

CREATE INDEX idx_user_sessions_started_at 
ON user_sessions(started_at);

ALTER TABLE user_sessions
ALTER COLUMN started_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE user_sessions
ALTER COLUMN last_activity_at SET DEFAULT CURRENT_TIMESTAMP;