#!/bin/bash
(
  echo "=== Structure ==="
  tree -I 'node_modules|.git|.idea|.next|target|.bsp|project' --noreport -F --charset ascii
  echo "=== Scala ==="
  find server/src -type f -name "*.scala" -exec sh -c 'echo "\n--- $1 ---\n"; cat "$1"' _ {} \;
  echo -e "\n\n=== Frontend ==="
  find frontend/src -type f \( -name "*.ts" -o -name "*.tsx" \) -exec sh -c 'echo "\n--- $1 ---\n"; cat "$1"' _ {} \;
) > ai-context.txt