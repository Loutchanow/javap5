import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { take } from 'rxjs/operators';

describe('SessionService', () => {
  let service: SessionService;

  const mockSession: SessionInformation = {
    token: 'fake-jwt-token',
    type: 'Bearer',
    id: 1,
    username: 'john.doe',
    firstName: 'John',
    lastName: 'Doe',
    admin: true,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in and emit true', (done) => {
    service.logIn(mockSession);

    expect(service.sessionInformation).toEqual(mockSession);
    expect(service.isLogged).toBe(true);

    service
      .$isLogged()
      .pipe(take(1))
      .subscribe((value) => {
        expect(value).toBe(true);
        done();
      });
  });

  it('should log out and emit false', (done) => {
    service.logIn(mockSession);
    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBe(false);

    service
      .$isLogged()
      .pipe(take(1))
      .subscribe((value) => {
        expect(value).toBe(false);
        done();
      });
  });
});
