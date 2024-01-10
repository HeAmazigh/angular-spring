import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    let req = request.clone({
      headers : request.headers.set('Content-Type', "application/json")
    })

    if (!req.url.includes("/auth/authenticate") || !req.url.includes("/auth/register")) {
      let newRequent = req.clone({
        headers : request.headers.set('Authorization', 'Bearer '+ this.authService.jwtToken)
      })
      return next.handle(newRequent).pipe(
        catchError(err => {
          // console.log("err.status", err.status );
          if (err.status == 403) {
            this.authService.logout();
          }
          return throwError(err.message)
        })
      );
    } else return next.handle(request);
  }
}
