import { genAI } from '@/lib/chatClient';

export async function handleSummarize(req) {
  const { text } = await req.json();
  if (!text) {
    return new Response('Text not found', { status: 400 });
  }

  const streamResult = await genAI.generateContent(text);
  const summary = streamResult.response.candidates[0].content.parts[0].text;

  return Response.json({ text: summary });
}
