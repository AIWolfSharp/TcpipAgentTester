# TcpipAgentTester
第1回大会で提供されたAgentTesterをTCP/IP接続に改造したものです．
プレイヤーのインスタンスは初めに生成したものが最後まで使用されます．
全役職についてランダムプレイヤーの中で複数回対戦を行い，
Exception発生の可能性をチェックします．

### 使用法

  ```
  java -jar TcpipAgentTeseter.jar [-h] [-c clientClass] [-g gameNum] [-n playerNum] [-p port]
  ```

-h オプションを与えるとUsageを出力します．

clientClass : プレイヤークラス名．無指定の場合TCP/IP接続を待ちます．

gameNum : 1役職あたりのゲーム数（デフォルトは10）

playerNum : プレイヤー数（デフォルトは15）

port : ポート番号（デフォルトは10000）
  
### 使用例

  1. org.aiwolf.client.base.smpl.SampleRoleAssignPlayerのテスト.
  
  
  ```
  java -jar TcpipAgentTester.jar -c org.aiwolf.client.base.smpl.SampleRoleAssignPlayer
  ```
  
  1. 12345番ポートで接続を待ち，
  
  ```
  java -jar TcpipAgentTester.jar -p 12345
  ```
  公式ClientStarterで接続．
  ```
  java -cp aiwolf-common.jar;aiwolf-client.jar;jsonic-1.3.10.jar org.aiwolf.common.bin.ClientStarter -h localhost -p 12345 -c org.aiwolf.client.base.smpl.SampleRoleAssignPlayer
  ```
  
### その他
  
  ログ出力がうるさい場合はjavaのVM argumentでlogging.propertiesファイルを指定すると抑制されます．
  
  ```
  java -Djava.util.logging.config.file=logging.properties -jar TcpipAgentTester.jar -p 12345
  ```
  