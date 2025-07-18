declare namespace Cypress {
  interface Chainable {
    loginMock(): Chainable<void>;
  }
}
