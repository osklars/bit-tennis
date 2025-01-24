'use client';

import {EventType, StateSummary, Side, ErrorResponse} from "@/lib/types";
import {useState} from "react";

const highlight = 'outline-offset-[-4px] outline outline-4 outline-yellow-400';

export default function Simulator() {
    const [state, setState] = useState<StateSummary | ErrorResponse>();

    const sendEvent = async (event: EventType, side?: Side) => {
        try {
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/event`, {
                method: 'POST',
                body: JSON.stringify({ event, side })
            });
            setState(await response.json());
        } catch (e) {
            console.error("Event error:", e);
        }
    };

    const isLatestEvent = (event: EventType, side?: Side) => {
        if (!state || 'error' in state) return false;
        return state.latestEvent?.event === event && (!side || state.latestEvent?.side === side);
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
                    <span className="text-2xl font-bold">Left</span>
                    <div className="flex flex-col gap-2">
                        <Button
                            onClick={() => sendEvent(EventType.Throw, Side.Left)}
                            className={`bg-red-600 text-white ${isLatestEvent(EventType.Throw, Side.Left) ? highlight : ''}`}
                        >
                            Throw
                        </Button>
                        <Button
                            onClick={() => sendEvent(EventType.Racket, Side.Left)}
                            className={`bg-red-600 text-white ${isLatestEvent(EventType.Racket, Side.Left) ? highlight : ''}`}
                        >
                            Racket
                        </Button>
                    </div>
                </div>

                {/* Table */}
                <div className="flex border-2 border-gray-400 rounded">
                    <button
                        onClick={() => sendEvent(EventType.Board, Side.Left)}
                        className={`w-36 h-32 bg-green-100 hover:bg-green-200 border-r-4 border-gray-400 border-inset ${
                            isLatestEvent(EventType.Board, Side.Left) ? highlight : ''
                        }`}
                    >
                        Left Side
                    </button>
                    <button
                        onClick={() => sendEvent(EventType.Net)}
                        className={`w-4 h-32 bg-gray-300 hover:bg-gray-400 ${
                            isLatestEvent(EventType.Net) ? highlight : ''
                        }`}
                    />
                    <button
                        onClick={() => sendEvent(EventType.Board, Side.Right)}
                        className={`w-36 h-32 bg-green-100 hover:bg-green-200 border-l-4 border-gray-400 border-inset ${
                            isLatestEvent(EventType.Board, Side.Right) ? highlight : ''
                        }`}
                    >
                        Right Side
                    </button>
                </div>

                {/* Right side */}
                <div className="flex items-center gap-4">
                    <div className="flex flex-col gap-2">
                        <Button
                            onClick={() => sendEvent(EventType.Throw, Side.Right)}
                            className={`bg-black text-white ${isLatestEvent(EventType.Throw, Side.Right) ? highlight : ''}`}
                        >
                            Throw
                        </Button>
                        <Button
                            onClick={() => sendEvent(EventType.Racket, Side.Right)}
                            className={`bg-black text-white ${isLatestEvent(EventType.Racket, Side.Right) ? highlight : ''}`}
                        >
                            Racket
                        </Button>
                    </div>
                    <span className="text-2xl font-bold">Right</span>
                </div>
            </div>
        </div>
    );
}