#!/bin/zsh

# プロジェクトのクリーンアップとビルド
echo "Running mvn clean install -U..."
mvn clean install -U

# Dockerイメージのビルド
echo "Building Docker image..."
docker build -t twitter-clone .

# 既存のコンテナを停止して削除
echo "Stopping and removing old container..."
docker stop twitter-clone-container || true
docker rm twitter-clone-container || true

# 新しいコンテナの起動
echo "Running new container..."
docker run -d --name twitter-clone-container -p 8080:8080 twitter-clone

echo "Done!"