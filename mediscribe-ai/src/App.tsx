import '@/index.css';
import { useEffect, useRef, useState } from 'react';
import { toast } from 'sonner';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Icons } from '@/components/icons';
import { Tabs } from '@/components/ui/tabs';
import { FeatureIcon } from '@/components/featureIcon';
import { GRADIENT_PRIMARY, ICON_SIZE_MD } from '@/constants';
import { TabNavigation } from '@/components/tabNavigation';
import { RecordingTabContent } from '@/components/recordingTabContent';
import { PlaybackTabContent } from '@/components/playbackTabContent';
import { TranscriptTabContent } from '@/components/transcriptTabContent';
import { SummaryTabContent } from '@/components/summaryTabContent';
import { useAudioRecorder } from '@/hooks/useAudioRecorder';

export function App() {
  const {
    isRecording,
    isPaused,
    isTransitioning,
    recordingTime,
    recordedBlob,
    audioUrl,
    startRecording,
    pauseRecording,
    resumeRecording,
    stopRecording,
    handleNewRecording,
  } = useAudioRecorder();

  const [isPlaying, setIsPlaying] = useState(false);
  const [playbackProgress, setPlaybackProgress] = useState(0);
  const [transcript, setTranscript] = useState('');
  const [summary, setSummary] = useState('');
  const [activeTab, setActiveTab] = useState('record');

  const audioRef = useRef<HTMLAudioElement>(null);

  useEffect(() => {
    document.documentElement.classList.add('dark');
  }, []);

  useEffect(() => {
    if (audioRef.current && isPlaying) {
      const update = () => {
        const dur = audioRef.current!.duration;
        const prog = dur ? (audioRef.current!.currentTime / dur) * 100 : 0;
        setPlaybackProgress(prog);
      };
      audioRef.current.addEventListener('timeupdate', update);
      return () => audioRef.current?.removeEventListener('timeupdate', update);
    }
  }, [isPlaying]);

  // wrap stop to also switch tab
  const handleStop = () => {
    stopRecording();
    setActiveTab('playback');
  };

  const handleTranscribe = async () => {
    if (!recordedBlob) {
      toast.error('No recording found', {
        description: 'Please make a recording first.',
      });
      return;
    }

    try {
      const formData = new FormData();
      formData.append('audio', recordedBlob, 'recording.webm');

      const res = await fetch('/api/transcribe', {
        method: 'POST',
        body: formData,
      });

      const result = await res.json();
      setTranscript(result.transcription);
      toast('Transcript created');
      setActiveTab('transcript');
    } catch (error) {
      toast.error('Transcription failed', {
        description: error.message,
      });
    }
  };

  const handleSummary = async () => {
    if (!transcript) {
      toast.error('No transcription found', {
        description: 'Please generate a transcription first.',
      });
      return;
    }

    try {
      const res = await fetch('/api/summarize', {
        method: 'POST',
        body: JSON.stringify({
          text: transcript,
        }),
      });

      const result = await res.json();
      setSummary(result?.text);
      toast('Summary created');
      setActiveTab('summary');
    } catch (error) {
      toast.error('Error generating summary', {
        description: error.message,
      });
    }
  };

  const handlePlayPause = () => {
    if (!audioRef.current) return;
    if (isPlaying) audioRef.current.pause();
    else audioRef.current.play();
    setIsPlaying((p) => !p);
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-900 to-gray-950 flex items-center justify-center px-4 py-12">
      <Card className="shadow-xl border-none max-w-3xl w-full bg-gray-900/90 backdrop-blur-sm">
        <CardHeader className="text-center pb-6">
          <FeatureIcon
            icon={<Icons.mic className={ICON_SIZE_MD} />}
            className="mx-auto mb-4 h-14 w-14"
          />
          <CardTitle
            className={`text-4xl font-bold bg-gradient-to-r ${GRADIENT_PRIMARY} bg-clip-text text-transparent`}
          >
            MediScribe AI
          </CardTitle>
          <CardDescription className="text-lg mt-2">
            Record, transcribe and summarize patient conversations
          </CardDescription>
        </CardHeader>
        <CardContent className="pb-8 px-6">
          <Tabs
            value={activeTab}
            onValueChange={setActiveTab}
            className="w-full"
          >
            <TabNavigation
              recordedBlob={recordedBlob}
              transcript={transcript}
              summary={summary}
            />

            <RecordingTabContent
              isRecording={isRecording}
              recordedBlob={recordedBlob}
              isTransitioning={isTransitioning}
              startRecording={startRecording}
              resumeRecording={resumeRecording}
              pauseRecording={pauseRecording}
              stopRecording={handleStop}
              handleNewRecording={() => {
                setTranscript('');
                setSummary('');
                setPlaybackProgress(0);
                setActiveTab('record');
                handleNewRecording();
              }}
              setActiveTab={setActiveTab}
              recordingTime={recordingTime}
              isPaused={isPaused}
            />

            <PlaybackTabContent
              recordedBlob={recordedBlob}
              isPlaying={isPlaying}
              handlePlayPause={handlePlayPause}
              recordingTime={recordingTime}
              playbackProgress={playbackProgress}
              audioRef={audioRef}
              audioUrl={audioUrl}
              handleTranscribe={handleTranscribe}
              setIsPlaying={setIsPlaying}
            />

            <TranscriptTabContent
              transcript={transcript}
              setTranscript={setTranscript}
              handleSummary={handleSummary}
            />

            <SummaryTabContent
              summary={summary}
              handleNewRecording={() => {
                setTranscript('');
                setSummary('');
                setPlaybackProgress(0);
                setActiveTab('record');
                handleNewRecording();
              }}
            />
          </Tabs>
        </CardContent>
      </Card>
    </div>
  );
}

export default App;
