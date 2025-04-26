import { create } from "zustand";
import createSelectors from "./createSelectors";

type StateAction = {
  // state
  isRecording: boolean;
  isPaused: boolean;
  recordingTime: number;
  recordedBlob: Blob | null;
  audioUrl: string | null;
  isPlaying: boolean;
  playbackProgress: number;
  isTransitioning: boolean;

  // actions
  setIsRecording: (flag: boolean) => void;
  setIsPaused: (flag: boolean) => void;
  incRecordingTime: () => void;
  setRecordingTime: (t: number) => void;
  setRecordedBlob: (b: Blob | null) => void;
  setAudioUrl: (url: string | null) => void;
  setIsPlaying: (flag: boolean) => void;
  togglePlaying: () => void;
  setPlaybackProgress: (p: number) => void;
  setIsTransitioning: (flag: boolean) => void;
};

const base = create<StateAction>((set) => ({
  // initial state
  isRecording: false,
  isPaused: false,
  recordingTime: 0,
  recordedBlob: null,
  audioUrl: null,
  isPlaying: false,
  playbackProgress: 0,
  isTransitioning: false,

  // actions
  setIsRecording: (isRecording) => set({ isRecording }),
  setIsPaused: (isPaused) => set({ isPaused }),
  setRecordingTime: (recordingTime) => set({ recordingTime }),
  incRecordingTime: () => set((state) => ({ recordingTime: state.recordingTime + 1 })),
  setRecordedBlob: (recordedBlob) => set({ recordedBlob }),
  setAudioUrl: (audioUrl) => set({ audioUrl }),
  setIsPlaying: (isPlaying) => set({ isPlaying }),
  togglePlaying: () => set((state) => ({ isPlaying: !state.isPlaying })),
  setPlaybackProgress: (playbackProgress) => set({ playbackProgress }),
  setIsTransitioning: (isTransitioning) => set({ isTransitioning }),
}));

export const useRecordingStore = createSelectors(base);
