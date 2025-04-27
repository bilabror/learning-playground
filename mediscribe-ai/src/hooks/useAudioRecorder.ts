import { useActiveTabStore } from "@/store/useActiveTabStore";
import { useRecordingStore } from "@/store/useRecordingStore";
import { useSummaryStore } from "@/store/useSummaryStore";
import { useTranscriptStore } from "@/store/useTranscriptStore";
import { useRef, useEffect } from "react";
import { toast } from "sonner";
import { useTranslation } from "react-i18next";

export function useAudioRecorder() {
  const { t } = useTranslation();
  const {
    audioUrl,

    setIsRecording,
    setIsPaused,
    incRecordingTime,
    setRecordingTime,
    setRecordedBlob,
    setAudioUrl,
    setIsTransitioning,
    setPlaybackProgress,
  } = useRecordingStore();
  const { setTranscript } = useTranscriptStore();
  const { setSummary } = useSummaryStore();
  const { setActiveTab } = useActiveTabStore();

  const timerRef = useRef<number | NodeJS.Timeout>(undefined);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);
  const audioChunksRef = useRef<Blob[]>([]);

  // cleanup on unmount or when URL changes
  useEffect(() => {
    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
      if (audioUrl) URL.revokeObjectURL(audioUrl);
    };
  }, [audioUrl]);

  const startRecording = async () => {
    setIsTransitioning(true);
    await new Promise((res) => setTimeout(res, 300));

    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      const recorder = new MediaRecorder(stream);
      mediaRecorderRef.current = recorder;
      audioChunksRef.current = [];

      recorder.ondataavailable = (e) => {
        if (e.data.size > 0) audioChunksRef.current.push(e.data);
      };
      recorder.onstop = () => {
        const blob = new Blob(audioChunksRef.current, { type: "audio/wav" });
        setRecordedBlob(blob);
        setAudioUrl(URL.createObjectURL(blob));
        stream.getTracks().forEach((t) => t.stop());
      };

      recorder.start();
      setIsRecording(true);
      setIsPaused(false);
      setRecordingTime(0);
      timerRef.current = setInterval(() => incRecordingTime(), 1000);
      toast(t("record:alert.started"));
    } catch (err) {
      console.error(err);
      toast.error(t("record:alert.error.cloudNotAccessMic"));
    } finally {
      setIsTransitioning(false);
    }
  };

  const pauseRecording = () => {
    if (mediaRecorderRef.current?.state === "recording") {
      mediaRecorderRef.current.pause();
      if (timerRef.current) clearInterval(timerRef.current);
      setIsPaused(true);
      toast(t("record:alert.paused"));
    }
  };

  const resumeRecording = () => {
    if (mediaRecorderRef.current?.state === "paused") {
      mediaRecorderRef.current.resume();
      timerRef.current = setInterval(() => incRecordingTime(), 1000);
      setIsPaused(false);
      toast(t("record:alert.resumed"));
    }
  };

  const stopRecording = () => {
    mediaRecorderRef.current?.stop();
    if (timerRef.current) clearInterval(timerRef.current);
    setIsRecording(false);
    setIsPaused(false);
    toast(t("record:alert.completed"));
  };

  const handleNewRecording = () => {
    setTranscript("");
    setSummary("");
    setPlaybackProgress(0);
    setActiveTab("record");
    setRecordedBlob(null);
    setAudioUrl(null);
    setRecordingTime(0);
    setIsTransitioning(true);
    setTimeout(() => setIsTransitioning(false), 300);
  };

  return {
    startRecording,
    pauseRecording,
    resumeRecording,
    stopRecording,
    handleNewRecording,
  };
}
