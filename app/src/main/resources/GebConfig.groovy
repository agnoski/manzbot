import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxDriver

waiting {
    presets {
        slow {
            timeout = 60
            retryInterval = 1
        }
        quick {
            timeout = 5
        }
    }
}

driver = {
    FirefoxOptions options = new FirefoxOptions()
        .addPreference("intl.accept_languages", "en-us")
    def driverInstance = new FirefoxDriver(options);
    driverInstance
}
