import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';

describe('AppComponent', () => {
  let mockRouter: any;
  let mockSessionService: any;

  beforeEach(async () => {
    mockRouter = { navigate: jest.fn() };
    mockSessionService = {
      logOut: jest.fn(),
      $isLogged: jest.fn().mockReturnValue(of(true)),
    };

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule, MatToolbarModule],
      declarations: [AppComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should return isLogged observable from session service', (done) => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.$isLogged().subscribe((value) => {
      expect(value).toBe(true);
      expect(mockSessionService.$isLogged).toHaveBeenCalled();
      done();
    });
  });

  it('should log out and navigate to root on logout()', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.logout();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
