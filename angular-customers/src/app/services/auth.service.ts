import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http'
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { RegisterCustomer } from '../shared/interfaces/register-customer.interface';
import { of } from 'rxjs';

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

  public validateToken() {
    const token = window.localStorage.getItem('token');
    if (token) {
      const options = token ? { params: new HttpParams().set('jwt', token) } : {};
      // console.log(options)
      return this.http.get(`${this.baseURL}/validate`, options);
    } else {
      return of(false)
    }
  }

  public logoutApi() {
    return this.http.post(`${this.baseURL}/logout`,{});
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

