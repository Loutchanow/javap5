describe('MeComponent (user profile)', () => {
  beforeEach(() => {
    cy.loginMock();
    cy.contains('Account').click();
    cy.url().should('include', '/me');

    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        email: 'mail@gmail.com',
        firstName: 'first',
        lastName: 'last',
        password: '',
        admin: false,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      },
    }).as('getUser');
  });

  it('should display user info on /me', () => {
    cy.contains('mail@gmail.com');
    cy.contains('first');
    cy.contains('LAST');
  });

  it('should go back when clicking Back', () => {
    cy.go('back');
  });

  it('should delete account and redirect to home', () => {
    cy.intercept('DELETE', '/api/user/1', { statusCode: 200 }).as('deleteUser');
    cy.contains('Detail').click();
    cy.wait('@deleteUser');
    cy.url().should('include', '/');
  });
});
