import { Routes } from '@angular/router';
import { inject } from "@angular/core";
import { UserService } from "./core/auth/services/user";
import { map } from "rxjs/operators";

export const routes: Routes = [
  {
    path: "login",
    loadComponent: () => import("./core/auth/auth"),
    canActivate: [
      () => inject(UserService).isAuthenticated.pipe(map((isAuth) => !isAuth)),
    ],
  },
  {
    path: "register",
    loadComponent: () => import("./core/auth/auth"),
    canActivate: [
      () => inject(UserService).isAuthenticated.pipe(map((isAuth) => !isAuth)),
    ],
  }
];
