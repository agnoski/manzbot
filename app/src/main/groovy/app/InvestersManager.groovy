package app

import groovy.json.JsonSlurper
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class InvestersManager {
  private final int threadDelay = 60
  private String configFilePath = "/home/agno/Dev/gradleTest/app/app/src/main/groovy/app/config.json"

  public InvestersManager() {
    // load json
    def jsonSlurper = new JsonSlurper()
    def config = jsonSlurper.parse(new File(configFilePath))

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(config.indexes.size())

    // create Investers
    def investers = config.assets.collect { asset -> 
      return new Invester(asset.asset, asset.technicalSettings, asset.parameters, asset.timePeriod)
    }

    // launch threads
    investers.eachWithIndex { invester, index ->
      scheduledThreadPool.scheduleAtFixedRate(invester, index * threadDelay, invester.timePeriod, TimeUnit.SECONDS)
    }
  }
}
