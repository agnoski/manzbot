package app

import geb.Module

class ToolBar extends Module {
  String version

  static content = {
    symbols { version == "4" ? $("a.page-bar-button.iconed", title: "Symbols") : $("a.at-symbols-button") }
    newOrder { version == "4" ? $("a.page-bar-button.iconed", title: "New Order") : $("a.at-new-order-button") }
  }
}
