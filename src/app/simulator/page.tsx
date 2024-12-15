'use client';

import {BallEvent, GameState, Player} from "@/lib/types";
import {useState} from "react";

export default function SimulatorPage() {
    const [state, setState] = useState<GameState>()
    const sendEvent = async (ballEvent: BallEvent, player?: Player) => {
        const body = JSON.stringify({
            ballEvent,
            player,
            timestamp: Date.now()
        })
        console.log("oskar posting event", body);
        try {
            const response = await fetch('http://localhost:8080/event', {
                method: 'POST',
                body,
            });
            const newState = await response.json()
            console.log("oskar posted event", newState);
            setState(newState);
        } catch (e) {
            console.log("exception", e)
        }
    };

    return (
        <div className="p-8">
            <div><h2 className="font-bold mb-2">Current State: {JSON.stringify(state)}</h2></div>
            <h1 className="text-2xl font-bold mb-4">Vision Event Simulator</h1>
            <div className="grid grid-cols-2 gap-4">
                <div>
                    <h2 className="font-bold mb-2">Player A</h2>
                    <button
                        onClick={() => sendEvent(BallEvent.Racket, Player.A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket A
                    </button>
                    <button
                        onClick={() => sendEvent(BallEvent.Board, Player.A)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board A
                    </button>
                </div>
                <div>
                    <button
                        onClick={() => sendEvent(BallEvent.Net)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Net
                    </button>
                    <button
                        onClick={() => sendEvent(BallEvent.Out)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Out
                    </button>
                </div>
                <div>
                    <h2 className="font-bold mb-2">Player B</h2>
                    <button
                        onClick={() => sendEvent(BallEvent.Racket, Player.B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Racket B
                    </button>
                    <button
                        onClick={() => sendEvent(BallEvent.Board, Player.B)}
                        className="w-full mb-2 bg-blue-500 text-white px-4 py-2 rounded"
                    >
                        Board B
                    </button>
                </div>
            </div>
        </div>
    );
}