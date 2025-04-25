import { TabsList, TabsTrigger } from './ui/tabs';

export const TabNavigation = ({ recordedBlob, transcript, summary }) => (
  <div className="flex justify-center w-full mb-8">
    <TabsList className="p-1 bg-gray-800/50 rounded-xl w-auto">
      <TabsTrigger
        value="record"
        className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 cursor-pointer`}
      >
        Record
      </TabsTrigger>
      <TabsTrigger
        value="playback"
        disabled={!recordedBlob}
        className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 ${
          recordedBlob && `cursor-pointer`
        }`}
      >
        Playback
      </TabsTrigger>
      <TabsTrigger
        value="transcript"
        className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 ${
          transcript && `cursor-pointer`
        }`}
      >
        Transcript
      </TabsTrigger>
      <TabsTrigger
        value="summary"
        disabled={!summary}
        className={`data-[state=active]:shadow-md data-[state=active]:bg-gray-700 rounded-lg px-6 ${
          summary && `cursor-pointer`
        }`}
      >
        Summary
      </TabsTrigger>
    </TabsList>
  </div>
);
