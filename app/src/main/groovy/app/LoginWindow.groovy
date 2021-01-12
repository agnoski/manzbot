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

    this.setInputValue(loginInput, credentials.user)
    this.setInputValue(passwordInput, credentials.password)
    this.setInputValue(serverInput, credentials.server)

    button("OK").click()
  }

  private void setInputValue(input, value) {
    input.click()
    input.value(value)
  }
}
