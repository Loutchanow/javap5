import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router, ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { FormComponent } from './form.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let fb: FormBuilder;

  const mockRouter = {
    navigate: jest.fn(),
    url: '/sessions/create',
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('123'),
      },
    },
  };

  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of({})),
    update: jest.fn().mockReturnValue(of({})),
    detail: jest.fn().mockReturnValue(
      of({
        name: 'Yoga',
        date: '2025-07-20T00:00:00.000Z',
        teacher_id: 1,
        description: 'Stretching',
      })
    ),
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([])),
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  const mockSessionService = {
    sessionInformation: { admin: true },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatSelectModule,
        MatSnackBarModule,
        BrowserAnimationsModule,
      ],
      declarations: [FormComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fb = TestBed.inject(FormBuilder);
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to /sessions if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;

    component.ngOnInit();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should call create on submit if not updating', () => {
    component.onUpdate = false;
    component.sessionForm = fb.group({
      name: ['Test'],
      date: ['2025-07-20'],
      teacher_id: [1],
      description: ['Test session'],
    });

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith({
      name: 'Test',
      date: '2025-07-20',
      teacher_id: 1,
      description: 'Test session',
    });
  });

  it('should call update on submit if updating', () => {
    component.onUpdate = true;
    component['id'] = '123';
    component.sessionForm = fb.group({
      name: ['Update'],
      date: ['2025-07-21'],
      teacher_id: [2],
      description: ['Updated desc'],
    });

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('123', {
      name: 'Update',
      date: '2025-07-21',
      teacher_id: 2,
      description: 'Updated desc',
    });
  });

  it('should open snackbar and navigate on exitPage()', () => {
    component['exitPage']('Hello');

    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Hello', 'Close', {
      duration: 3000,
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should load session detail and init form in update mode', () => {
    mockRouter.url = '/sessions/update/123';
    component.ngOnInit();
    expect(component.onUpdate).toBe(true);
    expect(component['id']).toBe('123');
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
  });
});
