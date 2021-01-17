package app

import groovy.util.logging.Log

@Log
class App {

  static void main(String[] args) {
    //TODO: could this be done smarter?
    System.setProperty("java.util.logging.SimpleFormatter.format", '[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS][%4$-12s][%3$s]: %5$s%6$s%n')

    //TODO: handle file logging
    // FileHandler handler = new FileHandler("demo.log", true);
    // handler.setFormatter(new SimpleFormatter())
    // log.addHandler(handler)

    log.info("Hello world Manzbot!")

    // load config
    def configManager = new ConfigManager()
    def config = configManager.getConfig()

    // init webdriver
    def webdriverConfig = config.webdriver
    System.setProperty("webdriver.gecko.driver", webdriverConfig.path);
    
    // init browserBot
    def tradePlatformConfig = config.tradePlatform
    def browserBot = new BrowserBot(tradePlatformConfig)
    browserBot.start()

    // init investers
    def assetsConfig = config.investers
    def investersManager = new InvestersManager(assetsConfig)

    log.info("Dirty job done...")
  }

  def getGreeting() {
    return "Hello!"
  }
}
