import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockRouter = {
    navigate: jest.fn(),
  };

  const mockAuthService = {
    register: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be invalid when form is empty', () => {
    component.form.setValue({
      email: '',
      firstName: '',
      lastName: '',
      password: '',
    });
    expect(component.form.invalid).toBe(true);
  });

  it('should call authService.register and navigate on success', () => {
    const formValues = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'pwd123',
    };
    component.form.setValue(formValues);
    mockAuthService.register.mockReturnValue(of(void 0));

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalledWith(formValues);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should set onError to true if register fails', () => {
    component.form.setValue({
      email: 'fail@example.com',
      firstName: 'Fail',
      lastName: 'Error',
      password: 'fail123',
    });
    mockAuthService.register.mockReturnValue(
      throwError(() => new Error('fail'))
    );

    component.submit();

    expect(component.onError).toBe(true);
  });

  it('should be invalid if firstName is too short', () => {
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'A',
      lastName: 'Doe',
      password: 'password',
    });
    expect(component.form.invalid).toBe(true);
  });

  it('should be invalid if lastName is too long', () => {
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'a'.repeat(22),
      password: 'password',
    });
    expect(component.form.invalid).toBe(true);
  });
});
