package app

import geb.Module

class MenuBar extends Module {
  static content = {
    file { $("div.page-menu div.menu div.first")[0].find(text: "File") }
  }
}
