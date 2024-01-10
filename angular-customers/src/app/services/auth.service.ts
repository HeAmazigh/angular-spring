import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RegisterCustomer } from '../shared/interfaces/register-customer.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseURL: string = 'http://localhost:8080/api/auth';

  isAuthenticated: boolean = false;
  username!: any;
  jwtToken!: string;

  constructor(private http: HttpClient, private router: Router) { }

  public login(email: string, password: string) {
    return this.http.post(`${this.baseURL}/authenticate`, {email, password});
  }

  public register(customer: RegisterCustomer) {
    return this.http.post(`${this.baseURL}/register`, customer);
  }

  public loadProfil(userToken: any) {
    this.isAuthenticated = true;
    this.jwtToken = userToken.data.token;

    let decoderJwt = jwtDecode(this.jwtToken)
    this.username = decoderJwt?.sub;
    window.localStorage.setItem('token', userToken.data.token)
    this.router.navigateByUrl('/customers/list');
  }

  public logout() {
    this.isAuthenticated = false;
    this.jwtToken = '';
    this.username = null;
    window.localStorage.removeItem('token');
    this.router.navigateByUrl('/login');
  }

  loadToken() {
    const token = window.localStorage.getItem('token');
    if (token == null) {
      this.router.navigateByUrl('/login');
    } else {
      const userToken = {
        data: {
          token: token
        }
      }
      this.loadProfil(userToken);
    }
  }
}

