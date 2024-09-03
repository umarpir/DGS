#!/bin/bash

set -e
echo "Building projects..."
docker-compose build

echo "Starting database container..."
docker-compose up -d db

echo "Waiting for the database to be ready..."
until docker exec mysql mysqladmin ping -h"localhost" --silent; do
    printf "."
    sleep 1
done

echo "Database is ready."

echo "Starting all services..."
docker-compose up

echo "All services are up and running."