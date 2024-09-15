#!/bin/zsh

# プロジェクトのクリーンアップとビルド（テストプロファイルを使用）
echo "Running mvn clean install -U with test profile..."
mvn clean install -U -Ptwitter-clone-test

# Dockerイメージのビルド（テスト用）
echo "Building Docker image for test..."
docker build -t twitter-clone-test .

# 既存のテスト用コンテナを停止して削除
echo "Stopping and removing old test container..."
docker stop twitter-clone-test-container || true
docker rm twitter-clone-test-container || true

# 新しいテスト用コンテナの起動
echo "Running new test container..."
docker run -d --name twitter-clone-test-container -p 8081:8080 -e SPRING_PROFILES_ACTIVE=test twitter-clone-test

echo "Test container is running. You can access it at http://localhost:8081"