'use client';

import {EventType, StateSummary, Player, ErrorResponse} from "@/lib/types";
import {useState} from "react";

export default function Simulator() {
    const [state, setState] = useState<StateSummary | ErrorResponse>();

    const sendEvent = async (event: EventType, player?: Player) => {
        const body = JSON.stringify({
            event,
            player: player || null
        })
        try {
            console.log("oskar posting event", body);
            const response = await fetch('http://localhost:8080/event', {
                method: 'POST',
                body,
            });
            const newState = await response.json();
            console.log("oskar posted event", newState);
            setState(newState);
        } catch (e) {
            console.log("oskar post event", e)
        }
    };
    
    return (
        <div className="relative p-16"> {/* Added padding for out area */}
            {/* Out area */}
            <div
                onClick={() => sendEvent(EventType.Out)}
                className="absolute inset-0 bg-gray-100 bg-opacity-50 hover:bg-opacity-70 cursor-pointer"
            />

            {/* Main content - added z-10 to stay above out area */}
            <div className="relative z-10 flex items-center justify-center gap-8">
                <div className="flex items-center justify-center gap-8">
                    {/* Left side */}
                    <div className="flex items-center gap-4">
                        <span className="text-2xl font-bold">A</span>
                        <div className="flex flex-col gap-2">
                            <button
                                onClick={() => sendEvent(EventType.Throw, Player.A)}
                                className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                            >
                                Throw
                            </button>
                            <button
                                onClick={() => sendEvent(EventType.Racket, Player.A)}
                                className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                            >
                                Racket
                            </button>
                        </div>
                    </div>

                    {/* Table */}
                    <div className="border-2 border-gray-400 rounded">
                        <div className="flex">
                            <button
                                onClick={() => sendEvent(EventType.Board, Player.A)}
                                className="w-48 h-32 bg-green-100 hover:bg-green-200 border-r-4 border-gray-400"
                            >
                                Side A
                            </button>
                            <div
                                onClick={() => sendEvent(EventType.Net)}
                                className="w-4 h-32 bg-gray-300 hover:bg-gray-400 cursor-pointer"
                            />
                            <button
                                onClick={() => sendEvent(EventType.Board, Player.B)}
                                className="w-48 h-32 bg-green-100 hover:bg-green-200 border-l-4 border-gray-400"
                            >
                                Side B
                            </button>
                        </div>
                    </div>

                    {/* Right side */}
                    <div className="flex items-center gap-4">
                        <div className="flex flex-col gap-2">
                            <button
                                onClick={() => sendEvent(EventType.Throw, Player.B)}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                Throw
                            </button>
                            <button
                                onClick={() => sendEvent(EventType.Racket, Player.B)}
                                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                            >
                                Racket
                            </button>
                        </div>
                        <span className="text-2xl font-bold">B</span>
                    </div>
                </div>
            </div>
        </div>
    
    );
}