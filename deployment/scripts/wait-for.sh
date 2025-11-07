#!/bin/bash

set -e

# Аргументы:
# $1 - Команда проверки (обязательная, в кавычках, если с параметрами)
# $2 - Максимальное количество попыток (по умолчанию 30)
# $3 - Интервал между попытками в секундах (по умолчанию 1)

CHECK_COMMAND="$1"
MAX_ATTEMPTS=${2:-30}
SLEEP_INTERVAL=${3:-1}

if [ -z "$CHECK_COMMAND" ]; then
  echo "Error: Check command is required."
  exit 1
fi

COUNT=0
while ! eval "$CHECK_COMMAND" > /dev/null 2>&1; do
  if [ $COUNT -ge $MAX_ATTEMPTS ]; then
    echo "Service did not become available after $MAX_ATTEMPTS attempts. Exiting."
    exit 1
  fi
  echo "Attempt $((COUNT+1))/$MAX_ATTEMPTS: Waiting for service..."
  sleep $SLEEP_INTERVAL
  COUNT=$((COUNT+1))
done

echo "Service is ready! Proceeding..."