package app

import groovy.json.JsonSlurper
import java.time.Instant

class Invester implements Runnable {
  private final def urlTemplate = 'https://ssltsw.forexprostools.com/api.php?action=refresher&pairs=${code}&timeframe=${timeFrame}'

  private def asset
  private def timeFrames
  private def timePeriod
  private def parameters
  private def indicators

  public Invester(asset, technicalSettings, parameters, timePeriod) {
    this.asset = asset
    this.timeFrames = technicalSettings.timeFrames
    this.indicators = technicalSettings.indicators
    this.parameters = parameters
    this.timePeriod = timePeriod
  }

  @Override
  public void run() {
    def buy = this.doRequest()
    def action = [
      index: this.asset,
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
    return this.timeFrames.collect { time ->
      def url = this.getUrl(time)
      def response = this.getResponse(url)
      def technicalSummary = response[this.asset.id]["technicalSummary"]
      return technicalSummary
    }
  }

  private String getUrl(time) {
      def binding = ["code": this.asset.id, "timeFrame": time]
      def engine = new groovy.text.SimpleTemplateEngine()
      return engine.createTemplate(urlTemplate).make(binding).toString()
  }

  private def getResponse(url) {
      def response = new URL(url).text
      def jsonSlurper = new JsonSlurper()
      return jsonSlurper.parseText(response)
  }

  private def shouldIBuy(results) {
    def buy = results.every { it in this.indicators.buy }
    def sell = results.every { it in this.indicators.sell }

    if (buy) {
      return Action.Type.BUY
    } else if (sell) {
      return Action.Type.SELL
    } else {
      return Action.Type.NEUTRAL
    }
  }
}
