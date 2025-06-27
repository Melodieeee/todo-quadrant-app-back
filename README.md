# My Todo Quadrant App - Spring Boot Backend

This is the backend API for [**My Todo Quadrant App**](https://todo-quadrant-app.vercel.app/), a productivity tool that categorizes tasks using the Eisenhower Matrix (Urgent vs Important). This Spring Boot application provides RESTful endpoints for managing tasks and authenticating users via Google OAuth2.

---

## 🛠 Technologies Used

* **Spring Boot 3**
* **Spring Security + OAuth2 (Google Login)**
* **MongoDB Atlas** (cloud database)
* **Maven** for build
* **Docker** for containerized deployment
* **Deployed on [Render](https://render.com)**

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/melody/todoquadrantback/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── config/
│   └── resources/
│       ├── application.yml
│       └── static/
└── test/
```

---

## ⚙️ Environment Configuration

Create an `.env.local` file (or configure env variables in deployment platform like Render):

```
MONGODB_URI=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/<dbname>?retryWrites=true&w=majority
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
FRONTEND_URL=https://your-frontend-url
```

---

## 🚀 Running Locally

### Option 1: Run via Maven

1. Clone the repo:

   ```bash
   git clone https://github.com/Melodieeee/todo-quadrant-app-back.git
   cd todo-quadrant-app-back
   ```

2. Create `.env.local` file with MongoDB & OAuth info.

3. Run the app:

   ```bash
   ./mvnw spring-boot:run
   ```

The app will run at: `http://localhost:8080`

---

## 💪 Running with Docker

### Option 1: With `--env-file`

```bash
docker build -t todo-app .
docker run -p 8080:8080 --env-file .env.local todo-app
```

> `.env.local` should contain all required environment variables.

### Option 2: Set variables directly

```bash
docker build -t todo-quadrant-backend .

docker run -p 8080:8080 \
  -e MONGODB_URI="your-uri" \
  -e GOOGLE_CLIENT_ID="your-client-id" \
  -e GOOGLE_CLIENT_SECRET="your-client-secret" \
  -e FRONTEND_URL="http://localhost:5173" \
  todo-quadrant-backend
```

### Option 3: Docker Compose

```yaml
version: '3'
services:
  backend:
    build: .
    ports:
      - "8080:8080"
    env_file:
      - .env.local
```

Run with:

```bash
docker compose up --build
```

---

## 🌐 API Endpoints

* `GET /api/tasks/user` – fetch user's tasks
* `POST /api/tasks` – create a new task
* `PUT /api/tasks/{id}` – update task
* `DELETE /api/tasks/{id}` – delete task
* `GET /api/user/info` – get current logged-in user's info

---

## 🔐 Authentication

This backend uses **Google OAuth2 login**.

* Frontend initiates OAuth login
* Backend handles redirect and sets user session via Spring Security
* Once logged in, `/api/user/info` returns user details

---

## 👾 Deployment

You can deploy to [Render](https://render.com):

1. Push your backend repo to GitHub
2. Create new Web Service in Render
3. Select your repo, set environment variables
4. Use Dockerfile to build and deploy
5. Ensure MongoDB Atlas allows Render IP access

Live backend URL: [https://todo-quadrant-app-back.onrender.com](https://todo-quadrant-app-back.onrender.com)

---

## 🧩 Frontend Repository

👉 The frontend (React + Vite) is hosted here:
[https://github.com/Melodieeee/todo-quadrant-app](https://github.com/Melodieeee/todo-quadrant-app)

Frontend live: [https://todo-quadrant-app.vercel.app](https://todo-quadrant-app.vercel.app)

---

## 📬 License

MIT License © 2025 Melody Yu
