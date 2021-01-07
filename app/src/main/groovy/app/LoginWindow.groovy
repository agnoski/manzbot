package app

class LoginWindow extends BaseWindow {
  static content = {
    loginInput { $("input#login") }
    passwordInput { $("input#password") }
    serverInput { $("input#server") }
  }

  void login(credentials) {
    waitFor('quick') {
      loginInput.displayed
      passwordInput.displayed
      serverInput.displayed
    }

    loginInput.click()
    loginInput.value(credentials.user)

    passwordInput.click()
    passwordInput.value(credentials.password)

    serverInput.click()
    serverInput.value(credentials.server)

    button("OK").click()
  }
}
