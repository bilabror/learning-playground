import { useSummaryStore } from "@/store/useSummaryStore";
import { TabsList, TabsTrigger } from "./ui/tabs";
import { useRecordingStore } from "@/store/useRecordingStore";
import { useTranscriptStore } from "@/store/useTranscriptStore";
import { useTranslation } from "react-i18next";

export const TabNavigation = () => {
  const { t } = useTranslation();
  const { summary } = useSummaryStore();
  const { transcript } = useTranscriptStore();
  const { recordedBlob } = useRecordingStore();
  return (
    <div className="flex justify-center w-full mb-8">
      <TabsList className="p-1 bg-gray-800/50 rounded-xl w-auto">
        <TabsTrigger value="record" className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 cursor-pointer`}>
          {t("app:tabs.record")}
        </TabsTrigger>
        <TabsTrigger
          value="playback"
          disabled={!recordedBlob}
          className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 ${recordedBlob && `cursor-pointer`}`}
        >
          {t("app:tabs.playback")}
        </TabsTrigger>
        <TabsTrigger
          value="transcript"
          className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 ${transcript && `cursor-pointer`}`}
        >
          {t("app:tabs.transcript")}
        </TabsTrigger>
        <TabsTrigger
          value="summary"
          disabled={!summary}
          className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 ${summary && `cursor-pointer`}`}
        >
          {t("app:tabs.summary")}
        </TabsTrigger>
      </TabsList>
    </div>
  );
};
