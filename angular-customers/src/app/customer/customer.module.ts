import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list/list.component';
import { HeaderComponent } from '../shared/header/header.component';
import { LayoutModule } from '../shared/modules/layout/layout.module';
import { FormComponent } from './form/form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ContainerComponent } from './container/container.component';
import { RouterModule } from '@angular/router';
import { CUSTOMER_ROUTES } from './customer.routing';



@NgModule({
  declarations: [
    ListComponent,
    HeaderComponent,
    FormComponent,
    ContainerComponent,
  ],
  imports: [
    CommonModule,
    LayoutModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule.forChild(CUSTOMER_ROUTES),
  ],
  exports: [
    ListComponent,
    FormComponent,
    ContainerComponent
  ]
})
export class CustomerModule { }
