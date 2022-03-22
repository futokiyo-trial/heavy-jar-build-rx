# heavy-jar-build-rx
並列でMavenスクリプトなどを実行するためのCLI

## ビルド方法

```
git clone https://github.com/futokiyo-trial/heavy-jar-build-rx.git

cd heavy-jar-build-rx

mvn clean package
```

## 使い方
heavy-jar-build-rx用のconfig.jsonを用意し、次のようなコマンドに指定し、実行する。
```
java -jar target/heavy-jar-build-rx.jar ＜heavy-jar-build-rx用のconfig.jsonのパス＞
```

