import {
  createContext,
  useContext,
  useState,
  useEffect,
  type ReactNode,
} from 'react';

// Types for our context
interface Recording {
  id: string;
  title: string;
  duration: number;
  blob: Blob;
  url: string;
  timestamp: string;
}

interface Transcription {
  id: string;
  recordingId: string;
  title: string;
  content: string;
  wordCount: number;
  timestamp: string;
  summary?: string;
}

interface RecordingContextType {
  recordings: Recording[];
  transcriptions: Record<string, Transcription>;
  addRecording: (recording: Recording) => void;
  deleteRecording: (id: string) => void;
  addTranscription: (transcription: Transcription) => void;
  updateTranscription: (id: string, updates: Partial<Transcription>) => void;
  deleteTranscription: (id: string) => void;
  getRecordingById: (id: string) => Recording | undefined;
  getTranscriptionById: (id: string) => Transcription | undefined;
}

// Create the context
const RecordingContext = createContext<RecordingContextType | undefined>(
  undefined
);

// Provider component
export function RecordingProvider({ children }: { children: ReactNode }) {
  const [recordings, setRecordings] = useState<Recording[]>([]);
  const [transcriptions, setTranscriptions] = useState<
    Record<string, Transcription>
  >({});

  // Load data from local storage on mount
  useEffect(() => {
    const savedRecordings = localStorage.getItem('mediscribe-recordings');
    const savedTranscriptions = localStorage.getItem(
      'mediscribe-transcriptions'
    );

    if (savedRecordings) {
      try {
        // We need to recreate Blobs since they can't be stored in JSON directly
        const parsedRecordings = JSON.parse(savedRecordings);
        const reconstructedRecordings = parsedRecordings.map((rec: any) => {
          if (
            rec.blob &&
            typeof rec.blob === 'object' &&
            !('size' in rec.blob)
          ) {
            // If the blob was serialized as an object (with type and data properties)
            const arrayBuffer = new Uint8Array(Object.values(rec.blob.data))
              .buffer;
            return {
              ...rec,
              blob: new Blob([arrayBuffer], { type: rec.blob.type }),
              url: URL.createObjectURL(
                new Blob([arrayBuffer], { type: rec.blob.type })
              ),
            };
          }
          return rec;
        });
        setRecordings(reconstructedRecordings);
      } catch (error) {
        console.error('Error loading recordings:', error);
        setRecordings([]);
      }
    }

    if (savedTranscriptions) {
      try {
        setTranscriptions(JSON.parse(savedTranscriptions));
      } catch (error) {
        console.error('Error loading transcriptions:', error);
        setTranscriptions({});
      }
    }
  }, []);

  // Save data to local storage when it changes
  useEffect(() => {
    if (recordings.length > 0) {
      // Need to serialize Blobs for storage
      const serializableRecordings = recordings.map((rec) => {
        if (rec.blob instanceof Blob) {
          // Clone the recording to avoid modifying the original
          const serRec = { ...rec };
          // Store the Blob type
          serRec.blob = {
            type: rec.blob.type,
            data: {}, // Will be filled with array data
          };

          // We'll use FileReader to convert Blob to array buffer
          const reader = new FileReader();
          reader.onload = () => {
            if (reader.result instanceof ArrayBuffer) {
              const array = new Uint8Array(reader.result);
              // Store as object with numeric indices
              for (let i = 0; i < array.length; i++) {
                serRec.blob.data[i] = array[i];
              }
              // Save the updated serializable recordings
              localStorage.setItem(
                'mediscribe-recordings',
                JSON.stringify(serializableRecordings)
              );
            }
          };

          reader.readAsArrayBuffer(rec.blob);
          return serRec;
        }
        return rec;
      });

      // Save immediately with whatever we have (URLs will be recreated on load)
      localStorage.setItem(
        'mediscribe-recordings',
        JSON.stringify(serializableRecordings)
      );
    } else {
      localStorage.setItem('mediscribe-recordings', JSON.stringify([]));
    }
  }, [recordings]);

  useEffect(() => {
    localStorage.setItem(
      'mediscribe-transcriptions',
      JSON.stringify(transcriptions)
    );
  }, [transcriptions]);

  // Context functions
  const addRecording = (recording: Recording) => {
    setRecordings((prev) => [...prev, recording]);
  };

  const deleteRecording = (id: string) => {
    setRecordings((prev) => prev.filter((rec) => rec.id !== id));
    // Also delete associated transcriptions
    const updatedTranscriptions = { ...transcriptions };
    for (const key of Object.keys(updatedTranscriptions)) {
      if (updatedTranscriptions[key].recordingId === id) {
        delete updatedTranscriptions[key];
      }
    }
    setTranscriptions(updatedTranscriptions);
  };

  const addTranscription = (transcription: Transcription) => {
    setTranscriptions((prev) => ({
      ...prev,
      [transcription.id]: transcription,
    }));
  };

  const updateTranscription = (id: string, updates: Partial<Transcription>) => {
    if (transcriptions[id]) {
      setTranscriptions((prev) => ({
        ...prev,
        [id]: {
          ...prev[id],
          ...updates,
        },
      }));
    }
  };

  const deleteTranscription = (id: string) => {
    const updatedTranscriptions = { ...transcriptions };
    delete updatedTranscriptions[id];
    setTranscriptions(updatedTranscriptions);
  };

  const getRecordingById = (id: string) => {
    return recordings.find((rec) => rec.id === id);
  };

  const getTranscriptionById = (id: string) => {
    return transcriptions[id];
  };

  // Context value
  const value = {
    recordings,
    transcriptions,
    addRecording,
    deleteRecording,
    addTranscription,
    updateTranscription,
    deleteTranscription,
    getRecordingById,
    getTranscriptionById,
  };

  return (
    <RecordingContext.Provider value={value}>
      {children}
    </RecordingContext.Provider>
  );
}

// Hook for using the context
export function useRecordings() {
  const context = useContext(RecordingContext);
  if (context === undefined) {
    throw new Error('useRecordings must be used within a RecordingProvider');
  }
  return context;
}
