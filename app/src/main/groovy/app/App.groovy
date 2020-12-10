package app

class App {
    String getGreeting() {
        return 'Hello World!'
    }

    static void main(String[] args) {
        println new App().greeting

        System.setProperty("webdriver.gecko.driver", "/home/agno/Scaricati/geckodriver28");
        def browserBot = new BrowserBot()
        browserBot.start()
        def investersManager = new InvestersManager()
        println("Dirty job done...")
    }
}
