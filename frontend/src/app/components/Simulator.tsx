'use client';

import {EventType, StateSummary, Player, ErrorResponse} from "@/lib/types";
import {useState} from "react";

const highlight = 'outline-offset-[-4px] outline outline-4 outline-yellow-400';

export default function Simulator() {
    const [state, setState] = useState<StateSummary | ErrorResponse>();

    const sendEvent = async (event: EventType, player?: Player) => {
        try {
            const response = await fetch('http://localhost:8080/event', {
                method: 'POST',
                body: JSON.stringify({ event, player: player || null })
            });
            setState(await response.json());
        } catch (e) {
            console.error("Event error:", e);
        }
    };

    const isLatestEvent = (event: EventType, player?: Player) => {
        if (!state || 'error' in state) return false;
        return state.latestEvent?.event === event && (!player || state.latestEvent?.player === player);
    };

    const Button = ({ onClick, className, children }: any) => (
        <button onClick={onClick} className={`px-4 py-2 rounded hover:opacity-90 ${className}`}>
            {children}
        </button>
    );

    return (
        <div className="h-screen flex flex-col p-8">
            <h1 className="text-2xl font-bold mb-4">Event Simulator</h1>
            <button
                onClick={() => sendEvent(EventType.Out)}
                className={`mb-8 h-32 bg-gray-100 hover:bg-gray-200 rounded-lg ${
                    isLatestEvent(EventType.Out) ? highlight : ''
                }`}
            >
                Out
            </button>

            {/* Main content - added z-10 to stay above out area */}
            <div className="flex items-center justify-center gap-8">
                <div className="flex items-center gap-4">
                    <span className="text-2xl font-bold">Red</span>
                    <div className="flex flex-col gap-2">
                        <Button
                            onClick={() => sendEvent(EventType.Throw, Player.Red)}
                            className={`bg-red-600 text-white ${isLatestEvent(EventType.Throw, Player.Red) ? highlight : ''}`}
                        >
                            Throw
                        </Button>
                        <Button
                            onClick={() => sendEvent(EventType.Racket, Player.Red)}
                            className={`bg-red-600 text-white ${isLatestEvent(EventType.Racket, Player.Red) ? highlight : ''}`}
                        >
                            Racket
                        </Button>
                    </div>
                </div>

                {/* Table */}
                <div className="flex border-2 border-gray-400 rounded">
                    <button
                        onClick={() => sendEvent(EventType.Board, Player.Red)}
                        className={`w-36 h-32 bg-green-100 hover:bg-green-200 border-r-4 border-gray-400 border-inset ${
                            isLatestEvent(EventType.Board, Player.Red) ? highlight : ''
                        }`}
                    >
                        Red Side
                    </button>
                    <button
                        onClick={() => sendEvent(EventType.Net)}
                        className={`w-4 h-32 bg-gray-300 hover:bg-gray-400 ${
                            isLatestEvent(EventType.Net) ? highlight : ''
                        }`}
                    />
                    <button
                        onClick={() => sendEvent(EventType.Board, Player.Black)}
                        className={`w-36 h-32 bg-green-100 hover:bg-green-200 border-l-4 border-gray-400 border-inset ${
                            isLatestEvent(EventType.Board, Player.Black) ? highlight : ''
                        }`}
                    >
                        Black Side
                    </button>
                </div>

                {/* Right side */}
                <div className="flex items-center gap-4">
                    <div className="flex flex-col gap-2">
                        <Button
                            onClick={() => sendEvent(EventType.Throw, Player.Black)}
                            className={`bg-black text-white ${isLatestEvent(EventType.Throw, Player.Black) ? highlight : ''}`}
                        >
                            Throw
                        </Button>
                        <Button
                            onClick={() => sendEvent(EventType.Racket, Player.Black)}
                            className={`bg-black text-white ${isLatestEvent(EventType.Racket, Player.Black) ? highlight : ''}`}
                        >
                            Racket
                        </Button>
                    </div>
                    <span className="text-2xl font-bold">Black</span>
                </div>
            </div>
        </div>
    );
}