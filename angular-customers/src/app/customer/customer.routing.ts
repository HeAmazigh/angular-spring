import { Routes } from "@angular/router";
import { authenticateGuard } from "../guards/authenticate.guard";
import { ListComponent } from "./list/list.component";
import { ContainerComponent } from "./container/container.component";

export const CUSTOMER_ROUTES: Routes = [
  {
    path: '', canActivate: [authenticateGuard], component: ContainerComponent,
    children: [
      {
        path: 'list',
        component: ListComponent,
      },
    ],
  },
]
