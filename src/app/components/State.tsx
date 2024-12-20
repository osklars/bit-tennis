'use client';

import {useEffect, useState} from 'react';
import {StateSummary} from '@/lib/types';

export default function State() {
    const [history, setHistory] = useState<StateSummary[]>([]);

    useEffect(() => {
        const eventSource = new EventSource('http://localhost:8080/state');
        eventSource.onmessage = (event) => {
            const data = JSON.parse(event.data);
            console.log("oskar updated history", data);
            setHistory([data, ...history].slice(0, 5));
        };

        return () => eventSource.close();
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Game State</h1>
            <div className="bg-white rounded-lg shadow p-4">
                <h2 className="font-bold mb-2">Latest Events</h2>
                <div className="space-y-2">
                    {history.map((h, i) => (
                        <div key={i} className="flex items-center space-x-2 text-sm">
                            <span>{h.latestEvent?.event} {h.latestEvent?.player}</span>
                            <span>{h.rallyState} {h.possession} {h.gamePoints.A}:{h.gamePoints.B}</span>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}