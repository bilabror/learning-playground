import { serve } from "bun";
import indexPage from "./index.html";
import { handleTranscribe } from "@/api/controllers/transcribe";
import { handleSummarize } from "@/api/controllers/summarize";

const server = serve({
  port: 3001,
  routes: {
    "/api/transcribe": { POST: handleTranscribe },
    "/api/summarize": { POST: handleSummarize },
    "/*": indexPage,
  },
  development: process.env.NODE_ENV !== "production",
});

console.log(`🚀 Server running at ${server.url}`);
