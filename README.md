## Freelance tracker API
RESTful API service which provides project and task management for freelancers and their clients.
This API was developed as a part of a design and technology training practice.
### Features
[x] Registration and authentication with JWT
[x] User roles support: `CLIENT`, `FREELANCER`, `ADMIN`
[x] `CLIENT`: create project, assign freelancers, manage tasks
[x] `FREELANCER`: update task status, add reminders, log time entries
[x] `ADMIN`: manage users, review projects and tasks properties
### Quick start
#### Requirements
- Java 21+
- Maven 3.9+
- Docker Compose
#### Setup
1. Clone the repository:
```shell
git clone https://github.com/Jordosi/freelance-tracker.git
cd freelance-tracker/backend
```
2. Start up the database container
```shell
docker compose up -d
```
- Wait ~30 seconds for PostgreSQL to initialize
3. Build the project

```shell
mvn clean install
```
#### Launch
1. Run the application
```shell
java -jar target/freelance-tracker-*.jar
```
2. To stop the containers
```shell
docker compose down
```
### API endpoints
- `POST /api/auth/register` - Register a user
- `POST /api/auth/login` - Login
- `GET /api/tasks/{id}` - Retreive a task by ID
- `POST /api/tasks` - Create a task (requires `CLIENT` role)
- `POST /api/tasks/{id}/change-status` - Change task status (requires `FREELANCER` role)
- `DELETE /api/users/{id}` - Delete a user (requires `ADMIN` role)
### Database schema
The application uses a PostgreSQL database with the following table structure:
```sql
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
```

*Note:* For a visual representation, an ER-diagram is available at docs/er-diagram.png.
### Contributing
- Fork a repository and create a feature branch.
- Submit a pull request with a clear description.
- Issues and suggestions are welcome.
### Contact
- Author: Jordosi
- Last Updated: August 06, 2025