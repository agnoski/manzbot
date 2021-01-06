package app

import geb.Page
import geb.module.Select

class WebTerminal extends Page {
  static content = {
    menuBar { module MenuBar }
    toolBar { module ToolBar }

    loginWindow { module LoginWindow }
    orderWindow { module OrderWindow }

    buyButton { $("div.input-trade-button")[1] }

    symbolsItems { $("div.items") }

    loginToTradeAccount { $("span.label", text: "Login to Trade Account") }
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
      println("Tried to close window")
    } else {
      println("No widows to close")
    }
  }

  void clickButton(text) {
    def button = getButton(text)
    if(button) {
      button.click()
    }
    else {
      println("Button $text not found")
    }
  }

  void clickIndexCategory(indexCategoryName) {
    waitFor('quick') { symbolsItems.displayed }
    def indexCategory = $("span.label", text: indexCategoryName)
    indexCategory.click()
  }

  void login(credentials) {
    menuBar.file.click()
    waitFor('slow') { loginToTradeAccount.displayed }
    interact { moveToElement(loginToTradeAccount) }
    loginToTradeAccount.click()
    loginWindow.login(credentials)
    sleep(3000)
  }
}
