package app

class App {

  static void main(String[] args) {
    println("Hello world Manzbot!")

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

    println("Dirty job done...")
  }
}
