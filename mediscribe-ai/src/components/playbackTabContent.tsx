import { GRADIENT_PRIMARY, ICON_SIZE_LG, ICON_SIZE_SM } from "@/constants";
import { FeatureIcon } from "./featureIcon";
import { Icons } from "./icons";
import { Button } from "./ui/button";
import { TabsContent } from "./ui/tabs";
import { cn, formatTime } from "@/lib/utils";
import { Progress } from "./ui/progress";
import { useRecordingStore } from "@/store/useRecordingStore";
import { toast } from "sonner";
import { useActiveTabStore } from "@/store/useActiveTabStore";
import { useTranscriptStore } from "@/store/useTranscriptStore";
import { useEffect, useRef } from "react";
import { useTranslation } from "react-i18next";

export const PlaybackTabContent = () => {
  const { t } = useTranslation();
  const { recordingTime, recordedBlob, audioUrl, isPlaying, playbackProgress, setIsPlaying, togglePlaying, setPlaybackProgress } =
    useRecordingStore();
  const { setActiveTab } = useActiveTabStore();
  const { setTranscript } = useTranscriptStore();

  const audioRef = useRef<HTMLAudioElement>(null);

  useEffect(() => {
    if (audioRef.current && isPlaying) {
      const update = () => {
        const dur = audioRef.current!.duration;
        const prog = dur ? (audioRef.current!.currentTime / dur) * 100 : 0;
        setPlaybackProgress(prog);
      };
      audioRef.current.addEventListener("timeupdate", update);
      return () => audioRef.current?.removeEventListener("timeupdate", update);
    }
  }, [isPlaying]);

  const handlePlayPause = () => {
    if (!audioRef.current) return;
    if (isPlaying) audioRef.current.pause();
    else audioRef.current.play();
    togglePlaying();
  };

  const handleTranscribe = async () => {
    if (!recordedBlob) {
      toast.error(t("transcript:alert.error.recordingNotFound.title"), {
        description: t("transcript:alert.error.recordingNotFound.description"),
      });
      return;
    }

    try {
      const formData = new FormData();
      formData.append("audio", recordedBlob, "recording.webm");

      const res = await fetch("/api/transcribe", {
        method: "POST",
        body: formData,
      });

      const result = await res.json();
      setTranscript(result.transcription);
      toast("Transcript created");
      setActiveTab("transcript");
    } catch (error) {
      console.error(error);
      toast.error(t("transcript:alert.error.transcriptError.title"), {
        description: t("transcript:alert.error.transcriptError.description"),
      });
    }
  };

  return (
    <TabsContent value="playback" className="focus-visible:outline-none">
      {recordedBlob && (
        <div className="space-y-8 py-8">
          <div className="flex justify-center mb-6">
            <FeatureIcon icon={<Icons.headphones className={ICON_SIZE_LG} />} className="w-20 h-20" />
          </div>

          <div className="w-full max-w-md mx-auto bg-gray-800/40 p-6 rounded-xl shadow-inner">
            <div className="flex items-center space-x-4 mb-4">
              <Button
                variant={isPlaying ? "default" : "outline"}
                size="icon"
                onClick={handlePlayPause}
                className={cn(
                  "h-14 w-14 rounded-full border-2 transition-all flex-shrink-0",
                  isPlaying ? `bg-gradient-to-r ${GRADIENT_PRIMARY} shadow-md shadow-blue-500/20` : "hover:border-blue-500"
                )}
              >
                {isPlaying ? <Icons.pause className="h-6 w-6 text-white" /> : <Icons.play className="h-6 w-6" />}
              </Button>
              <div className="flex-1">
                <div className="flex justify-between text-sm font-medium">
                  <span>{formatTime(isPlaying && audioRef.current ? audioRef.current.currentTime : 0)}</span>
                  <span>{formatTime(recordingTime)}</span>
                </div>
                <div className="relative mt-2">
                  <div className="absolute inset-0 bg-gradient-to-r from-blue-500/30 to-indigo-600/30 rounded-full blur-sm"></div>
                  <Progress value={playbackProgress} className="h-3 rounded-full bg-gray-700 relative z-10" />
                </div>
              </div>
            </div>
            <audio ref={audioRef} src={audioUrl || undefined} onEnded={() => setIsPlaying(false)} className="hidden" />
          </div>

          <div className="flex justify-center mt-10">
            <Button
              onClick={handleTranscribe}
              className={`cursor-pointer gap-2 px-8 py-6 text-lg rounded-full bg-gradient-to-r ${GRADIENT_PRIMARY} hover:shadow-lg hover:shadow-blue-500/30 transition-all text-white`}
            >
              <Icons.fileText className={ICON_SIZE_SM} /> {t("transcript:create")}
            </Button>
          </div>
        </div>
      )}
    </TabsContent>
  );
};
