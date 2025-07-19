import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { expect } from '@jest/globals';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const baseUrl = 'api/session';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    const mockSessions: Session[] = [
      {
        id: 1,
        name: 'Yoga',
        description: 'Morning session',
        date: new Date('2025-12-01'),
        teacher_id: 1,
        users: [],
      },
    ];

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should fetch session detail by id', () => {
    const mockSession: Session[] = [
      {
        id: 1,
        name: 'Yoga',
        description: 'Morning session',
        date: new Date('2025-12-01'),
        teacher_id: 1,
        users: [],
      },
    ];

    service.detail('1').subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete session by id', () => {
    service.delete('1').subscribe((res) => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({ success: true });
  });

  it('should create a new session', () => {
    const newSession: Session = {
      id: 1,
      name: 'Yoga',
      description: 'Morning session',
      date: new Date('2025-12-01'),
      teacher_id: 1,
      users: [],
    };

    service.create(newSession).subscribe((session) => {
      expect(session).toEqual(newSession);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(newSession);
  });

  it('should update a session by id', () => {
    const updatedSession: Session = {
      id: 1,
      name: 'Yoga',
      description: 'Morning session',
      date: new Date('2025-12-01'),
      teacher_id: 1,
      users: [],
    };

    service.update('1', updatedSession).subscribe((session) => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should participate in a session', () => {
    service.participate('1', '42').subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/participate/42`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should unparticipate from a session', () => {
    service.unParticipate('1', '42').subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/participate/42`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
