import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog'
import { Router } from '@angular/router';
import { FormComponent } from 'src/app/customer/form/form.component';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService, private dialog: MatDialog, private router: Router) {}

  ngOnInit(): void {}

  logout() {
    this.authService.logout();
  }

  openAddEditEmpForm() {
    const dialogRef = this.dialog.open(FormComponent);
    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.redirectTo('/customers/list');
        }
      }
    });
  }

  redirectTo(url: string): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([url]);
    });
  }
}
