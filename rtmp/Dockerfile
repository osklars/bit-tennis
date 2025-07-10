FROM alpine:latest
RUN apk add --no-cache ffmpeg bash
COPY restart_ffmpeg.sh /restart_ffmpeg.sh
RUN chmod +x /restart_ffmpeg.sh
EXPOSE 1935
CMD ["/restart_ffmpeg.sh"]