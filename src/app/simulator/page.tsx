'use client';

import {BallEvent, Player} from "@/lib/types";

export default function SimulatorPage() {
    const sendEvent = async (event: BallEvent, player?: Player) => {
        await fetch('http://localhost:8080/event', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                event,
                player,
                timestamp: Date.now()
            })
        });
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Vision Event Simulator</h1>
            <div className="grid grid-cols-2 gap-4">
                <div>
                    <h2 className="font-bold mb-2">Player A</h2>
                    <button
                        onClick={() => sendEvent(BallEvent.RACKET, Player.A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket A
                    </button>
                    <button
                        onClick={() => sendEvent(BallEvent.BOARD, Player.A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board A
                    </button>
                </div>
                <div>
                    <button
                        onClick={() => sendEvent(BallEvent.NET)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Net
                    </button>
                    <button
                        onClick={() => sendEvent(BallEvent.OUT)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Out
                    </button>
                </div>
                <div>
                    <h2 className="font-bold mb-2">Player B</h2>
                    <button
                        onClick={() => sendEvent(BallEvent.RACKET, Player.B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket B
                    </button>
                    <button
                        onClick={() => sendEvent(BallEvent.BOARD, Player.B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board B
                    </button>
                </div>
            </div>
        </div>
    );
}