import { ICON_SIZE_LG, ICON_SIZE_SM } from '@/constants';
import { FeatureIcon } from './featureIcon';
import { Icons } from './icons';
import { Button } from './ui/button';
import { TabsContent } from './ui/tabs';
import Markdown from 'react-markdown';

export const SummaryTabContent = ({ summary, handleNewRecording }) => (
  <TabsContent value="summary" className="focus-visible:outline-none">
    <div className="space-y-8 py-8">
      <div className="flex justify-center mb-4">
        <FeatureIcon
          icon={<Icons.clipboardCheck className={ICON_SIZE_LG} />}
          className="w-20 h-20"
        />
      </div>

      <div className="relative">
        <div className="absolute -inset-0.5 bg-gradient-to-r from-blue-500/20 to-indigo-600/20 rounded-xl blur"></div>
        <div className="bg-gray-800 rounded-xl p-6 shadow-inner min-h-56 text-lg relative z-10 border border-gray-700">
          <Markdown>{summary}</Markdown>
        </div>
      </div>

      <div className="flex justify-center mt-6">
        <Button
          onClick={handleNewRecording}
          variant="outline"
          className="cursor-pointer gap-2 px-8 py-6 text-lg rounded-full border-2 hover:shadow-lg transition-all"
        >
          <Icons.refresh className={ICON_SIZE_SM} /> Start New Session
        </Button>
      </div>
    </div>
  </TabsContent>
);
