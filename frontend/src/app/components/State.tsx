'use client';

import {useEffect, useState} from 'react';
import {Player, StateSummary} from '@/lib/types';

export default function State() {
    const [history, setHistory] = useState<StateSummary[]>([]);

    useEffect(() => {
        const eventSource = new EventSource(`${process.env.NEXT_PUBLIC_API_URL}/state`);
        eventSource.onmessage = (event) => {
            const data = JSON.parse(event.data);
            console.log("oskar updating history", data);
            setHistory(old => [data, ...old].slice(0, 30));
        };

        return () => eventSource.close();
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Game State</h1>
            {history.length > 0 && <VisualState state={history[0]}/>}
            <div className="bg-white rounded-lg shadow p-4">
                <h2 className="font-bold mb-2">Latest Events</h2>
                <div className="space-y-2">
                    {history.map((h, i) => (
                        <div key={i} className="flex items-center space-x-2 text-sm">
                            <span>{h.latestEvent?.event} {h.latestEvent?.side}</span>
                            <span>{h.rallyState} {h.possession} {h.gamePoints.Red}:{h.gamePoints.Black}</span>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

function VisualState({state}: { state: StateSummary }) {

    const isPlayerActive = (player: Player) => state.possession === player;

    return (
        <div className="p-8 space-y-8">
            {/* Score Display */}
            <div className="flex justify-center items-center gap-16">
                <div className={`text-6xl font-bold ${isPlayerActive(Player.Red) ? 'text-red-600' : ''}`}>
                    {state.gamePoints.Red}
                    <span className="text-2xl text-gray-400 ml-2">({state.setPoints.Red})</span>
                </div>
                <div className="text-2xl font-bold text-gray-400">vs</div>
                <div className={`text-6xl font-bold ${isPlayerActive(Player.Black) ? 'text-black' : ''}`}>
                    {state.gamePoints.Black}
                    <span className="text-2xl text-gray-400 ml-2">({state.setPoints.Black})</span>
                </div>
            </div>

            {/* Rally State */}
            <div className="text-center text-lg">
                {state.rallyState} - {state.latestEvent?.event} by {state.latestEvent?.side || 'none'}
            </div>
        </div>
    );
}