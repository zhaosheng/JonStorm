package sheng;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;


/**
 * Created by shezhao on 8/25/14.
 */
public class FirstTopology {
//  private static Logger _logger = LoggerFactory.getLogger(FirstTopology.class);

  public SpoutConfig buildKafkaSpoutConfig() {
    ZkHosts zkHosts = new ZkHosts("ec2-54-191-68-82.us-west-2.compute.amazonaws.com");
    SpoutConfig config = new SpoutConfig(zkHosts, "firstT", "", "0");
    config.scheme = new SchemeAsMultiScheme(new StringScheme());
    return config;
  }

  public StormTopology build() {
    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("in", new KafkaSpout(buildKafkaSpoutConfig()));
    builder.setBolt("out", new PrintBolt()).shuffleGrouping("in");
    return builder.createTopology();
  }

  public static void main(String[] args)
      throws AlreadyAliveException, InvalidTopologyException {
    FirstTopology firstTopology = new FirstTopology();
    Config config = new Config();
    config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
    StormTopology build = firstTopology.build();
    StormSubmitter.submitTopology("Hello", config, build);
  }
}