#!/bin/bash
(
  echo "=== Script ==="
  echo -e "\n--- dump-context.sh ---\n"
  cat $0
  
  echo -e "\n=== Structure ==="
  tree -I 'node_modules|.git|.idea|.next|target|.bsp|project' --noreport -F --charset ascii
  
  echo -e "\n=== Build ==="
  echo -e "\n--- server/build.sbt ---\n"
  cat server/build.sbt
  
  echo -e "\n=== Docker ==="
  echo -e "\n--- docker-compose.yml ---\n"
  cat docker-compose.yml
  echo -e "\n--- server/Dockerfile ---\n"
  cat server/Dockerfile
  
  echo -e "\n=== Scala ==="
  find server/src -type f -name "*.scala" -exec sh -c 'echo "\n--- $1 ---\n"; cat "$1"' _ {} \;
  
  echo -e "\n=== Frontend ==="
  find frontend/src -type f \( -name "*.ts" -o -name "*.tsx" \) -exec sh -c 'echo "\n--- $1 ---\n"; cat "$1"' _ {} \;
) > ai-context.txt
