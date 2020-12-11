package app

import groovy.json.JsonSlurper

class Config {
  private def path
  private def config
  
  Config() {
    def path = "/home/agno/Dev/gradleTest/app/app/src/main/groovy/app/config.json"
    this(path)
  }

  Config(path) {
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
