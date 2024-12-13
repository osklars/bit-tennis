'use client';

import {VisionEvent} from "@/lib/types";

export default function SimulatorPage() {
    const sendEvent = async (type: VisionEvent) => {
        await fetch('/api/events', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ type, timestamp: Date.now() }),
        });
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Vision Event Simulator</h1>
            <div className="grid grid-cols-2 gap-4">
                <div>
                    <h2 className="font-bold mb-2">Player A</h2>
                    <button
                        onClick={() => sendEvent(VisionEvent.RACKET_A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket A
                    </button>
                    <button
                        onClick={() => sendEvent(VisionEvent.BOARD_A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board A
                    </button>
                    <button
                        onClick={() => sendEvent(VisionEvent.EDGE_A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Edge A
                    </button>
                    <button
                        onClick={() => sendEvent(VisionEvent.CLIP_A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Clip A
                    </button>
                </div>
                <div>
                    <button
                        onClick={() => sendEvent(VisionEvent.NET)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Net
                    </button>
                    <button
                        onClick={() => sendEvent(VisionEvent.OUT)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Out
                    </button>
                </div>
                <div>
                    <h2 className="font-bold mb-2">Player B</h2>
                    <button
                        onClick={() => sendEvent(VisionEvent.RACKET_B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket B
                    </button>
                    <button
                        onClick={() => sendEvent(VisionEvent.BOARD_B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board B
                    </button>
                    <button
                        onClick={() => sendEvent(VisionEvent.EDGE_B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Edge B
                    </button>
                    <button
                        onClick={() => sendEvent(VisionEvent.CLIP_B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Clip B
                    </button>
                </div>
            </div>
        </div>
    );
}