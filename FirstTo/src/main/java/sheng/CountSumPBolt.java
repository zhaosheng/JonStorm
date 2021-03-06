package sheng;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by shezhao on 8/25/14.
 */
public class CountSumPBolt extends BaseRichBolt {
  private long _totalSum;
  private static final Logger _LOG = LoggerFactory.getLogger(CountSumPBolt.class);
  private OutputCollector _outputCollector;

  /**
   * Called when a task for this component is initialized within a worker on the cluster.
   * It provides the bolt with the environment in which the bolt executes.
   *
   * <p>This includes the:</p>
   *  @param stormConf The Storm configuration for this bolt. This is the configuration provided to the topology merged in with cluster configuration on this machine.
   * @param context This object can be used to get information about this task's place within the topology, including the task id and component id of this task, input and output information, etc.

   * @param collector The collector is used to emit tuples from this bolt. Tuples can be emitted at any time, including the prepare and cleanup methods. The collector is thread-safe and should be saved as an instance variable of this bolt object.
   */
  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    _outputCollector = collector;
  }

  /**
   * Process a single tuple of input. The Tuple object contains metadata on it
   * about which component/stream/task it came from. The values of the Tuple can
   * be accessed using Tuple#getValue. The IBolt does not have to process the Tuple
   * immediately. It is perfectly fine to hang onto a tuple and process it later
   * (for instance, to do an aggregation or join).
   *
   * <p>Tuples should be emitted using the OutputCollector provided through the prepare method.
   * It is required that all input tuples are acked or failed at some point using the OutputCollector.
   * Otherwise, Storm will be unable to determine when tuples coming off the spouts
   * have been completed.</p>
   *
   * <p>For the common case of acking an input tuple at the end of the execute method,
   * see IBasicBolt which automates this.</p>
   *

   * @param input The input tuple to be processed.
   */
  @Override
  public void execute(Tuple input) {
    _totalSum += 1;
    if (_totalSum % 5 == 0) {
      _LOG.info("Total Count: " + _totalSum);
    }
    _outputCollector.ack(input);
  }

  /**
   * Declare the output schema for all the streams of this topology.
   *

   * @param declarer this is used to declare output stream ids, output fields, and whether or not each output stream is a direct stream
   */
  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {

  }
}
