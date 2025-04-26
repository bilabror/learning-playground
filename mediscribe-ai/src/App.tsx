import "@/index.css";
import { useEffect } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Icons } from "@/components/icons";
import { Tabs } from "@/components/ui/tabs";
import { FeatureIcon } from "@/components/featureIcon";
import { GRADIENT_PRIMARY, ICON_SIZE_MD } from "@/constants";
import { TabNavigation } from "@/components/tabNavigation";
import { RecordingTabContent } from "@/components/recordingTabContent";
import { PlaybackTabContent } from "@/components/playbackTabContent";
import { TranscriptTabContent } from "@/components/transcriptTabContent";
import { SummaryTabContent } from "@/components/summaryTabContent";
import { useAudioRecorder } from "@/hooks/useAudioRecorder";
import { useActiveTabStore } from "@/store/useActiveTabStore";

export function App() {
  const { startRecording, pauseRecording, resumeRecording, stopRecording, handleNewRecording } = useAudioRecorder();
  const { activeTab, setActiveTab } = useActiveTabStore();

  useEffect(() => {
    document.documentElement.classList.add("dark");
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-900 to-gray-950 flex items-center justify-center px-4 py-12">
      <Card className="shadow-xl border-none max-w-3xl w-full bg-gray-900/90 backdrop-blur-sm">
        <CardHeader className="text-center pb-6">
          <FeatureIcon icon={<Icons.mic className={ICON_SIZE_MD} />} className="mx-auto mb-4 h-14 w-14" />
          <CardTitle className={`text-4xl font-bold bg-gradient-to-r ${GRADIENT_PRIMARY} bg-clip-text text-transparent`}>MediScribe AI</CardTitle>
          <CardDescription className="text-lg mt-2">Record, transcribe and summarize patient conversations</CardDescription>
        </CardHeader>
        <CardContent className="pb-8 px-6">
          <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
            <TabNavigation />
            <RecordingTabContent
              stopRecording={stopRecording}
              startRecording={startRecording}
              resumeRecording={resumeRecording}
              pauseRecording={pauseRecording}
              handleNewRecording={handleNewRecording}
            />

            <PlaybackTabContent />
            <TranscriptTabContent />
            <SummaryTabContent handleNewRecording={handleNewRecording} />
          </Tabs>
        </CardContent>
      </Card>
    </div>
  );
}

export default App;
