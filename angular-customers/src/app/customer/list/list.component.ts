import { Component, ViewChild } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';
import { Customer } from 'src/app/shared/interfaces/customer.interface';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { FormComponent } from '../form/form.component';
import { CoreService } from 'src/app/core/core.service';


@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {
  constructor(private customerService: CustomerService,private dialog: MatDialog, private coreService: CoreService) { }

  displayedColumns: string[] = ['id','firstname', 'lastname', 'email', 'createdAt', 'action'];
  dataSource = new MatTableDataSource<Customer>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  customers!: Customer[]

  ngOnInit(): void {
    this.getCustomersList();
  }

  getCustomersList() {
    this.customerService.loadAllCustomers().subscribe({
      next: (data: any) => {
        this.customers = data?.data?.customers;
        this.dataSource = new MatTableDataSource(data?.data?.customers);
        this.dataSource.paginator = this.paginator
      },
      error: err => {
        console.log(err);
      }
    })
  }

  deleteCustomer(id: number) {
    if (id !== undefined) {
      this.customerService.deleteCustomersById(id).subscribe({
        next: () => {
          this.coreService.openSnackBar('Customer Deleted Successfully!', 'done');
          this.getCustomersList();
        },
        error: (err) => {
          console.log(err);
        }
      })
    } else {
      console.error('Invalid customer ID');
    }
  }

  openEditDialog(custemr: Customer) {
    const dialogRef = this.dialog.open(FormComponent, {
      data: custemr
    })

    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.getCustomersList();
        }
      }
    })
  }
}
