import { VertexAI } from '@google-cloud/vertexai';
import credentials from '../../credentials.json';

const vertexAI = new VertexAI({
  project: credentials.project_id,
  location: 'us-central1',
});
const MODEL_NAME = 'gemini-2.0-flash-lite-001';
const SYSTEM_INSTRUCTION = {
  role: 'system',
  parts: [
    {
      text: `Kamu adalah asisten medis digital yang bertugas membuat ringkasan hasil konsultasi dokter umum. Sumber informasi berasal dari percakapan antara dokter dan pasien yang ditranskrip dari rekaman audio. Tugasmu: Buat ringkasan diagnosis dalam bahasa Indonesia yang profesional dan mudah dibaca dengan format terstruktur berikut:\n- Identitas Pasien: Usia, jenis kelamin\n- Keluhan Utama: Ringkasan singkat keluhan utama pasien\n- Riwayat Penyakit Sekarang: Gambaran perjalanan penyakit saat ini\n- Objektif: Tanda vital dan pemeriksaan fisik\n- Riwayat Medis: Penyakit terdahulu, alergi, dan pengobatan\n- Diagnosis: Diagnosis dokter\n- Rencana: Pengobatan dan tindak lanjut\n- Edukasi: Saran yang diberikan dokter\n\nCatatan:\nGunakan bahasa netral dan objektif\nTampilkan "Tidak disampaikan" jika informasi tidak ada,`,
    },
  ],
};

export const genAI = vertexAI.preview.getGenerativeModel({
  model: MODEL_NAME,
  generationConfig: { maxOutputTokens: 8192, temperature: 0.2, topP: 0.95 },
  systemInstruction: SYSTEM_INSTRUCTION,
});
