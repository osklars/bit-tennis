'use client'

import React, {useEffect, useState} from 'react';
import {InputAction, Player, StateSummary} from '@/lib/types';
import Link from "next/link";
import {Opponent} from "@/lib/utils";
import {ChevronDown, ChevronUp} from "lucide-react";

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
     isRightSide,
     bgColor,
     player
 }: {
    name: string
    setScore: number
    gameScore: number
    isServing: boolean
    isSecondServe: boolean
    isTwoServesEach: boolean
    isRightSide?: boolean
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
        <div className={`${bgColor} w-1/2 h-full flex flex-col p-8 items-center justify-between text-white relative`}>
            {/* Original content stays the same */}
            <div className="text-[5vh] flex-1">{name}</div>
            <div className={`w-full flex flex-2 items-center ${isRightSide ? 'flex-row-reverse text-end' : ''}`}>
                <div className="text-[10vh] flex-1">{setScore}</div>
                <div style={{fontSize: '40vh', lineHeight: '40vh'}} className="flex-2 text-center font-bold mb-0">{gameScore}</div>
                <div className="flex-1"/>
            </div>
            <div className="flex-1 flex gap-4">
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

function HiddenButtons() {
    const [hidden, setHidden] = useState(true);
    
    return (
        <div className="absolute z-10 flex flex-col w-full items-center justify-center bottom-0 bg-gradient-to-t from-transparent to-white/20">
            <button onClick={() => setHidden(!hidden)}>
                {hidden ? <ChevronUp color={"white"} height={64} width={128}/> : <ChevronDown color={"white"} height={64} width={128}/>}
            </button>
            {!hidden &&
                <Link
                    href="/setup"
                    className="m-8 p-4 bg-white rounded shadow hover:bg-gray-100 text-black font-bold text-[5vh]"
                >
                    New Game
                </Link>}
        </div>
    );
}


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
    
    const leftPlayer = (state.setPoints.Red + state.setPoints.Black) % 2 == 0 ? Player.Red : Player.Black;

    return (
        <div className="flex w-screen h-dvh overflow-hidden">
            <HiddenButtons />
            <PlayerScore
                {...playerScores[leftPlayer]}
            />
            <PlayerScore
                {...playerScores[Opponent(leftPlayer)]}
                isRightSide
            />
        </div>
    );
};

export default Scoreboard;