package app

class App {

  static void main(String[] args) {
    println("Hello world Manzbot!")

    // load config
    def configManager = new ConfigManager()

    // init webdriver
    def webdriverConfig = configManager.webdriver
    System.setProperty("webdriver.gecko.driver", webdriver.path);
    
    // init browserBot
    def browserBotConfig = configManager.tradePlatform
    def browserBot = new BrowserBot(tradePlatform)
    browserBot.start()

    // init investers
    def assetsConfig = configManager.assets
    def investersManager = new InvestersManager(assetsConfig)

    println("Dirty job done...")
  }
}
