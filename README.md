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
    └── java/
    └── com/melody/todoquadrantback/
        ├── controller/
        └──service/

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

## 🧪 Testing
This project includes both **unit tests** and **integration tests** to ensure reliability and maintainability.
### ✅ Test Frameworks & Tools
* **JUnit 5** – for writing unit and integration tests
* **Spring Boot Test** – for testing application context and REST endpoints
* **MockMvc** – for controller layer testing without starting the full server

### 📂 Test Structure
Test files are located under:
```
src/
└── test/
    └── java/
        └── com/melody/todoquadrantback/
            ├── controller/    ← API integration tests
            ├── service/       ← business logic unit tests
            ├── repository/    ← repository integration tests (optional)
```

### ▶️ Run All Tests
To run tests locally:
```
./mvnw test
```
Or run specific test class in your IDE.

---

## ⚙️ CI/CD Integration

This project uses **GitHub Actions** for continuous integration and deployment. All tests are automatically executed on each push to the `main` branch.

**LINE Notify integration** is included to send notifications on both test **success** and **failure**.

- ✅ **Example Workflow:** [backend-ci.yml](.github/workflows/backend-ci.yml)

### 🔔 How to Enable LINE Notify

1. Create a LINE Developer account and set up a new **Messaging API** channel.
2. Get the following from your channel settings:
   - **LINE_USER_ID** (your own LINE user ID)
   - **LINE_CHANNEL_ACCESS_TOKEN** (Messaging API token)
3. Add the following secrets to your GitHub repository:
   - `LINE_USER_ID`
   - `LINE_CHANNEL_ACCESS_TOKEN`
4. The CI workflow will automatically send messages to your LINE account on test results.

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

---

## 🧩 Frontend Repository

👉 The frontend (React + Vite) is hosted here:
[https://github.com/Melodieeee/todo-quadrant-app](https://github.com/Melodieeee/todo-quadrant-app)

Frontend live: [https://todo-quadrant-app.vercel.app](https://todo-quadrant-app.vercel.app)

---

## 📬 License

MIT License © 2025 Melody Yu
