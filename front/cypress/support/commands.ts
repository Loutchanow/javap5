Cypress.Commands.add('loginMock', () => {
  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      firstName: 'first',
      lastName: 'last',
      admin: true,
      token: 'fake-jwt-token',
    },
  });

  cy.intercept('GET', '/api/session', []).as('getSessions');
  cy.intercept('GET', '/api/user/1', {
    id: 1,
    email: 'mail@gmail.com',
    firstName: 'first',
    lastName: 'last',
    password: '',
    admin: false,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
  });

  cy.visit('/login');
  cy.get('input[formControlName=email]').type('mail@gmail.com');
  cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
  cy.url().should('include', '/sessions');
});
