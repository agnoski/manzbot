package app

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class InvestersManager {
  private final int threadDelay = 60

  InvestersManager(config) {
    // create thread pool
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(config.assets.size())

    // create Investers
    def investers = config.assets.collect { Invester(it.asset, it.technicalSettings, it.parameters, it.timePeriod)
    }

    // launch Investers threads
    investers.eachWithIndex { invester, index ->
      scheduledThreadPool.scheduleAtFixedRate(invester, index * threadDelay, invester.timePeriod, TimeUnit.SECONDS)
    }
  }
}
