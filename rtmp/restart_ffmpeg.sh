#!/bin/bash
while true; do
    echo "Starting FFmpeg RTMP server..."
    ffmpeg -f flv -listen 1 -i rtmp://0.0.0.0/live/stream -f mjpeg -q:v 2 http://localhost:8081/stream
    echo "FFmpeg exited, restarting in 2 seconds..."
    sleep 2
done

# ffmpeg -f flv -listen 1 -i rtmp://0.0.0.0:1935/live/stream -c copy output2.mp4
# ffmpeg -re -i video.mov -c copy -f flv rtmp://localhost:1935/live/stream