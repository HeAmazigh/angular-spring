import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CoreService } from 'src/app/core/core.service';
import { CustomerService } from 'src/app/services/customer.service';
import { Customer } from 'src/app/shared/interfaces/customer.interface';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {

  customerForm: FormGroup = new FormGroup({
    firstname: new FormControl('', [Validators.required]),
    lastname: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  error: string = "";

  constructor(
    private customrService: CustomerService,
    private dialogRef: MatDialogRef<FormComponent>,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) public customer: Customer,
    private coreService: CoreService
  ) { }

    ngOnInit(): void {
      this.customerForm.patchValue(this.customer);
    }

  onSubmit() {
    if (this.customerForm.valid) {
      if (this.customer) {
        let customerData: Customer = this.customerForm.getRawValue();
        customerData.id = this.customer.id
        customerData.createdAt = this.customer.createdAt
        this.customrService.updateCustomer(customerData).subscribe({
          next: (data) => {
            this.coreService.openSnackBar('Customer Updated Successfully');
            this.dialogRef.close(true);
          },
          error: (err) => {
            console.log(err);
          }
        })
      } else {
        const customer: Customer = this.customerForm.getRawValue();
        this.customrService.addCustomer(customer).subscribe({
          next: (data) => {
            this.coreService.openSnackBar('Customer Created Successfully');
            this.dialogRef.close(true);
          },
          error: (err) => {
            console.log(err);
          }
        })
      }
    }
  }

  // redirectTo(url: string): void {
  //   this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
  //     this.router.navigate([url]);
  //   });
  // }

  closeDialog() {
    this.dialogRef.close();
  }
}
