package app

import groovy.json.JsonSlurper

class ConfigManager {
  private def path
  private def config
  
  ConfigManager() {
    this("/home/agno/Dev/gradleTest/app/app/src/main/groovy/app/config.json")
  }

  ConfigManager(path) {
    this.path = path
    this.config = loadConfig()
  }

  def getConfig() {
    return this.config
  }

  private def loadConfig() {
    def jsonSlurper = new JsonSlurper()
    return jsonSlurper.parse(new File(this.path))
  }
}
