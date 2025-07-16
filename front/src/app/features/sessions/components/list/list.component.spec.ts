import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { of } from 'rxjs';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { RouterModule } from '@angular/router';
describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Yoga Matin',
      date: new Date('2025-07-20'),
      description: 'Séance de yoga matinale',
      teacher_id: 1,
      users: [2],
    },
    {
      id: 2,
      name: 'Yoga Soir',
      date: new Date('2025-07-20'),
      description: 'Séance de yoga du soir',
      teacher_id: 2,
      users: [2],
    },
  ];

  const mockSessionService = {
    sessionInformation: {
      id: 42,
      email: 'john@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: '',
      admin: true,
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  };

  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of(mockSessions)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        RouterModule.forRoot([]),
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return user session information', () => {
    expect(component.user).toEqual(mockSessionService.sessionInformation);
  });

  it('should provide sessions observable', (done) => {
    component.sessions$.subscribe((sessions) => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(mockSessions);
      done();
    });
  });
});
