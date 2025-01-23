'use client'

import React, {useEffect, useState} from 'react';
import { StateSummary } from '@/lib/types';

const ServeIndicator = ({ active }: { active: boolean }) => (
    <div className={`w-32 h-32 rounded-full border-8 border-white ${active ? 'bg-white' : ''}`} />
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
        <div className="flex gap-4">
            <ServeIndicator active={isServing} />
            <ServeIndicator active={isServing && isSecondServe} />
        </div>
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

    const isPlayerAServing = state.gamePoints.Red + state.gamePoints.Black < 20 ?
        ((state.gamePoints.Red + state.gamePoints.Black) % 4 < 2) :
        (state.gamePoints.Red + state.gamePoints.Black) % 2 == 0;
    const isSecondServe =
        (state.gamePoints.Red + state.gamePoints.Black < 20) &&
        ((state.gamePoints.Red + state.gamePoints.Black) % 2 == 1);

    return (
        <div className="flex w-screen h-screen">
            <PlayerScore
                setScore={state.setPoints.Red}
                gameScore={state.gamePoints.Red}
                isServing={isPlayerAServing}
                isSecondServe={isSecondServe}
                bgColor="bg-red-600"
            />
            <PlayerScore
                setScore={state.setPoints.Black}
                gameScore={state.gamePoints.Black}
                isServing={!isPlayerAServing}
                isSecondServe={isSecondServe}
                bgColor="bg-black"
            />
        </div>
    );
};

export default Scoreboard;