'use client';

import { useEffect, useState } from 'react';
import {GameHistory} from '@/lib/types';

export default function StatePage() {
    const [history, setHistory] = useState<GameHistory[]>([]);

    useEffect(() => {
        const eventSource = new EventSource('http://localhost:8080/state');
        eventSource.onmessage = (event) => {
            console.log("oskar updated history", event.data);
            setHistory(JSON.parse(event.data));
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
                            <span>{h.event.type}</span>
                            <span className="text-gray-500">
                {new Date(h.event.timestamp).toLocaleTimeString()}
              </span>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}