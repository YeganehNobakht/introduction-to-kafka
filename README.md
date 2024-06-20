Running the Kafka on windows:
commands:
$ wsl --install
$ sudo apt-get update && sudo apt-get upgrade -y
$ sudo apt install openjdk-17-jdk -y
$ wget https://dlcdn.apache.org/kafka/3.5.0/kafka_2.13-3.5.0.tgz
$ tar -xzf kafka_2.13-3.5.0.tgz
$ cd kafka_2.13-3.5.0


Kafka with KRaft:
$ KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
$ bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties
$ bin/kafka-server-start.sh config/kraft/server.properties

CREATE A TOPIC TO STORE YOUR EVENTS:
$ bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092

DISCRIBE SPECEFIC TOPIC:
$ bin/kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092

WRITE SOME EVENTS INTO THE TOPIC (PRODUCER)
$ bin/kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092

READ THE EVENTS (CONSUMER)
$ bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092

KEYED MESSAGE:
IN PRODUCER SIDE:
$ bin/kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092 --property parse.key=true --property key.seprate=:

IN CONSUMER SIDE:
$ bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092 --propery print.key=true key.seprator=:

UPDATING PARTITION COUNT FOR A TOPIC:
$ bin/kafka-topics.sh --bootstrap-server localhost:9092 --alter --topic quickstart-events --partitions 5

SEEING THE LIST OF CONSUMER GROUPS:
$./bin/kafka-consumer-group.sh --bootstrap-server localhost:9092 --list

DESCRIBE SPECEFIC CONSUMER GROUP:
$./bin/kafka-consumer-group.sh --bootstrap-server localhost:9092 --describe --group <your.consumer.group.name>

CONNECT YOUR LOCALHOST TO UBUNTU LOCALHOST IN WINDOWS:
OPEN COMMAND PROMPT AS ADMINASTRATOR AN THEN PASTE THIS COMMAND:
$ netsh interface portproxy add v4tov4 listenport=9092 listenaddress=0.0.0.0 connectport=9092 connectaddress=172.X.X.X
 WHITCH 172.X.X.X IS THE IP OF THE WSL2 -> RUN "ifconfig" IN WSL2 UBUNTU

