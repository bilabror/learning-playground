import { create } from "zustand";
import createSelectors from "./createSelectors";

type StateAction = {
  // state
  activeTab: string;

  // actions
  setActiveTab: (text: string) => void;
};

const base = create<StateAction>((set) => ({
  // initial state
  activeTab: "record",

  // actions
  setActiveTab: (activeTab) => set({ activeTab }),
}));

export const useActiveTabStore = createSelectors(base);
