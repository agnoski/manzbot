package app

import geb.Module

class ToolBar extends Module {
  static content = {
    symbols { $("a.at-symbols-button") }
    newOrder { $("a.at-new-order-button") }
  }
}
