# College User Feedback Form Application
> **Course:** MCA (Master of Computer Applications) — Advanced Internet Programming  
> **Specification:** Jakarta EE 10 / Servlet API 6.0 | **Server:** Apache Tomcat 10.1+ | **Language:** Java 17+

---

## 📋 Project Overview

The **College User Feedback Form Application** is an enterprise-grade Java web application designed to collect, validate, process, and display student and stakeholder feedback. Built according to modern Jakarta EE standards, this project demonstrates end-to-end web request lifecycles, HTTP method handling (`POST` vs. `GET`), thread-safe in-memory data structures, client-side validation, and server-side dynamic HTML rendering with modern Glassmorphism CSS.

### 💻 Technology Stack

| Layer | Technology | Details |
| :--- | :--- | :--- |
| **Backend Runtime** | Java 17+ (LTS) | Modern Java SE features |
| **Servlet Specification** | Jakarta Servlet API 6.0 | Jakarta EE namespace (`jakarta.servlet.*`) |
| **Application Server** | Apache Tomcat 10.1+ | Servlet Container handling deployment & threads |
| **Build & Dependency Tool** | Apache Maven | Standard directory layout & `.war` packaging |
| **Frontend UI** | HTML5 & CSS3 | Semantic markup, Google Font *Plus Jakarta Sans* |
| **Design System** | Glassmorphism Theme | Responsive Flexbox & Grid, CSS custom properties |

---

## 🏛️ Architecture & Request Flow

The application follows the classical **Model-View-Controller (MVC)** architectural pattern within a Java Servlet environment:

```
+-------------------------------------------------------------------------------+
|                                CLIENT BROWSER                                 |
|                                                                               |
|   index.html (UI Form)  <======== [HTML Response] <======== PrintWriter       |
|            |                                                    ^             |
|            |  HTTP POST (Payload in Body)                       |             |
|            |  or HTTP GET (Query String Parameters)             |             |
+------------|----------------------------------------------------|-------------+
             |                                                    |
             v                                                    |
+-----------------------------------------------------------------|-------------+
|                          APACHE TOMCAT 10+ (Web Container)      |             |
|                                                                 |             |
|   1. Accepts TCP Connection & Parses HTTP Request               |             |
|   2. Instantiates/Dispatches to FeedbackServlet (/FeedbackServlet)            |
|                                                                 |             |
|        +--------------------------------------------------------+----+        |
|        |           FeedbackServlet (Controller Layer)                |        |
|        |                                                             |        |
|        |   • doPost(req, resp)  ──> Extracts body parameters         |        |
|        |   • doGet(req, resp)   ──> Checks query params or dashboard |        |
|        +-----------------------------+-------------------------------+        |
|                                      |                                        |
|                                      v                                        |
|        +-------------------------------------------------------------+        |
|        |           Feedback.java (Model Layer)                       |        |
|        |   • Instantiates Feedback Model Bean                        |        |
|        |   • Stores in Collections.synchronizedList<Feedback>()      |        |
|        |   • Generates AtomicInteger IDs & Formatted Timestamps      |        |
|        +-------------------------------------------------------------+        |
+-------------------------------------------------------------------------------+
```

### Detailed Lifecycle Steps:
1. **User Action:** The student inputs data on `index.html` (Full Name, Email, Category, Star Rating, Comments).
2. **Transmission:** Depending on the user's action, the browser initiates an HTTP `POST` or `GET` request directed at `/FeedbackServlet`.
3. **Container Processing:** Apache Tomcat routes the request to `FeedbackServlet` based on `@WebServlet("/FeedbackServlet")` and `web.xml` mappings.
4. **Data Extraction & Modeling:**
   - Character encoding is set to `UTF-8`.
   - Request parameters are validated and mapped to a new `Feedback` model bean.
   - The bean is assigned an auto-incremented ID via `AtomicInteger` and a localized timestamp.
5. **Thread-Safe Persistence:** The bean is saved into `Collections.synchronizedList(new ArrayList<Feedback>())`, maintaining data integrity across concurrent user threads.
6. **Dynamic View Generation:** The servlet's `PrintWriter` produces an HTML5 page containing:
   - A **Success Confirmation Card** with a prominent method badge (`HTTP POST` vs. `HTTP GET`).
   - A **Summary Data Table** containing all feedback entries submitted during the Tomcat session.

---

## ⚖️ Architectural Viva Comparison: HTTP POST vs. HTTP GET

A central objective of this application is demonstrating the functional and security differences between HTTP request methods:

| Feature / Metric | HTTP POST | HTTP GET |
| :--- | :--- | :--- |
| **Payload Location** | Encapsulated inside the **HTTP Request Body**. | Appended directly to the **URL Query String** (`?name=...&email=...`). |
| **Security & Privacy** | **High:** Sensitive data is hidden from URLs, browser history, proxy logs, and referer headers. | **Low:** Parameters are fully exposed in the address bar and stored in browser history/server access logs. |
| **Data Length Limit** | **No theoretical limit** (Container configured; typically megabytes or gigabytes). | **Strictly limited** by web browsers and servers (typically ~2,048 characters). |
| **Idempotence & Caching**| **Non-idempotent:** Not cached by default; prompts a warning if refreshed (*"Confirm Form Resubmission"*). | **Idempotent:** Safe to cache, pre-fetch, and reload without altering server state. |
| **Bookmarkability** | Cannot be bookmarked or shared with submitted form values intact. | URLs can be bookmarked and shared along with query parameters. |
| **Application Usage** | Primary submission method for recording feedback records (`doPost`). | Used for retrieving the summary dashboard and viva demonstration (`doGet`). |

