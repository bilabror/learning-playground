# 🩺 MediScribe AI

> Record, transcribe, and summarize patient conversations — learning playground edition.

---

## 🚀 Features

- **Real-time Recording**
  Capture audio in-browser (WEBM/OPUS).
- **Accurate Transcription**
  Google Cloud Speech-to-Text for Indonesian audio.
- **AI-Powered Summaries**
  Vertex AI’s Gemini model generates structured medical reports in Bahasa Indonesia.

---

## 🛠 Tech Stack

- **Runtime:** [Bun](https://bun.sh/)
- **Speech-to-Text:** [@google-cloud/speech](https://www.npmjs.com/package/@google-cloud/speech)
- **AI Summarization:** [@google-cloud/vertexai](https://www.npmjs.com/package/@google-cloud/vertexai) (Gemini 2.0 Flash Lite)
- **Frontend:** [React 19](https://react.dev/), [Tailwindcss 4](https://tailwindcss.com/), [shandcn UI](https://ui.shadcn.com/)

---

## 📝 Getting Started

### 1. Prerequisites

- **Bun** ≥ 1.2.x
- A **Google Cloud** project with:
  - Speech-to-Text API enabled
  - Vertex AI API enabled

### 2. Install dependencies

```bash
bun install
```

### 3. Environment Variables

Create `.env`, you can copy it from `.env.example`

```
GOOGLE_APPLICATION_CREDENTIALS=/path/to/service-account.json
```

### 4. Run the app

```
bun dev
```

---

## 📚 What I Learned

- [x] Cloud Speech: Configuring and using Google Speech-to-Text in Bun.
- [x] Vertex AI: Using Gemini-2.0 for structured medical summaries.
- [x] Zustand: Using it for state management.
- [x] i18n: Using it for internationalization
