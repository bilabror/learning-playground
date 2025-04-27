import { GRADIENT_PRIMARY, GRADIENT_RECORDING, GRADIENT_SUCCESS, ICON_SIZE_LG, ICON_SIZE_SM, ICON_SIZE_XL } from "@/constants";
import { Icons } from "./icons";
import { Button } from "./ui/button";
import { TabsContent } from "./ui/tabs";
import { cn, formatTime } from "@/lib/utils";
import { useRecordingStore } from "@/store/useRecordingStore";
import { useActiveTabStore } from "@/store/useActiveTabStore";
import { useTranslation } from "react-i18next";

export const RecordingTabContent = ({ startRecording, resumeRecording, pauseRecording, stopRecording, handleNewRecording }) => {
  const { t } = useTranslation();
  const { recordingTime, isRecording, recordedBlob, isTransitioning, isPaused } = useRecordingStore();
  const { setActiveTab } = useActiveTabStore();

  // wrap stop to also switch tab
  const handleStop = () => {
    stopRecording();
    setActiveTab("playback");
  };

  return (
    <TabsContent value="record" className="focus-visible:outline-none">
      <div className="flex flex-col items-center justify-center py-12 space-y-8">
        {!isRecording && !recordedBlob ? (
          <>
            <div
              className={cn("relative group cursor-pointer transform transition-all duration-300", isTransitioning && "scale-90 opacity-0")}
              onClick={startRecording}
            >
              <div className="absolute inset-0 rounded-full bg-gradient-to-r from-blue-500 to-indigo-600 blur-md opacity-50 group-hover:opacity-75 transition-opacity animate-pulse"></div>
              <div className="relative rounded-full bg-gradient-to-br from-blue-600 to-indigo-600 p-10 shadow-xl group-hover:shadow-blue-500/30 transition-all">
                <Icons.mic className={ICON_SIZE_XL} />
              </div>
            </div>
            <Button
              size="lg"
              onClick={startRecording}
              disabled={isTransitioning}
              className={cn(
                `gap-3 px-8 py-6 rounded-full text-lg bg-gradient-to-r ${GRADIENT_PRIMARY} hover:shadow-lg hover:shadow-blue-500/30 transition-all duration-300 text-white cursor-pointer`,
                isTransitioning && "opacity-0 transform translate-y-4"
              )}
            >
              <Icons.mic className={ICON_SIZE_SM} />
              {isTransitioning ? t("record:starting") : t("record:start")}
            </Button>
          </>
        ) : isRecording ? (
          <div className="flex flex-col items-center space-y-8">
            <div className="relative">
              <div className="absolute -inset-4 rounded-full bg-red-500/20 animate-pulse"></div>
              <div className="absolute -inset-8 rounded-full bg-red-500/10 animate-pulse" style={{ animationDelay: "0.5s" }}></div>
              <div className="absolute -inset-12 rounded-full bg-red-500/5 animate-pulse" style={{ animationDelay: "1s" }}></div>
              <div className="relative p-12 rounded-full bg-gradient-to-br from-red-500 to-red-600 shadow-lg shadow-red-500/30 transform transition-all duration-500 animate-[fadeIn_0.5s_ease-out_forwards]">
                <Icons.mic className={ICON_SIZE_XL} />
              </div>
            </div>

            <div className="text-center">
              <p className={`text-5xl font-bold bg-gradient-to-r ${GRADIENT_RECORDING} bg-clip-text text-transparent`}>{formatTime(recordingTime)}</p>
              <p className="text-lg text-muted-foreground mt-2">{isPaused ? t("record:alert.paused") : t("record:inProgress")}</p>
            </div>

            <div className="flex flex-wrap justify-center gap-3">
              {isPaused ? (
                <Button
                  onClick={resumeRecording}
                  size="lg"
                  className={`gap-2 rounded-full px-6 bg-gradient-to-r ${GRADIENT_SUCCESS} hover:shadow-lg hover:shadow-green-500/30 transition-all`}
                >
                  <Icons.play className={ICON_SIZE_SM} /> {t("record:resume")}
                </Button>
              ) : (
                <Button
                  onClick={pauseRecording}
                  variant="outline"
                  size="lg"
                  className="gap-2 rounded-full px-6 border-2 hover:shadow-lg transition-all"
                >
                  <Icons.pause className={ICON_SIZE_SM} /> {t("record:pause")}
                </Button>
              )}
              <Button
                onClick={handleStop}
                variant="destructive"
                size="lg"
                className={`gap-2 rounded-full px-6 bg-gradient-to-r ${GRADIENT_RECORDING} hover:shadow-lg hover:shadow-red-500/30 transition-all`}
              >
                <Icons.square className={ICON_SIZE_SM} /> {t("record:stop")}
              </Button>
            </div>
          </div>
        ) : (
          <div className="text-center space-y-6 py-4">
            <div className="rounded-full mx-auto bg-green-900/30 p-5 w-20 h-20 flex items-center justify-center">
              <Icons.check className={ICON_SIZE_LG} />
            </div>
            <div>
              <p className="text-2xl font-medium">{t("record:alert.completed")}</p>
              <p className="text-muted-foreground mt-2">
                {t("record:duration")}: {formatTime(recordingTime)}
              </p>
            </div>
            <div className="flex flex-wrap justify-center gap-3">
              <Button onClick={handleNewRecording} variant="outline" className="gap-2 rounded-full px-6 border-2 cursor-pointer">
                <Icons.refresh className="h-4 w-4" /> {t("record:new")}
              </Button>
              <Button
                onClick={() => setActiveTab("playback")}
                className={`gap-2 rounded-full px-6 bg-gradient-to-r ${GRADIENT_PRIMARY} cursor-pointer text-white`}
              >
                <Icons.play className="h-4 w-4" /> {t("record:listen")}
              </Button>
            </div>
          </div>
        )}
      </div>
    </TabsContent>
  );
};
