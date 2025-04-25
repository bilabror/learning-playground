import { GRADIENT_PRIMARY, ICON_SIZE_LG, ICON_SIZE_SM } from '@/constants';
import { FeatureIcon } from './featureIcon';
import { Icons } from './icons';
import { TabsContent } from './ui/tabs';
import { Button } from './ui/button';

const EXAMPLE_TRANSCRIPT = `selamat pagi silakan duduk dulu ya nama saya dokter adi oh iya selamat pagi dok makasih siapa nama lengkap bapak ya mau dicatat dulu di rekam medis nama saya budi santoso dok umur 35 tahun oh baik pak budi boleh cerita apa yang dirasakan hari ini udah tiga hari ini saya batuk terus dok rasanya tenggorokan sakit banget apalagi kalau malam jadi susah tidur gitu batuknya gimana pak kering atau ada dahaknya awalnya sih kering dok tapi dari kemarin udah mulai ada dahaknya warnanya putih kadang agak kuning gitu hmm ada keluhan lain nggak misal kayak demam pilek atau kepala pusing ada dok rasanya badan agak panas terutama malam hari hidung juga sering mampet terus kadang kepala pusing badan juga lemes rasanya nggak enak banget kapan pertama kali mulai ngerasa nggak enak gitu pak kira-kira sekitar empat hari yang lalu dok mulai ngerasa nggak enak badan terus besoknya langsung batuk-batuk gitu pak budi punya riwayat penyakit tertentu nggak kayak asma kencing manis atau darah tinggi nggak ada dok cuma saya emang gampang kena flu sih biasanya dua tiga kali setahun pasti kena ada alergi obat tertentu nggak pak setau saya sih nggak ada dok oke pak budi saya periksa dulu ya coba saya ukur suhu badannya dulu oh ini 38,2 derajat celsius tekanan darahnya 120/80 masih normal ya coba saya lihat tenggorokannya buka mulut dan bilang aaaa aaaa hmm iya tenggorokannya merah dan agak bengkak sekarang tarik napas yang dalam lalu keluarkan pelan-pelan dari mulut ya ok baik berdasarkan pemeriksaan yang saya lakukan pak budi kena radang tenggorokan kayaknya disebabkan infeksi virus gejala-gejala lain seperti demam batuk sama hidung tersumbat menunjukkan infeksi saluran napas bagian atas jadi perlu antibiotik nggak dok untuk sekarang belum perlu antibiotik karena kemungkinan penyebabnya virus bukan bakteri saya kasih resep obat untuk meredakan gejalanya aja parasetamol untuk demam dan nyeri obat dekongestan buat hidung tersumbat sama obat batuk berapa lama ya dok sampai saya sembuh total biasanya sih sekitar 5-7 hari gejala mulai membaik kalau setelah 5 hari nggak ada perbaikan atau malah tambah parah sebaiknya kontrol lagi ke sini ya oke dok ada saran lain nggak usahakan istirahat yang cukup ya pak terus banyak minum air putih hangat jangan makan atau minum yang terlalu dingin terus kalau demam tinggi di atas 39 derajat atau tiba-tiba sesak napas langsung ke IGD aja ya jangan ditunda-tunda baik dok makasih banyak sama-sama pak budi ini resepnya bisa ditebus di apotek di depan semoga cepat sembuh ya oh iya dok umur saya 35 tahun laki-laki kerja di perusahaan swasta apa itu perlu dicatat iya sudah saya catat kok pak budi tadi di awal terima kasih informasinya pak semoga cepat sembuh ya makasih dok`;

export const TranscriptTabContent = ({
  transcript,
  setTranscript,
  handleSummary,
}) => (
  <TabsContent value="transcript" className="focus-visible:outline-none">
    <div className="space-y-8 py-8">
      <div className="flex justify-center mb-4">
        <FeatureIcon
          icon={<Icons.fileText className={ICON_SIZE_LG} />}
          className="w-20 h-20"
        />
      </div>

      <div className="flex justify-start mt-6">
        <Button
          onClick={() => setTranscript(EXAMPLE_TRANSCRIPT)}
          className={`cursor-pointer gap-2 px-8 py-6 text-lg rounded-full bg-gradient-to-r ${GRADIENT_PRIMARY} hover:shadow-lg hover:shadow-blue-500/30 transition-all text-white`}
        >
          <Icons.clipboardPaste className={ICON_SIZE_SM} /> Use example
          transcript
        </Button>
      </div>

      <div className="relative">
        <div className="absolute -inset-0.5 bg-gradient-to-r from-blue-500/20 to-indigo-600/20 rounded-xl blur"></div>
        <textarea
          className="w-full min-h-56 p-6 border border-gray-700 rounded-xl resize-none bg-gray-800 shadow-inner relative z-10 focus:ring-2 focus:ring-blue-500/30 focus:outline-none text-lg"
          value={transcript}
          onChange={(e) => setTranscript(e.target.value)}
          placeholder="Your transcript will appear here..."
        />
      </div>

      <div className="flex justify-center mt-6">
        <Button
          onClick={handleSummary}
          className={`cursor-pointer gap-2 px-8 py-6 text-lg rounded-full bg-gradient-to-r ${GRADIENT_PRIMARY} hover:shadow-lg hover:shadow-blue-500/30 transition-all text-white`}
        >
          <Icons.clipboardList className={ICON_SIZE_SM} /> Generate Summary
        </Button>
      </div>
    </div>
  </TabsContent>
);
