'use client';

import React, {useState, useEffect, ReactNode} from 'react';
import {Maximize2, Minimize2} from "lucide-react";

export function FullscreenWrapper({ children }: { children: ReactNode }) {
    const [isFullscreen, setIsFullscreen] = useState(false);

    useEffect(() => {
        async function preventScreenLock() {
            try {
                const wakeLock = await navigator.wakeLock.request('screen');
                return () => wakeLock.release();
            } catch (err) {
                console.log('Wake Lock not supported', err);
            }
        }
        preventScreenLock();
    }, []);

    const toggleFullscreen = async () => {
        if (!document.fullscreenElement) {
            await document.documentElement.requestFullscreen();
            setIsFullscreen(true);
        } else {
            await document.exitFullscreen();
            setIsFullscreen(false);
        }
    };

    return (
        <div className="relative">
            {children}
            <button
                onClick={toggleFullscreen}
                className="absolute top-2 right-2 p-2 bg-white text-black rounded hover:bg-gray-200"
            >
                {isFullscreen ? <Minimize2 size={24} /> : <Maximize2 size={24} />}
            </button>
        </div>
    );
};

export default FullscreenWrapper;