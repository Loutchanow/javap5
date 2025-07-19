import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call POST /api/auth/register with correct body', () => {
    const mockRequest: RegisterRequest = {
      email: 'test@mail.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };

    service.register(mockRequest).subscribe();

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);

    req.flush(null);
  });

  it('should call POST /api/auth/login and return session info', () => {
    const loginRequest: LoginRequest = {
      email: 'test@mail.com',
      password: 'password123',
    };

    const mockResponse: SessionInformation = {
      token: 'abc123',
      type: 'Bearer',
      id: 1,
      username: 'john',
      firstName: 'John',
      lastName: 'Doe',
      admin: true,
    };

    service.login(loginRequest).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(loginRequest);

    req.flush(mockResponse);
  });
});
