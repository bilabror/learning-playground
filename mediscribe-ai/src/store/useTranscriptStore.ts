import { create } from "zustand";
import createSelectors from "./createSelectors";

type StateAction = {
  // state
  transcript: string;

  // actions
  setTranscript: (text: string) => void;
};

const base = create<StateAction>((set) => ({
  // initial state
  transcript: "",

  // actions
  setTranscript: (transcript) => set({ transcript }),
}));

export const useTranscriptStore = createSelectors(base);
