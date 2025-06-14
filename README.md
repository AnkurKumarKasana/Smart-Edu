# 📚 Smart Edu – Empowering Digital Learning

Smart Edu is a powerful, modern Android app built to transform the e-learning experience for students. With smooth UI, engaging course content, AI chatbot support, and Firebase integration, it's a complete smart education platform designed for learning on the go.

![Smart Edu Banner](https://imgur.com/YOUR_BANNER_IMAGE) <!-- Optional: Replace with your image -->

---

## 🚀 Features

### 🎯 Core Modules:
- 🔐 **Firebase Authentication** – Secure login/sign-up for users
- 🏠 **HomeFragment** – Displays welcome message, featured courses, and AI assistant
- 📊 **DashboardFragment** – Shows enrolled courses
- 🔔 **NotificationFragment** – Displays user-specific notifications with relative time (e.g., "1 min ago")
- 🔁 **Forgot Password Screen** – Stylish UI with email recovery and animation

### 📚 Course Details:
- 🧠 Courses like Python, C++, Java, Android, English, AI/ML, Cloud Computing
- 🎥 **Video Lectures** – Hosted on Cloudinary, played in-app with custom video player and watch progress bar
- 📄 **PDF Library** – WebView-based PDF viewer for course materials
- 🤖 **AI Assistant Chatbot** – Gemini API-powered chatbot with Instagram-style chat layout

### 🙋 Doubt Section:
- Students can submit doubts via a form
- Submissions sent directly to admin's Gmail using background email API

---

## 🧠 Tech Stack

| Layer           | Tools/Technologies                                       |
|----------------|-----------------------------------------------------------|
| Language        | Kotlin, XML                                              |
| Backend         | Firebase (Auth, Firestore, Realtime DB), Cloudinary      |
| AI              | Gemini API (Chat Assistant)                              |
| UI/UX           | Material Design, RecyclerView, CardViews, Animations     |
| Video Player    | ExoPlayer (for Cloudinary), Progress Tracker             |
| PDF Viewer      | WebView with Google Drive links                          |
| Notifications   | Firestore-based with timestamps                          |

---

## 🛠️ Project Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/AnkurKumarKasana/Smart-Edu.git
   cd Smart-Edu
