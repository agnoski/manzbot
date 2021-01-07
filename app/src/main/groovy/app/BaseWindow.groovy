package app

import geb.Module

class BaseWindow extends Module {
  static content = {
    button { text -> $("button.input-button", text: text).find { it.displayed } }
  }
}
