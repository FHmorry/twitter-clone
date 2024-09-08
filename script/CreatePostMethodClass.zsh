#!/bin/zsh

# 基本パスを設定
BASE_PATH="/Users/sars6388/twitter-clone/src/main/java/com/example/twitterclone"

# フォルダとファイルを作成する関数
create_file() {
    local folder=$1
    local file=$2
    mkdir -p "$BASE_PATH/$folder"
    touch "$BASE_PATH/$folder/$file"
    echo "Created $BASE_PATH/$folder/$file"
}

# 各フォルダとファイルを作成
create_file "controller" "PostController.java"
create_file "service" "PostService.java"
create_file "repository" "PostRepository.java"
create_file "model" "Post.java"

echo "All folders and files have been created successfully."