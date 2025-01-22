'use client'

import React, {useEffect, useState} from 'react';
import { StateSummary, Player } from '@/lib/types';

const ServeIndicator = ({ active }: { active: boolean }) => (
    <div className={`w-8 h-8 rounded-full border-4 border-white ${active ? 'bg-white' : ''}`} />
);

const PlayerScore = ({
                         setScore,
                         gameScore,
                         isServing,
                         isSecondServe,
                         bgColor
                     }: {
    setScore: number
    gameScore: number
    isServing: boolean
    isSecondServe: boolean
    bgColor: string
}) => (
    <div className={`${bgColor} w-1/2 h-screen flex flex-col items-center justify-center text-white`}>
        <div className="text-3xl mb-8">{setScore}</div>
        <div style={{ fontSize: '45vh', lineHeight: '45vh' }} className="font-bold mb-8">{gameScore}</div>
        {isServing && (
            <div className="flex gap-4">
                <ServeIndicator active={true} />
                <ServeIndicator active={isSecondServe} />
            </div>
        )}
    </div>
);

const Scoreboard = () => {
    const [state, setState] = useState<StateSummary>();

    useEffect(() => {
        const eventSource = new EventSource('http://localhost:8080/state');
        eventSource.onmessage = (event) => {
            setState(JSON.parse(event.data));
        };
        return () => eventSource.close();
    }, []);

    if (!state) return null;

    const isFirstServe = (state.gamePoints.A + state.gamePoints.B) % 4 < 2;
    const isPlayerAServing = state.possession === Player.A && state.rallyState === 'Idle';
    const isPlayerBServing = state.possession === Player.B && state.rallyState === 'Idle';

    return (
        <div className="flex w-screen h-screen">
            <PlayerScore
                setScore={state.setPoints.A}
                gameScore={state.gamePoints.A}
                isServing={isPlayerAServing}
                isSecondServe={!isFirstServe}
                bgColor="bg-red-600"
            />
            <PlayerScore
                setScore={state.setPoints.B}
                gameScore={state.gamePoints.B}
                isServing={isPlayerBServing}
                isSecondServe={!isFirstServe}
                bgColor="bg-black"
            />
        </div>
    );
};

export default Scoreboard;