---

## 📂 Project Directory Structure

```
User Feedback Form Application/
├── pom.xml                                  <-- Maven Project Descriptor (Java 17, Jakarta 6.0)
├── README.md                                <-- Executive Documentation
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── college/
        │           └── feedback/
        │               ├── Feedback.java    <-- Serializable Data Model
        │               └── FeedbackServlet.java <-- Request Controller & HTML Renderer
        └── webapp/
            ├── index.html                   <-- Modern UI Form with Dual-Submit Viva Controls
            ├── css/
            │   └── styles.css               <-- Glassmorphism Styling, Animations, Responsive Grid
            └── WEB-INF/
                └── web.xml                  <-- Jakarta EE 6.0 Web Application Deployment Descriptor
```

---

## 🚀 Compilation & Deployment Guide

### Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher (`java -version`)
- **Apache Maven:** Version 3.8+ (`mvn -version`)
- **Apache Tomcat:** Version 10.1.x or higher *(Tomcat 10+ is required for the `jakarta.*` package namespace)*

---

### Step 1: Clean and Build the Project

Open a terminal or command prompt inside the project root folder (`User Feedback Form Application`):

```bash
mvn clean package
```

#### What this command does:
1. `clean`: Deletes previous build artifacts from the `target/` directory.
2. `compile`: Compiles Java classes against the Jakarta Servlet 6.0 API.
3. `package`: Packages the compiled classes, `index.html`, `styles.css`, and `web.xml` into a deployable Web Application Archive (`.war`) file:
   ```
   target/UserFeedbackFormApp.war
   ```

---

### Step 2: Deploy to Apache Tomcat 10+

#### Method A: Direct File Deployment (Recommended)
1. Locate the generated `.war` file:
   ```
   target/UserFeedbackFormApp.war
   ```
2. Copy `UserFeedbackFormApp.war` into your Tomcat installation directory's `webapps/` folder:
   - **Windows:** `C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps\`
   - *(or your custom Tomcat extraction directory, e.g., `D:\apache-tomcat-10.1.x\webapps\`)*
3. **Rename the WAR (Optional):**
   - If you want the URL context path to be `User_Feedback_Form_Application`, rename the WAR file to:
     ```
     User_Feedback_Form_Application.war
     ```
4. Start Apache Tomcat:
   - **Windows (Command Prompt / PowerShell):**
     ```cmd
     cd "path\to\apache-tomcat-10.1.x\bin"
     startup.bat
     ```
   - **Linux / macOS:**
     ```bash
     cd /path/to/apache-tomcat-10.1.x/bin
     ./startup.sh
     ```
5. Tomcat will automatically unpack the WAR into a folder with the same name.

#### Method B: Running via IDE (IntelliJ IDEA / Eclipse / NetBeans)
1. Open the project root folder as a Maven project.
2. Add a new **Run/Debug Configuration** -> **Tomcat Server** -> **Local**.
3. Under the **Deployment** tab, click **+** -> **Artifact** -> choose `UserFeedbackFormApp:war`.
4. Set the **Application context** to `/User_Feedback_Form_Application` or `/UserFeedbackFormApp`.
5. Click **Apply** and **Run**.

---

### Step 3: Accessing the Application

Once Tomcat is running, open any web browser and navigate to:

- **Main Feedback Form:**
  ```
  http://localhost:8080/User_Feedback_Form_Application/
  ```
  *(or `http://localhost:8080/User_Feedback_Form_Application/index.html`)*

- **Live In-Memory Feedback Dashboard:**
  ```
  http://localhost:8080/User_Feedback_Form_Application/FeedbackServlet
  ```

---

## 🧪 Testing Viva Features

1. **Testing POST Mode:**
   - Fill out the form fields.
   - Leave the mode toggle set to **POST (Secure Body)**.
   - Click **Submit via POST**.
   - Notice that the URL in the browser address bar remains clean (`/FeedbackServlet`), and the response displays the purple `HTTP POST` badge.
2. **Testing GET Mode:**
   - Fill out the form fields.
   - Click the mode toggle to **GET (URL Query)** or click **Submit via GET (Viva Demo)**.
   - Notice that the URL appends all form fields as query parameters:
     ```
     /FeedbackServlet?fullName=Naveen&email=student%40college.edu&category=Academics&rating=5&recommend=yes&comments=Excellent+course
     ```
   - The response displays the cyan `HTTP GET` badge.
3. **Inspecting In-Memory Persistence:**
   - Submit multiple feedback entries with different ratings and categories.
   - Notice that each entry is appended to the **Summary Data Table** with a unique ID and timestamp without restarting the server.
   - Access `/FeedbackServlet` directly in another tab to view the live dashboard.

---

## 🛡️ License & Academic Integrity
This project is developed for educational purposes under the **Master of Computer Applications (MCA)** curriculum for the **Advanced Internet Programming** laboratory course.
