CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       role TEXT NOT NULL CHECK (role IN ('CLIENT', 'FREELANCER', 'ADMIN')),
                       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          client_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_project_client FOREIGN KEY (client_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE projects_freelancers (
                                      project_id BIGINT NOT NULL,
                                      freelancer_id BIGINT NOT NULL,
                                      PRIMARY KEY (project_id, freelancer_id),
                                      CONSTRAINT fk_pf_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_pf_freelancer FOREIGN KEY (freelancer_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tasks (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       description TEXT,
                       priority VARCHAR(10) NOT NULL CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
                       status VARCHAR(20) NOT NULL CHECK (status IN ('NEW', 'PENDING', 'COMPLETED', 'CANCELED')),
                       deadline TIMESTAMP,
                       estimated_time INTEGER,
                       creator_id BIGINT NOT NULL,
                       project_id BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_task_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
                       CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE time_entries (
                              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              task_id BIGINT NOT NULL,
                              user_id BIGINT NOT NULL,
                              time_spent INTEGER NOT NULL CHECK (time_spent > 0),
                              entry_date TIMESTAMP NOT NULL,
                              description VARCHAR(500),
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_time_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
                              CONSTRAINT fk_time_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE comments (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          task_id BIGINT NOT NULL,
                          author_id BIGINT NOT NULL,
                          text TEXT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_comment_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
                          CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE reminders (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           task_id BIGINT NOT NULL,
                           creator_id BIGINT NOT NULL,
                           message TEXT NOT NULL,
                           remind_at TIMESTAMP NOT NULL,
                           is_sent BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_reminder_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
                           CONSTRAINT fk_reminder_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_time_entries_task ON time_entries(task_id);
CREATE INDEX idx_comments_task ON comments(task_id);
CREATE INDEX idx_reminders_task ON reminders(task_id);
CREATE INDEX idx_reminders_unsent ON reminders(remind_at) WHERE is_sent = FALSE;
