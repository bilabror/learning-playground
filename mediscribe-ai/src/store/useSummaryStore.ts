import { create } from "zustand";
import createSelectors from "./createSelectors";

type StateAction = {
  // state
  summary: string;

  // actions
  setSummary: (text: string) => void;
};

const base = create<StateAction>((set) => ({
  // initial state
  summary: "",

  // actions
  setSummary: (summary) => set({ summary }),
}));

export const useSummaryStore = createSelectors(base);
