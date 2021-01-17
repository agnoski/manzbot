package app

import groovy.util.logging.Log
import geb.Page
import geb.module.Select

@Log
class WebTerminal extends Page {
  static content = {
    menuBar { module MenuBar }
    toolBar { version -> module(new ToolBar(version: version)) }

    fileMenu { module FileMenu }

    loginWindow { module LoginWindow }
    symbolsWindow { module SymbolsWindow }
    orderWindow { version -> module(new OrderWindow(version: version)) }

    buyButton { $("div.input-trade-button")[1] }
    loader { $("div.loader") }
  }

  def getButton(text) {
    def button = $("button.input-button", text: text)
    if(button) {
      return button.find { it.displayed }
    } else {
      return null
    }
  }

  def getWindowCloseButton() {
    def button = $("div.page-window.modal div.w div.wx span.i")
    if(button) {
      return button.find { it.displayed }
    } else {
      return null
    }
  }

  void closeWindow() {
    def windowCloseButton = getWindowCloseButton()
    if(windowCloseButton) {
      windowCloseButton.click()
      log.info("Tried to close window")
    } else {
      log.info("No widows to close")
    }
  }

  void clickButton(text) {
    def button = getButton(text)
    if(button) {
      button.click()
    } else {
      log.severe("Button $text not found")
    }
  }
}
