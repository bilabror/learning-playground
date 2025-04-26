import { speechClient } from '@/lib/speechClient';

export async function handleTranscribe(req) {
  const formData = await req.formData();
  const audioFile = formData.get('audio');
  if (!(audioFile instanceof File)) {
    return new Response('Audio file not found', { status: 400 });
  }

  const arrayBuffer = await audioFile.arrayBuffer();
  const content = Buffer.from(arrayBuffer).toString('base64');

  try {
    const [response] = await speechClient.recognize({
      audio: { content },
      config: {
        encoding: 'WEBM_OPUS',
        sampleRateHertz: 48000,
        languageCode: 'id-ID',
      },
    });

    const transcription = response.results
      ?.map((r) => r.alternatives?.[0].transcript)
      .join('\n');

    return Response.json({
      transcription: transcription || 'No transcription found.',
    });
  } catch (err) {
    console.error('❌ Transcribe error:', err);
    return new Response('Failed to transcribe', { status: 500 });
  }
}
