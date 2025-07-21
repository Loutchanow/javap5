describe('Sessions - Full', () => {
  beforeEach(() => {
    cy.loginMock();
    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Yoga du matin',
        description: 'Relaxation',
        date: new Date('2025-12-01'),
        teacher_id: 1,
        users: [],
      },
    ]).as('getSessions');

    cy.intercept('GET', '/api/teacher', [
      {
        id: 1,
        lastName: 'Prof',
        firstName: 'Jean',
      },
    ]).as('getTeachers');
  });

  it('should create then delete a session', () => {
    cy.contains('Create').click();
    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type('Yoga Sunset');
    cy.get('input[formControlName=date]').type('2025-12-25');
    cy.get('[data-testid="teacher-select"]').click();
    cy.get('mat-option').contains('Jean').click();
    cy.get('textarea[formControlName=description]').type('Fin de journée zen');

    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      body: {
        id: 2,
        name: 'Yoga Sunset',
        description: 'Fin de journée zen',
        date: new Date('2025-12-25'),
        teacher_id: 1,
        users: [],
      },
    }).as('createSession');

    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Yoga du matin',
        description: 'Relaxation',
        date: new Date('2025-12-01'),
        teacher_id: 1,
        users: [],
      },
      {
        id: 2,
        name: 'Yoga Sunset',
        description: 'Fin de journée zen',
        date: new Date('2025-12-25'),
        teacher_id: 1,
        users: [],
      },
    ]).as('getSessionsAfterCreate');

    cy.get('button[type=submit]').click();
    cy.wait('@createSession');
    cy.wait('@getSessionsAfterCreate');

    cy.url().should('include', '/sessions');
    cy.contains('Yoga Sunset');

    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Yoga du matin',
      description: 'Relaxation',
      date: new Date('2025-12-01'),
      teacher_id: 1,
      users: [],
    }).as('getSessionToEdit');

    cy.contains('Edit').click();
    cy.wait('@getSessionToEdit');
    cy.url().should('include', '/sessions/update/1');

    cy.get('input[formControlName=name]').clear().type('Yoga Sunset Modifié');
    cy.get('textarea[formControlName=description]')
      .clear()
      .type('Modifié pour une meilleure détente');

    cy.intercept('PUT', '/api/session/1', {
      id: 1,
      name: 'Yoga Sunset Modifié',
      description: 'Modifié pour une meilleure détente',
      date: new Date('2025-12-25'),
      teacher_id: 1,
      users: [],
    }).as('updateSession');

    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Yoga Sunset Modifié',
        description: 'Modifié pour une meilleure détente',
        date: new Date('2025-12-25'),
        teacher_id: 1,
        users: [],
      },
    ]).as('getSessionsAfterUpdate');
    cy.get('button[type=submit]').click();
    cy.wait('@updateSession');
    cy.wait('@getSessionsAfterUpdate');
    cy.url().should('include', '/sessions');
    cy.contains('Yoga Sunset Modifié');
    cy.contains('Modifié pour une meilleure détente');

    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Yoga du matin',
      description: 'Relaxation',
      date: new Date('2025-12-01'),
      teacher_id: 1,
      users: [],
    }).as('getSessionDetail');

    cy.intercept('GET', '/api/teacher/1', {
      id: 1,
      firstName: 'Jean',
      lastName: 'Prof',
    }).as('getTeacherDetail');

    cy.contains('Detail').click();
    cy.url().should('include', '/sessions/detail/1');

    cy.wait('@getSessionDetail');
    cy.wait('@getTeacherDetail');

    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200,
    }).as('deleteSession');

    cy.get('[data-testid="delete-button"]').click();
    cy.wait('@deleteSession');

    cy.url().should('include', '/sessions');
    cy.contains('Yoga du matin').should('not.exist');
    cy.contains('Session deleted !');
  });
});
