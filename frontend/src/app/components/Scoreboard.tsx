'use client'

import React, {useEffect, useState} from 'react';
import {InputAction, Player, StateSummary} from '@/lib/types';
import Link from "next/link";
import {Opponent} from "@/lib/utils";

const ServeIndicator = ({active}: { active: boolean }) => (
    <div className={`w-[15vh] h-[15vh] rounded-full border-8 border-white ${active ? 'bg-white' : ''}`}/>
);

function PlayerScore
({
     name,
     setScore,
     gameScore,
     isServing,
     isSecondServe,
     isTwoServesEach,
     bgColor,
     player
 }: {
    name: string
    setScore: number
    gameScore: number
    isServing: boolean
    isSecondServe: boolean
    isTwoServesEach: boolean
    bgColor: string
    player: Player
}) {
    const sendInput = async (action: InputAction) => {
        try {
            await fetch(`${process.env.NEXT_PUBLIC_API_URL}/input`, {
                method: 'POST',
                body: JSON.stringify({action, player})
            });
        } catch (e) {
            console.error("Input error:", e);
        }
    };

    return (
        <div className={`${bgColor} w-1/2 h-full flex flex-col items-center justify-center text-white relative`}>
            {/* Original content stays the same */}
            <div className="text-[5vh]">{name}</div>
            <div className="text-[5vh]">{setScore}</div>
            <div style={{fontSize: '40vh', lineHeight: '40vh'}} className="font-bold mb-0">{gameScore}</div>
            <div className="flex gap-4">
                <ServeIndicator active={isServing && !isSecondServe}/>
                {isTwoServesEach && <ServeIndicator active={isServing && isSecondServe}/>}
            </div>

            {/* Clickable overlays */}
            <div
                className="absolute top-0 left-0 w-full h-1/2 cursor-pointer hover:bg-white/10 active:bg-white/20 transition-colors"
                onClick={() => sendInput(InputAction.Increase)}
            />
            <div
                className="absolute bottom-0 left-0 w-full h-1/2 cursor-pointer hover:bg-white/10 active:bg-white/20 transition-colors"
                onClick={() => sendInput(InputAction.Decrease)}
            />
        </div>
    );
};


function Scoreboard() {
    const [state, setState] = useState<StateSummary>();

    useEffect(() => {
        const eventSource = new EventSource(`${process.env.NEXT_PUBLIC_API_URL}/state`);
        eventSource.onmessage = (event) => {
            setState(JSON.parse(event.data));
        };
        return () => eventSource.close();
    }, []);

    if (!state) return null;

    const isTwoServesEach = state.gamePoints.Red + state.gamePoints.Black < 20
    const isFirstServer = isTwoServesEach ?
        ((state.gamePoints.Red + state.gamePoints.Black) % 4 < 2) :
        (state.gamePoints.Red + state.gamePoints.Black) % 2 == 0;
    const isSecondServe =
        isTwoServesEach &&
        ((state.gamePoints.Red + state.gamePoints.Black) % 2 == 1);

    const playerScores = {
        Red: {
            name: state.playerRed,
            setScore: state.setPoints.Red,
            gameScore: state.gamePoints.Red,
            isServing: (state.firstServer === Player.Red) === isFirstServer,
            isSecondServe,
            isTwoServesEach,
            bgColor:"bg-red-600",
            player: Player.Red,
        },
        Black: {
            name: state.playerBlack,
            setScore: state.setPoints.Black,
            gameScore: state.gamePoints.Black,
            isServing: (state.firstServer === Player.Black) === isFirstServer,
            isSecondServe,
            isTwoServesEach,
            bgColor:"bg-black",
            player: Player.Black,
        }
    }

    return (
        <div className="flex w-screen h-dvh">
            <div className="absolute flex w-full justify-center py-16">
                <Link
                    href="/setup"
                    className="absolute z-10 p-4 bg-white rounded shadow hover:bg-gray-100 text-black"
                >
                    New Game
                </Link>
            </div>
            <PlayerScore
                {...playerScores[state.firstServer]}
            />
            <PlayerScore
                {...playerScores[Opponent(state.firstServer)]}
            />
        </div>
    );
};

export default Scoreboard;