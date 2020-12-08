package app

import geb.Module

class ManualMenu extends Module { 
  static content = { 
    toggle { $("div.menu a.manuals") }
    linksContainer { $("#manuals-menu") }
    links { linksContainer.find("a") } 
  }

  void open() { 
    toggle.click()
    waitFor { !linksContainer.hasClass("animating") }
  }
}
