#!/bin/bash

# 成功フラグの初期化
all_success=true

# ログインしてトークンを取得
response=$(curl -s -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser03", "password":"password03"}')

# レスポンスからトークンを抽出
ACCESS_TOKEN=$(echo "$response" | jq -r '.token')

# トークンが取得できたか確認
if [ -z "$ACCESS_TOKEN" ] || [ "$ACCESS_TOKEN" == "null" ]; then
  echo "アクセストークンの取得に失敗しました。"
  exit 1
fi

# 関数: curl リクエストを実行し、レスポンスとステータスコードを処理
perform_request() {
  local method=$1
  local url=$2
  local data=$3
  local tmp_file=$(mktemp)
  local status_code

  if [ "$method" == "POST" ]; then
    status_code=$(curl -s -X POST "$url" \
      -H "Authorization: Bearer $ACCESS_TOKEN" \
      -H "Content-Type: application/json" \
      -d "$data" \
      -w "%{http_code}" \
      -o "$tmp_file")
  elif [ "$method" == "GET" ]; then
    status_code=$(curl -s -X GET "$url" \
      -H "Authorization: Bearer $ACCESS_TOKEN" \
      -H "Content-Type: application/json" \
      -w "%{http_code}" \
      -o "$tmp_file")
  else
    echo "サポートされていないHTTPメソッド: $method"
    rm -f "$tmp_file"
    return 1
  fi

  # レスポンスボディを読み込む
  local response_body=$(cat "$tmp_file")
  rm -f "$tmp_file"

  # レスポンスボディとステータスコードを返す
  echo "$response_body"
  echo "$status_code"
}

# リクエストをチェックする関数
check_request() {
  local description=$1
  local method=$2
  local url=$3
  local data=$4

  echo "$description を実行中..."

  # perform_request の出力を2行に分けて取得
  response=$(perform_request "$method" "$url" "$data")
  body=$(echo "$response" | head -n 1)
  status_code=$(echo "$response" | tail -n 1)

  # レスポンスボディを表示
  echo "レスポンス内容:"
  echo "$body"
  echo ""

  # ステータスコードを表示
  echo "HTTPステータス: $status_code"
  echo ""

  # ステータスコードが3桁の数字であることを確認
  if ! [[ "$status_code" =~ ^[0-9]{3}$ ]]; then
    echo "エラー: '$description' から有効なステータスコードを取得できませんでした。"
    echo ""
    all_success=false
  elif [ "$status_code" -ne 200 ]; then
    echo "エラー: '$description' がステータスコード $status_code で失敗しました。"
    echo ""
    all_success=false
  fi
}

# フォローユーザーの投稿を取得
check_request "フォローユーザーの投稿を取得" "GET" "http://localhost:8080/api/feed/following-posts"

# 自分とフォローユーザーの投稿を取得
check_request "自分とフォローユーザーの投稿を取得" "GET" "http://localhost:8080/api/feed/my-and-following-posts"

# 自分がフォローしているユーザー一覧を取得
check_request "自分がフォローしているユーザー一覧を取得" "GET" "http://localhost:8080/api/follow/following"

# 自分をフォローしているユーザー一覧を取得
check_request "自分をフォローしているユーザー一覧を取得" "GET" "http://localhost:8080/api/follow/followers"

# すべてのリクエストが成功したか確認
if [ "$all_success" = true ]; then
  echo "すべてのリクエストがHTTPステータス200で成功しました。"
else
  echo "一部のリクエストが失敗しました。上記のエラーを確認してください。"
fi