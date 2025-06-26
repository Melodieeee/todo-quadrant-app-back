# My Todo Quadrant App - Spring Boot Backend

This is the backend API for **My Todo Quadrant App**, a productivity tool that categorizes tasks using the Eisenhower Matrix (Urgent vs Important). This Spring Boot application provides RESTful endpoints for managing tasks and authenticating users via Google OAuth2.

---

## 🛠 Technologies Used

- **Spring Boot 3**
- **Spring Security + OAuth2 (Google Login)**
- **MongoDB Atlas** (cloud database)
- **Maven** for build
- **Deployed on [Render](https://render.com)**

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/mytodo/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── config/
│   └── resources/
│       ├── application.properties
│       └── static/
└── test/
```

---

## ⚙️ Environment Configuration

Create an `.env` file (or configure env variables in deployment platform like Render):

```
MONGODB_URI=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/<dbname>?retryWrites=true&w=majority
OAUTH2_CLIENT_ID=your-google-client-id
OAUTH2_CLIENT_SECRET=your-google-client-secret
FRONTEND_URL=https://your-frontend.vercel.app
```

**application.properties** should look like:

```properties
spring.application.name=my-todo-quadrant-app-back
spring.data.mongodb.uri=${MONGODB_URI}
spring.security.oauth2.client.registration.google.client-id=${OAUTH2_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${OAUTH2_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.registration.google.scope=openid,email,profile

# Allow cross-origin requests from frontend
frontend.url=${FRONTEND_URL}

# Server settings for deployment
server.port=8080
server.address=0.0.0.0
```

---

## 🚀 Running Locally

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/my-todo-quadrant-backend.git
   cd my-todo-quadrant-backend
   ```

2. Create `.env` file with MongoDB & OAuth info.

3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```

The app will run at: `http://localhost:8080`

---

## 🌐 API Endpoints

- `GET /api/tasks/user` – fetch user's tasks
- `POST /api/tasks` – create a new task
- `PUT /api/tasks/{id}` – update task
- `DELETE /api/tasks/{id}` – delete task
- `GET /api/user/info` – get current logged-in user's info

---

## 🔐 Authentication

This backend uses **Google OAuth2 login**.
- Frontend initiates OAuth login
- Backend handles redirect and sets user session via Spring Security
- Once logged in, `/api/user/info` returns user details

---

## 🧾 Deployment

You can deploy to [Render](https://render.com):

1. Push your backend repo to GitHub
2. Create new Web Service in Render
3. Select your repo, set environment variables
4. Use Build Command:
   ```bash
   ./mvnw clean install
   ```
   Start Command:
   ```bash
   java -jar target/*.jar
   ```
5. Ensure MongoDB Atlas allows Render IP access

---

## 🧩 Frontend Repository

👉 The frontend (React + Vite) is hosted here:  
[Todo List Quadrant App](https://todo-quadrant-app.vercel.app/)

---

## 📬 License

MIT License © 2025 Melody Yu