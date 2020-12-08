package app

import groovy.json.JsonSlurper
import java.time.Instant

class Invester implements Runnable {
  private final def urlTemplate = 'https://ssltsw.forexprostools.com/api.php?action=refresher&pairs=${code}&timeframe=${timeFrame}'
  private final def buyStrings = ["Strong Buy", "Buy"]
  private final def sellStrings = ["Strong Sell", "Sell"]

  private def index
  private def timeCollection
  private def timeInterval
  private def parameters

  public Invester(index, timeCollection, timeInterval, parameters) {
    this.index = index
    this.timeCollection = timeCollection
    this.timeInterval = timeInterval
    this.parameters = parameters
  }

  @Override
  public void run() {
    def buy = this.doRequest()
    def action = [
      index: this.index,
      action: buy,
      parameters: this.parameters,
      time: Instant.now()
    ]
    println("Action: $action")
    ActionsService.getInstance().put(action)
    println("Added action in the queue")
  }

  private def doRequest() {
    def data = this.getData()
    return this.shouldIBuy(data)
  }

  private def getData() {
    return this.timeCollection.collect { time ->
      def url = this.getUrl(time)
      def response = this.getResponse(url)
      def technicalSummary = response[this.index.id]["technicalSummary"]
      return technicalSummary
    }
  }

  private String getUrl(time) {
      def binding = ["code":this.index.id, "timeFrame":time]
      def engine = new groovy.text.SimpleTemplateEngine()
      return engine.createTemplate(urlTemplate).make(binding).toString()
  }

  private def getResponse(url) {
      def response = new URL(url).text
      def jsonSlurper = new JsonSlurper()
      return jsonSlurper.parseText(response)
  }

  private def shouldIBuy(results) {
    def buy = results.every { it in this.buyStrings }
    def sell = results.every { it in this.sellStrings }

    if (buy) {
      return Action.Type.BUY
    } else if (sell) {
      return Action.Type.SELL
    } else {
      return Action.Type.NEUTRAL
    }
  }
}
