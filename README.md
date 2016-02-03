# TcpipAgentTester
TCP/IP接続版(0.3.x対応) AgentTester

スタンドアロンテスター機能の他，テスト対象クライアントからの接続を待つサーバー機能と，サーバーに接続するだけの汎用クライアントスターター機能を実装しました．

### 使用法

  ```
  [-p port] [-h host] [-c clientClass]
  ```
    
  　　port : ポート番号（デフォルトは10000）
  
  　　host : 接続先ホスト名．指定した場合汎用クライアントスターターとなります．
  
  　　clientClass : プレイヤークラス名．無指定の場合接続を待つサーバーとなります．
  
### 使用例

  1. org.aiwolf.client.base.smpl.SampleRoleAssignPlayerのテスト
  
  ```
  java -jar TcpipAgentTester.jar -c org.aiwolf.client.base.smpl.SampleRoleAssignPlayer
  ```
  
  1. 12345番ポートで待つテストサーバーの起動
  
  ```
  java -jar TcpipAgentTester.jar -p 12345
  ```
  
  1. ホストfoo.bar.baz上のサーバにポート12345番で接続
  
  ```
  java -jar TcpipAgentTester.jar -p 12345 -h foo.bar.baz -c org.aiwolf.client.base.smpl.SampleRoleAssignPlayer
  ```