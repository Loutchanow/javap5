import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { MeComponent } from './me.component';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      id: 1,
      admin: false,
    },
    logOut: jest.fn(),
  };

  const mockUser = {
    getById: jest.fn().mockReturnValue(
      of({
        id: 1,
        email: 'john@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: '',
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date(),
      })
    ),
    delete: jest.fn().mockReturnValue(of({})),
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn().mockReturnValue(of({})),
  };

  const mockRouter = {
    navigate: jest.fn(),
  };

  const mockSnackBar = {
    open: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        HttpClientModule,
        MatSnackBarModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatButtonModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockSnackBar },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user on init', () => {
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should go back on back()', () => {
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation();
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should delete user, log out, show snackbar and navigate home', () => {
    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
